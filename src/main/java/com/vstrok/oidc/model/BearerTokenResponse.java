
package com.vstrok.oidc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BearerTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("id_token")
    private String idToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("x_refresh_token_expires_in")
    private Long xRefreshTokenExpiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getXRefreshTokenExpiresIn() {
        return xRefreshTokenExpiresIn;
    }

    public void setXRefreshTokenExpiresIn(Long xRefreshTokenExpiresIn) {
        this.xRefreshTokenExpiresIn = xRefreshTokenExpiresIn;
    }

}
