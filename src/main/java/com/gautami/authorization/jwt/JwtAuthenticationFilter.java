package com.gautami.authorization.jwt;

import com.gautami.authorization.exception.InvalidRequest;
import com.gautami.authorization.exception.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    private final JwtAuthenticationHelper jwtHelper;


    private final UserDetailsService userDetailsService;

    private static final String EXPIRED_TOKEN_MESSAGE="The token has expired, please generate a new token";

    @Autowired
    public JwtAuthenticationFilter(JwtAuthenticationHelper jwtHelper,UserDetailsService userDetailsService){
        this.jwtHelper=jwtHelper;
        this.userDetailsService=userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        String username;
        String token;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            log.debug("Starting filtering of request!!!!!");
            token = requestHeader.substring(7);
            try {
                username = jwtHelper.getUsernameFromToken(token);
            } catch (ExpiredJwtException e) {
                // Handle expired token
                log.error(EXPIRED_TOKEN_MESSAGE, e);
                String errorMessage = "{ \"error\": \"" + EXPIRED_TOKEN_MESSAGE + "\" }";
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write(errorMessage);
                return;
            }
            log.info("Fetched the username from the token: {}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    log.info("User details fetched from db");
                    checkingTokenExpiration(token,response);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(token, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                } catch (NotFoundException e) {
                    // Handle NotFoundException
                    log.error("Username in the token not found, please provide a valid token", e);
                    String errorMessage = "{ \"error\": \"Username in the token not found, please provide a valid token\" }";
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.setContentType("application/json");
                    response.getWriter().write(errorMessage);
                    return;
                }

            }
        }
        filterChain.doFilter(request, response);
    }

    private void checkingTokenExpiration(String token ,HttpServletResponse response)throws IOException{
        try {
            jwtHelper.isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            // Handle expired token
            log.error(EXPIRED_TOKEN_MESSAGE, e);
            String errorMessage = "{ \"error\": \"" + EXPIRED_TOKEN_MESSAGE + "\" }";
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(errorMessage);
        }
    }


}
