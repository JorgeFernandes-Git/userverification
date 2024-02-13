package userVerification.userverification.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import userVerification.userverification.appuser.AppUserService;

import java.util.List;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.requiresChannel(c -> c.requestMatchers("/actuator/**").requiresInsecure());
        http.authorizeHttpRequests(request -> {
            request.requestMatchers(
                    "/api/v*/registration/**",
                    "/register*",
                    "/login",
                    "/actuator/**").permitAll();
            request.anyRequest().authenticated();
        });
        http.formLogin(fL -> fL.loginPage("/login")
                .usernameParameter("email").permitAll()
                .defaultSuccessUrl("/", true)
                .failureUrl("/login-error"));
        http.logout(logOut -> logOut.logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "Idea-2e8e7cee")
                .logoutSuccessUrl("/login"));

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(appUserService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authProvider;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManager) throws BadCredentialsException {
        authenticationManager.authenticationProvider(daoAuthenticationProvider());
    }
}


