package com.norbert.translator.translator;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TranslatorRequest(
        @JsonProperty("to_translate")
        String toTranslate,
        @JsonProperty("from_language")
        Language fromLanguage,
        @JsonProperty("to_language")
        Language toLanguage
) {

}
