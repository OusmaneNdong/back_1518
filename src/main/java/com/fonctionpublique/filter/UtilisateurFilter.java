package com.fonctionpublique.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fonctionpublique.security.JwtUtilisateur;
import com.fonctionpublique.utils.UtilisateurUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Component
public class UtilisateurFilter /*extends OncePerRequestFilter*/ {

    private final JwtUtilisateur jwtUtilisateur;
    private final UtilisateurUtil utilisateurUtil;

    private String userName;

    @Autowired
    public UtilisateurFilter(JwtUtilisateur jwtUtilisateur, UtilisateurUtil utilisateurUtil) {
        this.jwtUtilisateur = jwtUtilisateur;
        this.utilisateurUtil = utilisateurUtil;
    }

   /* @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = request.getHeader("Authorization");
        if (jwt == null || !jwt.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("test")).build();
        jwt = jwt.substring("Bearer ".length());
        DecodedJWT decodedJWT = verifier.verify(jwt);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("profile").asList(String.class);
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String r : roles)
            authorities.add(new SimpleGrantedAuthority(r));
        UsernamePasswordAuthenticationToken user = new
                UsernamePasswordAuthenticationToken(username,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(user);
        filterChain.doFilter(request,response);
    }*/

    public String getCurrentUser() {
        return Objects.requireNonNullElse(userName, "Ministère de la Fonction Public");
    }

}
