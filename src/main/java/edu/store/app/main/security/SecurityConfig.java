package edu.store.app.main.security;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import edu.store.database.service.CfgUserService;
import edu.store.vaadin.ui.LoginView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableVaadin(value = { "edu.store" })
@EnableScheduling
@EnableAsync
public class SecurityConfig extends VaadinWebSecurity {

    private final transient Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig() {}

    @Override
    @SuppressWarnings("all")
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth ->
            auth
                .requestMatchers(
                    new AntPathRequestMatcher("/images/**"),
                    new AntPathRequestMatcher("/register/**"),
                    new AntPathRequestMatcher("/checkUser/**"),
                    new AntPathRequestMatcher("/registerUser/**"),
                    new AntPathRequestMatcher("/test-request/**")
                )
                .permitAll()
        );

        super.configure(http);
        setLoginView(http, LoginView.class);
        http
            .formLogin()
            .successHandler(
                new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication
                    ) throws IOException, ServletException {
                        response.sendRedirect(request.getContextPath() + "/" + routeRecognizer(authentication));
                    }
                }
            );
        http.csrf(AbstractHttpConfigurer::disable);
    }

    private String routeRecognizer(Authentication authentication) {
        String authUserName = authentication.getName().toLowerCase();
        if (StringUtils.containsAnyIgnoreCase(authUserName, "kierownik", "sprzedawca", "bileter", "serwisant")) {
            return authUserName;
        }

        // normalni userzy routujemy po ich rolach
        Optional<? extends GrantedAuthority> role = authentication.getAuthorities().stream().findFirst();
        if (role.isPresent()) {
            return role.get().getAuthority().toLowerCase();
        } else {
            logger.error("Nie znaleziono zadnej roli dla zwyklego uzytkownika, nie mo wjazdu!");
            return "login";
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
