package nextstep.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.util.request.HttpRequestMatcher;
import nextstep.security.util.request.RequestMatcher;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

public class LoginAuthenticationFilter extends GenericFilterBean {
    private final AuthenticationManager authenticationManager;
    private final RequestMatcher requestMatcher;

    public LoginAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.requestMatcher = new HttpRequestMatcher(HttpMethod.POST, "/login");
    }

    public LoginAuthenticationFilter(final AuthenticationManager authenticationManager, final RequestMatcher requestMatcher) {
        this.authenticationManager = authenticationManager;
        this.requestMatcher = requestMatcher;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;

        if (!requestMatcher.matches(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        var httpServletResponse = (HttpServletResponse) response;

        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            String username = parameterMap.get("username")[0];
            String password = parameterMap.get("password")[0];

            var authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password));
            if (!authentication.isAuthenticated()) {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpServletResponse.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
