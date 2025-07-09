package com.uca.parcialfinalncapas.dto.response;

import java.time.LocalDate;

public class ErrorResponse {
    private Object message;
    private int status;
    private LocalDate time;
    private String uri;

    // Constructor vacío
    public ErrorResponse() {}

    // Constructor con parámetros
    public ErrorResponse(Object message, int status, LocalDate time, String uri) {
        this.message = message;
        this.status = status;
        this.time = time;
        this.uri = uri;
    }

    // Getters y Setters
    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    // Builder pattern manual
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
        private Object message;
        private int status;
        private LocalDate time;
        private String uri;

        public ErrorResponseBuilder message(Object message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder time(LocalDate time) {
            this.time = time;
            return this;
        }

        public ErrorResponseBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(message, status, time, uri);
        }
    }
}