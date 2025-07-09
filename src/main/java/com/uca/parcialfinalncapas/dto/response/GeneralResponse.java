package com.uca.parcialfinalncapas.dto.response;

import java.time.LocalDate;

public class GeneralResponse {
    private String uri;
    private String message;
    private int status;
    private LocalDate time;
    private Object data;

    public GeneralResponse() {}

    public GeneralResponse(String uri, String message, int status, LocalDate time, Object data) {
        this.uri = uri;
        this.message = message;
        this.status = status;
        this.time = time;
        this.data = data;
    }

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public LocalDate getTime() { return time; }
    public void setTime(LocalDate time) { this.time = time; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public static GeneralResponseBuilder builder() {
        return new GeneralResponseBuilder();
    }

    public static class GeneralResponseBuilder {
        private String uri;
        private String message;
        private int status;
        private LocalDate time;
        private Object data;

        public GeneralResponseBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public GeneralResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public GeneralResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        public GeneralResponseBuilder time(LocalDate time) {
            this.time = time;
            return this;
        }

        public GeneralResponseBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public GeneralResponse build() {
            return new GeneralResponse(uri, message, status, time, data);
        }
    }
}