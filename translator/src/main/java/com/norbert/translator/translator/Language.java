package com.norbert.translator.translator;

import lombok.Getter;

@Getter
public enum Language {
    ENGLISH("en"),
    UKRAINIAN("uk"),
    FRENCH("fr");

    private final String code;

    Language(String code) {
        this.code = code;
    }

}
