package com.bwg.iot;

import com.bwg.iot.model.User;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        RemoteUserToken remoteUserToken = (RemoteUserToken) authentication;
        String currentUser = remoteUserToken.getPrincipal().toString();
        if (StringUtils.isEmpty(currentUser)){
            throw new UsernameNotFoundException("Could not authenticate user: No Username Provided");
        }

        User user = userRepository.findByUsername(currentUser);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user: " + currentUser);
        }
        List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>();
        user.getRoles().stream().forEach((role) -> {
            authorityList.add(new SimpleGrantedAuthority(role));
        });
        return new RemoteUserToken(user.getUsername(), null, authorityList);
    }

    @Override
    public boolean supports(Class<?> authentication){
        return RemoteUserToken.class.isAssignableFrom(authentication);
    }
}
