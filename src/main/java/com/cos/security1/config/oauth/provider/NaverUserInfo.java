package com.cos.security1.config.oauth.provider;

import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes; // oAuth2User.getAttributes()

    public NaverUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }
}
