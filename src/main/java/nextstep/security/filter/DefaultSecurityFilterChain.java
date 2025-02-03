package nextstep.security.filter;

import jakarta.servlet.Filter;

import java.util.ArrayList;
import java.util.List;

public class DefaultSecurityFilterChain extends SecurityFilterChain {

    private final List<Filter> filters;


    public DefaultSecurityFilterChain(final List<? extends Filter> filters) {
        this.filters = new ArrayList<>(filters);
    }

    @Override
    List<Filter> getFilters() {
        return this.filters;
    }


}
