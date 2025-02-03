package nextstep.security.filter;

import jakarta.servlet.*;

import java.io.IOException;
import java.util.List;

public abstract class SecurityFilterChain implements FilterChain {

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {
        new VirtualFilterChain(getFilters(), this);
    }

    abstract List<Filter> getFilters();


}
