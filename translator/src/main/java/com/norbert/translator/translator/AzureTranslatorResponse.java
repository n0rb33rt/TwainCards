package com.norbert.translator.translator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AzureTranslatorResponse {
    @JsonProperty("translations")
    private List<Translation> translations;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Translation {
        @JsonProperty("text")
        private String text;

        @JsonProperty("to")
        private String to;
    }

}
