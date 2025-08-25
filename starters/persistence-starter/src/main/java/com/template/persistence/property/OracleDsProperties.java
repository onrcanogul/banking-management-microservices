package com.template.persistence.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("devbank.datasource")
public class OracleDsProperties {
    private String url, username, password;
    private int poolSize = 15;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // getters/setters
}