package nextstep.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultSecurityFilterChain extends SecurityFilterChain {

    private final List<Filter> filters;

    public DefaultSecurityFilterChain(final List<? extends Filter> filters) {
        this.filters =  new ArrayList<>(filters);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response) throws IOException, ServletException {
        super.doFilter(request, response);
    }

    @Override
    List<Filter> getFilters() {
        return this.filters;
    }

}
