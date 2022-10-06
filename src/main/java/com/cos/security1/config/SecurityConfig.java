package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity //활성화 (스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean // 해당 메서드의 리턴되는 오브젝트를 loC로 등록해준다.
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                // 위 세개 주소가 아니면 누구나 접근 가능
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm") //접근이 불가능한 페이지를 접속 했을 때 내가 설정한 페이지(login)주소로 이동
                .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행
                .defaultSuccessUrl("/"); //로그인 후 이동 페이지 (특정 페이지에서 로그인을 시도하면 로그인 후 그 페이지로 이동해줌)


    }
}
