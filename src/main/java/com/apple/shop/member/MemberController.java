package com.apple.shop.member;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/register")
    String register() {

        return "register.html";
    }

    @PostMapping("/member")
    String addMember(String username,String password, String displayName) throws Exception {
        memberService.saveMember(username, password, displayName);
        return "redirect:/list";
    }

    @GetMapping("/login")
    String login() {
        return "login.html";
    }

    @GetMapping("/my-page")
    public String myPage(Authentication auth) {

        return "mypage.html";
    }

    @GetMapping("/user/1")
    @ResponseBody
    public MemberDTO user() {
        var a = memberRepository.findById(1L);
        var result = a.get();
        var data = new MemberDTO(result.getUsername(), result.getDisplayName(), result.getId());
        return data;
    }
}

class MemberDTO {
    public String username;
    public String displayName;
    public Long id;

    public MemberDTO(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public MemberDTO(String username, String displayName, Long id) {
        this.username = username;
        this.displayName = displayName;
        this.id = id;
    }
}