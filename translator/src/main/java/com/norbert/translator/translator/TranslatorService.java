package com.norbert.translator.translator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.norbert.translator.exception.RequestSendingException;
import lombok.RequiredArgsConstructor;
import net.eledge.urlbuilder.UrlBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslatorService {
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;
    @Value("${azure.translator.api.url}")
    private String url;

    @Value("${azure.translator.api.location}")
    private String location;

    @Value("${azure.translator.api.key}")
    private String key;

    public TranslatorResponse translate(TranslatorRequest translatorRequest) {
        try {
            RequestBody body = buildRequestBody(translatorRequest);
            String url = buildUrl(translatorRequest);
            Request request = buildRequest(url, body);
            Response response = okHttpClient.newCall(request).execute();
            List<AzureTranslatorResponse> azureTranslatorResponses = objectMapper.readValue(response.body().string(), new TypeReference<>() {
            });
            return new TranslatorResponse(azureTranslatorResponses.get(0).getTranslations().get(0).getText());
        } catch (IOException e) {
            throw new RequestSendingException("Exception during sending the request. "+e.getMessage());
        }
    }

    private String buildUrl(TranslatorRequest request) {
        UrlBuilder urlBuilder = new UrlBuilder(url);
        urlBuilder.addParam("api-version", "3.0");
        urlBuilder.addParam("from", request.fromLanguage().getCode());
        urlBuilder.addParam("to", request.toLanguage().getCode());
        return urlBuilder.toString();
    }

    private RequestBody buildRequestBody(TranslatorRequest request) {
        MediaType mediaType = MediaType.parse("application/json");
        return RequestBody.create(mediaType,
                "[{\"Text\": \"" + request.toTranslate() + "\"}]");
    }

    private Request buildRequest(String url, RequestBody requestBody) {
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Ocp-Apim-Subscription-Key", key)
                .addHeader("Ocp-Apim-Subscription-Region", location)
                .addHeader("Content-type", "application/json")
                .build();
    }
}
