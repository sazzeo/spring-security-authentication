package nextstep.app.ui;

import jakarta.servlet.http.HttpSession;
import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.context.SecurityContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nextstep.security.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(
            HttpSession httpSession) {
        var session = (SecurityContext) httpSession.getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (session == null) {
            throw new AuthorizationException();
        }

        var member = memberRepository.findByEmail((String) session.getAuthentication().getPrincipal())
                .orElseThrow(AuthenticationException::new);

        //현재 인가 기능이 없어서 무조건 인가 에러 반환하도록 구현
        throw new AuthorizationException();
    }
}
