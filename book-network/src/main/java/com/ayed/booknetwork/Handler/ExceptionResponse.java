package com.ayed.booknetwork.Handler;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {
    private Integer businessErrorCode; // for business errors
    private String businessErrorDescription;
    private String error; // for other errors
    private Set<String> validationErrors;// for validation errors
    private Map<String, String> errors;// for other errors

}
