package nextstep.security.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.security.context.SecurityContextHolder;
import nextstep.security.util.Base64Convertor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class BasicAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    public BasicAuthenticationFilter(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        if (!httpServletRequest.getMethod().equals(HttpMethod.GET.name())) {
            chain.doFilter(request, response);
            return;
        }
        var authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization == null) {
            chain.doFilter(request, response);
            return;
        }
        var httpServletResponse = (HttpServletResponse) response;
        try {

            var credentials = authorization.split(" ")[1];
            String decodedString = Base64Convertor.decode(credentials);
            String[] usernameAndPassword = decodedString.split(":");
            String username = usernameAndPassword[0];
            String password = usernameAndPassword[1];

            var authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password));

            if (!authentication.isAuthenticated()) {
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        } finally {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);

    }

}
