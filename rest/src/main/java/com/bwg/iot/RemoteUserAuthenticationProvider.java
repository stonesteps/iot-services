package com.bwg.iot;

import com.bwg.iot.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by triton on 4/4/16.
 */
@Component
public class RemoteUserAuthenticationProvider implements AuthenticationProvider {
    private final Logger log = LoggerFactory.getLogger(RemoteUserAuthenticationProvider.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("Entering Authentication Provider");

        RemoteUserToken remoteUserToken = (RemoteUserToken) authentication;
        String currentUser = remoteUserToken.getPrincipal().toString();
        if (StringUtils.isEmpty(currentUser)){
            throw new UsernameNotFoundException("Could not authenticate user: No Username Provided");
        }

        log.info("Auth  remote_user " + currentUser );


        User user = userRepository.findByUsername(currentUser);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user: " + currentUser);
        }
        log.info("Auth  Found User: " + user.getUsername() );

        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        user.getRoles().stream().forEach((role) -> {
            authorityList.add(new SimpleGrantedAuthority(role));
        });
        log.info("user " + user.getUsername() + "authenticated successfully");
        return new RemoteUserToken(user.getUsername(), null, authorityList);
    }

    @Override
    public boolean supports(Class<?> authentication){
        return RemoteUserToken.class.isAssignableFrom(authentication);
    }
}
