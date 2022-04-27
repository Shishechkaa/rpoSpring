package ru.iu3.rpospring.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;
import ru.iu3.rpospring.domain.Usr;
import ru.iu3.rpospring.repo.UsrRepo;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Value("${private.session-timeout}")
    private int sessionTimeout;
    final UsrRepo usrRepo;

    AuthenticationProvider(UsrRepo usrRepo) {
        this.usrRepo = usrRepo;
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(
            String username,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        Object token = authentication.getCredentials();
        Optional<Usr> uu = usrRepo.findByToken(String.valueOf(token));
        if (!uu.isPresent())
            throw new UsernameNotFoundException("user not found");
        Usr u = uu.get();

        boolean timeout = true;
        LocalDateTime dateTime = LocalDateTime.now();
        if(u.getActivity() != null) {
            LocalDateTime newTime = u.getActivity().plusMinutes(sessionTimeout);
            if (dateTime.isBefore(newTime)) timeout = false;
        }
        if (timeout) {
            u.setToken(null);
            usrRepo.save(u);
            throw new NonceExpiredException("session is expired");
        } else {
            u.setActivity(dateTime);
            usrRepo.save(u);
        }

        return new User(
                u.getLogin(), u.getPassword(),
                true,
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList("USER"));
    }
}
