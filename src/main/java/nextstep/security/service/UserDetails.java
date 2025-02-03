package nextstep.security.service;

public interface UserDetails {
    Object getPrincipal();
    Object getCredentials();

    boolean match(Object credentials);
}
