package nextstep.app.config;

import nextstep.security.authentication.BasicAuthenticationFilter;
import nextstep.security.authentication.LoginAuthenticationFilter;
import nextstep.security.filter.DefaultSecurityFilterChain;
import nextstep.security.filter.FilterChainProxy;
import nextstep.security.filter.SecurityFilterChain;
import nextstep.security.service.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.GenericFilterBean;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

//    @Bean
//    public FilterRegistrationBean<Filter> basicAuthenticationFilter() {
//        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new BasicAuthenticationFilter(userDetailsService));
//        bean.setOrder(1);
//        bean.addUrlPatterns("/members");
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean<Filter> LoginAuthenticationFilter() {
//        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new LoginAuthenticationFilter(userDetailsService));
//        bean.setOrder(2);
//        bean.addUrlPatterns("/login");
//        return bean;
//    }

    @Bean
    public DelegatingFilterProxy delegatingFilterProxy() {
        return new DelegatingFilterProxy(filterChainProxy(List.of(securityFilterChain())));
    }

    @Bean
    public FilterChainProxy filterChainProxy(List<SecurityFilterChain> securityFilterChains) {
        return new FilterChainProxy(securityFilterChains);
    }

    @Bean
    public SecurityFilterChain securityFilterChain() {
        List<GenericFilterBean> filters =  List.of(
                new BasicAuthenticationFilter(userDetailsService),
                new LoginAuthenticationFilter(userDetailsService)
        );
        return new DefaultSecurityFilterChain(
                filters
        );
    }

}
