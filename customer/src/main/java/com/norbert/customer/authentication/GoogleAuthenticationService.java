package com.norbert.customer.authentication;


import com.norbert.customer.authentication.response.AuthenticationResponse;
import com.norbert.customer.exception.BadRequestException;
import com.norbert.customer.jwt.JwtService;
import com.norbert.customer.user.Role;
import com.norbert.customer.user.User;
import com.norbert.customer.authentication.request.GoogleAuthenticationRequest;
import com.norbert.customer.exception.GoogleResponseException;
import com.norbert.customer.user.UserService;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class GoogleAuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final OkHttpClient client;
    private final static String EMAIL_VERIFIED_PARAMETER = "email_verified";
    private final static String EMAIL_PARAMETER = "email";

    private record GoogleUserDetails(String email) {
    }

    public AuthenticationResponse authenticate(GoogleAuthenticationRequest request) {
        try {
            Response response = fetchUserDetails(buildUserDetailsRequest(request.accessToken()));
            GoogleUserDetails googleUserDetails = extractGoogleUserDetails(response);
            return generateAuthenticationResponse(googleUserDetails);
        } catch (IOException e){
            throw new GoogleResponseException("An error occurred. Try to use another way to register");
        } catch (JSONException e){
            throw new GoogleResponseException("Failed to extract Google user details from the response");
        }
    }

    private Response fetchUserDetails(Request userDetailsRequest) throws IOException, JSONException {

        Response response = client.newCall(userDetailsRequest).execute();
        if (!isGoogleEmailVerified(response))
            throw new BadRequestException("Your email is not verified. Try to use another one");
        response.close();
        return response;

    }

    public boolean isGoogleEmailVerified(Response userDetailsResponse) throws IOException, JSONException {
        if (userDetailsResponse.isSuccessful()) {
            String responseBody = userDetailsResponse.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            return (boolean) jsonObject.get(EMAIL_VERIFIED_PARAMETER);
        } else {
            throw new GoogleResponseException("Google response error. Try to use another way to register");
        }
    }

    private Request buildUserDetailsRequest(String accessToken) {
        return new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v3/userinfo")
                .header("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .get()
                .build();
    }

    private GoogleUserDetails extractGoogleUserDetails(Response response) throws JSONException, IOException {
        String email = extractParameter(response, EMAIL_PARAMETER);
        return new GoogleUserDetails(email);
    }

    private String extractParameter(Response response, String parameter) throws IOException, JSONException {
        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);
            return jsonObject.get(parameter).toString();
        } else {
            throw new GoogleResponseException("Google response error. Try to use another way to register");
        }
    }

    private AuthenticationResponse generateAuthenticationResponse(GoogleUserDetails googleUserDetails) {
        AuthenticationResponse authenticationResponse;
        String email = googleUserDetails.email;
        if (userService.isUserPresent(email)) {
            authenticationResponse = new AuthenticationResponse(jwtService.generateToken(userService.findUserByEmail(email)));
        } else {
            User user = registerUser(googleUserDetails);
            userService.save(user);
            authenticationResponse = new AuthenticationResponse(jwtService.generateToken(user));
        }
        return authenticationResponse;
    }

    private User registerUser(GoogleUserDetails googleUserDetails) {
        return User.builder()
                .role(Role.USER)
                .email(googleUserDetails.email)
                .setPassword(false)
                .enabled(true)
                .subscriptionUntil(null)
                .decks(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }
}

