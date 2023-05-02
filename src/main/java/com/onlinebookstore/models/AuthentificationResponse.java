package com.onlinebookstore.models;

public class AuthentificationResponse {
    private String jwt;

    public AuthentificationResponse() {
    }

    public AuthentificationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
