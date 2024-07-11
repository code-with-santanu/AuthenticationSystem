package com.santanu.jwtAuth.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private long timeStamp;

    private int status;

    private String message;

    private String path;

    @Override
    public String toString() {
        return "ErrorResponse{" +
                ", timeStamp=" + timeStamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", path=" + path +
                '}';
    }
}
