package com.noblesse.backend.oauth2.dto;

public class TokenDTO {
    private String token;
    private String refresh;

    public TokenDTO(String token, String refresh) {
        this.token = token;
        this.refresh = refresh;
    }
    public TokenDTO() {}

    public void setToken(String token) {
        this.token = token;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getToken() {
        return token;
    }
    public String getRefresh() {
        return refresh;
    }

}
