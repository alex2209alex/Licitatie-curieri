package ro.fmi.unibuc.licitatie_curieri.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JwtAuthenticationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Verifică header-ul Authorization pentru un token JWT
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrage tokenul din header
            String token = authorizationHeader.substring(7);

            try {
                // Verifică și validează tokenul
                String username = jwtUtil.validateToken(token);

                // Creează un obiect de autentificare
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, null // Poți adăuga roluri aici dacă sunt necesare
                );

                // Setează autentificarea în contextul securității
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Dacă tokenul nu este valid, răspunde cu un cod de eroare
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        // Continuă cu filtrul următor (adică, permite accesul la endpoint-ul controller-ului)
        filterChain.doFilter(request, response);
    }
}
