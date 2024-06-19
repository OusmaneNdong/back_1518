package com.fonctionpublique.security;

import com.fonctionpublique.filter.UtilisateurFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfigurer {

    private final UtilisateurFilter utilisateurFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/utilisateur/password-reset-request",
                        "/api/utilisateur/reset-password",
                        "/api/utilisateur/getUtilisateur",
                        "/api/utilisateur/utilisateurDetails/**",
                        "/api/utilisateur/forget-password",
                        "/api/utilisateur/set-password",
                        "/api/utilisateur/register",
                        "/api/utilisateur/authentication",
                        "/api/utilisateur/change-password",
                        "/api/utilisateur/change-password-test",
                        "/api/utilisateur/statut",

                        "/api/demandeur/demander",
                        "/api/demandeur/getDemandeur" ,
                        "/api/demandeur/demandeurDetails/**",
                        "/api/demandeur/details/**",
                        "/api/demandeur/getStatut/**",
                        "/api/dashbord/total_demandeur",
                        "/api/demadeur/getCours",
                        "/api/demande/bydemandeurid/**",


                        "/api/demande/demandez/**",
                        "/api/demande/demandeDetails/**",
                        "/api/demande/getDemande",
                        "/api/demande/getStatut/**",
                        "/api/demande/getDemandeurByDemande",

                        "/api/dashbord/total",
                        "/api/dashbord/total-cours",
                        "/api/dashbord/total-approuvees",
                        "/api/dashbord/total-rejetees",

                        "/api/dashbord/demande/total",
                        "/api/dashbord/demande/total-cours",
                        "/api/dashbord/demande/total-approuvees",
                        "/api/dashbord/demande/total-rejetees",


                        "/api/attestation/pdf_genere/**",

                        "/api/mail/mail_approuved",
                        "/api/mail/mail_rejete/**",
                        "/api/certification/generatedAttestation/**",

                        "/api/scanner/upload",
                        "/api/scanner/telechargement/**"

                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(utilisateurFilter, UsernamePasswordAuthenticationFilter.class)
                 .build()
         ;

      //  return security.build();

    }

    /*@Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://locahost:5300/node"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }*/
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
