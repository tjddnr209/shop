package com.apple.shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveMember(String username,
                           String password,
                           String displayName) throws Exception {
        var result = memberRepository.findByUsername(username);
        if (result.isPresent()){
            throw new Exception("존재하는아이디");
        }
        if (username.length() < 8 || password.length() < 8){
            throw new Exception("너무짧음");
        }
        Member member = new Member();
        member.setUsername(username);
        var hash = passwordEncoder.encode(password);
        member.setPassword(hash);
        member.setDisplayName(displayName);
        memberRepository.save(member);
    }

    public void getData() {
        var a = memberRepository.findById(1L);
        var result = a.get();
        var data = new MemberDTO(result.getUsername(), result.getDisplayName(), result.getId());
    }
}
