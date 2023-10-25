package jr.acens.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilterApplication;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/users/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/users/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/users/**").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/diretrizes/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.GET, "/diretrizes/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/diretrizes/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/diretrizes/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.POST, "/sintomas/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.PUT, "/sintomas/admin/**").hasRole("ADMIN");
                    req.requestMatchers(HttpMethod.DELETE, "/sintomas/admin/**").hasRole("ADMIN");

                    //deve-se criar manualmente no DB o registro de login do arildo
                    req.anyRequest().authenticated();
                })
                .addFilterBefore(securityFilterApplication, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
