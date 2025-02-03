package nextstep.security.service;

public interface UserDetailsService {
    UserDetails findUserDetailsByPrincipal(Object principal);

}
