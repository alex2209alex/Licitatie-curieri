package ro.fmi.unibuc.licitatie_curieri.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ro.fmi.unibuc.licitatie_curieri.common.utils.JWTUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JWTUtil jwtUtil;

    public SecurityConfiguration(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Permite accesul la endpoint-uri publice și protejează restul
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register", "/verify").permitAll() // Permite accesul public la aceste rute
                .anyRequest().authenticated() // Protejează restul endpoint-urilor
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class); // Adaugă filter-ul JWT
    }
}
