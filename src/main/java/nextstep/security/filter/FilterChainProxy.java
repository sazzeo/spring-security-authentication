package nextstep.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

public class FilterChainProxy extends GenericFilterBean {
    private final List<SecurityFilterChain> securityFilterChains;

    public FilterChainProxy(final List<SecurityFilterChain> securityFilterChains) {
        this.securityFilterChains = securityFilterChains;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        new VirtualFilterChain(securityFilterChains.get(0).getFilters(), chain);
        chain.doFilter(request, response);
    }
}
