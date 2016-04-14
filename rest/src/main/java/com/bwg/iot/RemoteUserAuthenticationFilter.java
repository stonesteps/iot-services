package com.bwg.iot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by triton on 4/4/16.
 */
public class RemoteUserAuthenticationFilter extends OncePerRequestFilter{
    private final Logger log = LoggerFactory.getLogger(RemoteUserAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authUser = request.getHeader("remote_user");
        log.info("RemoteUserAuthFiler   remote_user: " + authUser);
        if (StringUtils.isNotEmpty(authUser)) {
            Authentication auth = new RemoteUserToken(authUser);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
