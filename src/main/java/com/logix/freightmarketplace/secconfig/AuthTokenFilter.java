/*
package com.logix.freightmarketplace.secconfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    JwtUtils jwtUtils;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtUtils.getJwtFromHeader(request);
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            // get user name from jwt

            //String email = jwtUtils.getUsernameFromJwtToken(jwt);

            */
/*exchange = exchange.mutate()
                    .request(r -> r.headers(h -> {
                        h.add("X-user-id", claims.getSubject());
                        h.add("X-user-role", role);
                        h.add("X-User-Email", email);
                    }))
                    .build();*//*


            //Claims claims = jwtUtils.getClaims(jwt);
            String userId = request.getHeader("X-user-id");//claims.getSubject();
            String role = request.getHeader("X-user-role");
            String email = request.getHeader("X-User-Email");//claims.getSubject();

            //load userdetails by username
            //UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // create authentication object
            */
/*UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //set security context
            SecurityContextHolder.getContext().setAuthentication(authentication);*//*


            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}*/
