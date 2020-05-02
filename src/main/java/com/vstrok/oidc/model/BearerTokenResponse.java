
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

    public BearerTokenResponse setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public BearerTokenResponse setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getIdToken() {
        return idToken;
    }

    public BearerTokenResponse setIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public BearerTokenResponse setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Long getXRefreshTokenExpiresIn() {
        return xRefreshTokenExpiresIn;
    }

    public BearerTokenResponse setXRefreshTokenExpiresIn(Long xRefreshTokenExpiresIn) {
        this.xRefreshTokenExpiresIn = xRefreshTokenExpiresIn;
        return this;
    }

}
