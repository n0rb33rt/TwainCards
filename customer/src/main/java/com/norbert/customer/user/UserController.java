package com.norbert.customer.user;

import com.norbert.customer.user.request.PasswordUpdatingRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(){
        return ResponseEntity.ok(userService.getUserProfile());
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid PasswordUpdatingRequest request
    ){
        userService.updatePassword(request);
        return ResponseEntity.ok().build();
    }
}
