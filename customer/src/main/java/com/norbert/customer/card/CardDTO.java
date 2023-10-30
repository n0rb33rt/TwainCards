package com.norbert.customer.card;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Service;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDTO{
        @JsonProperty("id")
        private Long id;

        @JsonProperty("native_word")
        private String nativeWord;

        @JsonProperty("translation")
        private String translation;
        @JsonProperty("description")
        private String description;

}
