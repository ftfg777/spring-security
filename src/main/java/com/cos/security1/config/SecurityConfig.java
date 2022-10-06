package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//1.코드받기(인증) 2.액세스토큰(권한)
//3.사용자 프로필 정보 가져옴 4.정보를 토대로 회원가입을 자동 진행
//4-2 서비스를 위한 사용자 프로필 정보가 부족하면 추가로 기입해야 함
@Configuration
@EnableWebSecurity //활성화 (스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.)
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured 어노테이션 활성화, preAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig{

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Bean // 해당 메서드의 리턴되는 오브젝트를 loC로 등록해준다.
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // 위 세개 주소가 아니면 누구나 접근 가능
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")        //접근이 불가능한 페이지를 접속 했을 때 내가 설정한 페이지(login)주소로 이동
                .loginProcessingUrl("/login")   //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행
                .defaultSuccessUrl("/")         //로그인 후 이동 페이지 (특정 페이지에서 로그인을 시도하면 로그인 후 그 페이지로 이동해줌)
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserService); //구글 로그인이 완료된 뒤의 후처리가 필요. Tip.코드x (엑세스토큰+사용자 프로필 정보o)


        return http.build();

    }
}
