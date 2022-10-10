package com.cos.security1.config.oauth.provider;

import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes; // oAuth2User.getAttributes()
    public KakaoUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        // 수정 필요
        // {profile_nickname_needs_agreement=false, profile={nickname=정찬우}, has_email=true, email_needs_agreement=false, is_email_valid=true, is_email_verified=true, email=ftfg777@kakao.com}
        return attributes.get("kakao_account").toString(); //
    }

    @Override
    public String getName() {
        return attributes.get("properties").toString();
    }
}
