package nextstep.security.authentication;

import nextstep.app.ui.AuthenticationException;

public class DaoAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        return UsernamePasswordAuthenticationToken.authenticated(authentication.getPrincipal(), authentication.getCredentials());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
