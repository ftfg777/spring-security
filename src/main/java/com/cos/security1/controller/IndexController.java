package com.cos.security1.controller;

import com.cos.security1.model.ROLE;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view 리턴
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @GetMapping({"","/"})
    public String index(){
        //머스테치 기본폴더 src/main/resources (스프링이 권장함)
        //뷰리졸버 설정 : templates(prefix), .mustache(suffix) 의존성 추가하면 생략 가능
        return "index"; // src/main/resources/templates/index.mustache
    }

    @GetMapping("/user")
    public @ResponseBody String user(){

        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){

        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){

        return "manager";
    }

    // 스프링 시큐리티가 해당 주소를 낚아챔 - SecurityConfig 파일 생성 후 안 낚아챔
    @GetMapping("/loginForm")
    public String loginForm(){

        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){

        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        user.setRole(ROLE.ROLE_USER);

        // 패스워드 암호화
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        // DB 저장
        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정메소드에 간단하게 걸고 싶을 때
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data 메소드가 실행되기 직전에 실행됨, 여러 조건 걸기
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }




}
