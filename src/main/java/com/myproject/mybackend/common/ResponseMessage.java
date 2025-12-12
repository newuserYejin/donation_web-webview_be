package com.myproject.mybackend.common;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseMessage {

    private int httpStatusCode;
    private String message;
    private Map<String , Object> results;

}
