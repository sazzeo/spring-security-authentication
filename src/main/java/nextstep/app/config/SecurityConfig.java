package nextstep.app.config;

import jakarta.servlet.Filter;
import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.service.UserDetailsService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public FilterRegistrationBean<Filter> logFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();

        bean.setFilter(new BasicAuthenticationFilter(userDetailsService));
        bean.setOrder(1);
        bean.addUrlPatterns("/members");

        return bean;
    }

}
