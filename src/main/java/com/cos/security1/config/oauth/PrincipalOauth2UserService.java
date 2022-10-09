package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.ROLE;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    //구글에게 받은 userRequest 데이터에 대한 후처리 되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //Registration() 어떤 OAuth로 로그인 하는지 확인 가능
        System.out.println("getAttributes : " + super.loadUser(userRequest).getAttributes());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(OAuth-Client라이브러리) ->AccessToken요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원 프로필 받아준다
        System.out.println("getAdditionalParameters : " + userRequest.getAdditionalParameters());

        // 회원가입 강제로 진행
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){

        }

        if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){

        }

        if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){

        }

        String provider = userRequest.getClientRegistration().getRegistrationId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; //google_12321420140
        String password = passwordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        ROLE role = ROLE.ROLE_USER;

        User userEntity = userRepository.findByUsername(username);

        if(userEntity == null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }

        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
