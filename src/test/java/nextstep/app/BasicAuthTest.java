package nextstep.app;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.security.util.Base64Convertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasicAuthTest {
    private final Member TEST_MEMBER = new Member("a@a.com", "password", "a", "");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(TEST_MEMBER);
    }

    @DisplayName("Basic Auth 인증 성공 후 회원 목록 조회")
    @Test
    void members() throws Exception {
        String token = TEST_MEMBER.getEmail() + ":" + TEST_MEMBER.getPassword();
        String encoded = Base64Convertor.encode(token);

        ResultActions loginResponse = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + encoded)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );

        loginResponse.andDo(print());
        loginResponse.andExpect(status().isOk());
        loginResponse.andExpect(jsonPath("$[*].email", hasItem(TEST_MEMBER.getEmail())));
    }

    @DisplayName("Basic Auth 인증 실패 시 에러 응답")
    @Test
    void members_fail() throws Exception {
        ResultActions loginResponse = mockMvc.
                perform(get("/members")
                        .header("Authorization", "Basic " + "invalid")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                );

        loginResponse.andDo(print());
        loginResponse.andExpect(status().isUnauthorized());
    }
}
