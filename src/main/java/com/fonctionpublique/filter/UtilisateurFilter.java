package com.fonctionpublique.filter;

import com.fonctionpublique.security.JwtUtilisateur;
import com.fonctionpublique.utils.UtilisateurUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class UtilisateurFilter extends OncePerRequestFilter {

    private final JwtUtilisateur jwtUtilisateur;
    private final UtilisateurUtil utilisateurUtil;

    private String userName;

    @Autowired
    public UtilisateurFilter(JwtUtilisateur jwtUtilisateur, UtilisateurUtil utilisateurUtil) {
        this.jwtUtilisateur = jwtUtilisateur;
        this.utilisateurUtil = utilisateurUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Bearer ");
        String token = null;
        String username = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = utilisateurUtil.extractUsername(token);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = jwtUtilisateur.loadUserByUsername(username);

            if(utilisateurUtil.validationToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }

    public String getCurrentUser() {
        return Objects.requireNonNullElse(userName, "Ministère de la Fonction Public");
    }

}
