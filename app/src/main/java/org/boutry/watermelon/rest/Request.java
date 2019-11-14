package org.boutry.watermelon.rest;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Request {
    public enum Method {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        private String method;

        Method(String method) {
            this.method = method;
        }

        String getMethod() {
            return method;
        }
    }

    private Map<String, String> headers;
    private String endPoint;
    private Method method;
    private String body;

    public Request(@NonNull Method method, @NonNull String endPoint) {
        this(method, endPoint, new HashMap<>());
    }

    public Request(@NonNull Method method, @NonNull String endPoint, @NonNull Map<String,String> headers) {
        this.method = method;
        this.endPoint = endPoint;
        this.headers = headers;
    }

    public void addHeader(@NonNull String header, @NonNull String value) {
        this.headers.put(header, value);
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setBody(@NonNull String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getMethod() {
        return method.getMethod();
    }

}
