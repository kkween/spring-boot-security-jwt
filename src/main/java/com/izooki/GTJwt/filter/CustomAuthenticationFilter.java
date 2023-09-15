package com.izooki.GTJwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

//checks user when they are trying to log in.
//override attemptAuthentication() and successfulAuthentication
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    //step 1: will call to authenticate user
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    //step 2: work on attemptAuthentication() so we can call the AuthenticationManager
    //to pass in the user credentials and let spring do it's magic.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is: {}", username);
        log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        //call and return the authenticationManager
        return authenticationManager.authenticate(authenticationToken);
    }

    //step 3: Add CustomAuthenticationFilter to SecurityConfig class and
    // create the AuthenticationManager Bean

    //step 4: If login is successful, successfulAuthentication will be called,
    //which means we need to have a way to generate the token, sign the token
    // and send the tokens(access_token and refresh_token) to the user in the headers
    // of the client or response body of the client.
    //If authenticate is not successful, spring will send an error to the user
    //search for auth0 java jwt maven and add Jwt dependency to pom file
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //a. get user from spring core security and from the user, create the Jwt(s)
        User user = (User) authentication.getPrincipal();
        //b. define an Algorithm to sign the access_token and refresh_token
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + 1 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);

        //c. use the response to send the access_token and refresh_token to the user
        /*response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);*/

        //d. go to UserServiceImpl and add passwordEncoder() to the saveUser() method

        //e. test the application

        //instead of sending headers, send the tokens in the response body of postman
        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    //step 5: go to SecurityConfig class and add antMatchers
}
