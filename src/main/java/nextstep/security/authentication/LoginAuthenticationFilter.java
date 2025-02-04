package nextstep.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Map;

public class LoginAuthenticationFilter extends GenericFilterBean {
    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";
    private final AuthenticationManager authenticationManager;

    public LoginAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;

        if (!httpServletRequest.getMethod().equals(HttpMethod.POST.name())) {
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

            httpServletRequest.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY, authentication);
            httpServletResponse.setStatus(HttpStatus.OK.value());
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
