package ue.poznan.spring_jwt_auth.auth.config;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ue.poznan.spring_jwt_auth.auth.service.JwtService;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static ue.poznan.spring_jwt_auth.auth.config.JwtConstraintsUtils.REFRESH_TOKEN_PATH;
import static ue.poznan.spring_jwt_auth.auth.config.JwtConstraintsUtils.TOKEN_PREFIX;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!isNull(SecurityContextHolder.getContext().getAuthentication())) {
            log.trace("Not detected authentication");
            filterChain.doFilter(request, response);
            return;
        }

        var authenticationOptional = getAuthentication(request);
        if (authenticationOptional.isEmpty()) {
            log.trace("Not detected authentication context");
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(authenticationOptional.get());

        filterChain.doFilter(request, response);
    }

    private Optional<Authentication> getAuthentication(HttpServletRequest req) {
        var authorizationHeaderOptional = getAuthorizationHeader(req);
        if (authorizationHeaderOptional.isEmpty()) {
            log.trace("Authentication header is empty");
            return Optional.empty();
        }
        log.trace("Detected authentication");

        var claims = jwtService.extractClaims(authorizationHeaderOptional.get());
        var username = claims.getSubject();
        var userDetails = userDetailsService.loadUserByUsername(username);

        log.trace("Authenticate user: {}, with password: {}", userDetails.getUsername(), userDetails.getPassword());

        var tokenType = claims.get(JwtConstraintsUtils.Claims.JWT_TOKEN_TYPE, String.class);
        if (new Date().before(claims.getExpiration()) && ((req.getRequestURI().equals(REFRESH_TOKEN_PATH) &&
                tokenType.equals(JwtConstraintsUtils.Token.REFRESH.name())) || tokenType.equals(JwtConstraintsUtils.Token.AUTHORIZATION.name()))) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    jwtService.getRoles(claims).stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet())
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));


            log.trace("Auth token: {}", userDetails);
            return Optional.of(authToken);
        }

        return Optional.empty();
    }

    private Optional<String> getAuthorizationHeader(HttpServletRequest req) {
        var headerValue = req.getHeader(AUTHORIZATION);
        return StringUtils.isNotBlank(headerValue) && headerValue.startsWith(TOKEN_PREFIX) ? Optional.of(headerValue.substring(TOKEN_PREFIX.length())) : Optional.empty();
    }
}
