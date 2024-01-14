package com.norbert.translator.translator;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/translate")
@AllArgsConstructor
public class TranslatorController {
    private final TranslatorService translatorService;
    @PostMapping
    public ResponseEntity<TranslatorResponse> translate(@RequestBody @Valid TranslatorRequest request){
        return ResponseEntity.ok(translatorService.translate(request));
    }
}
