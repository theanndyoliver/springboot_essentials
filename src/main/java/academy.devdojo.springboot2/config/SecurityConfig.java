package academy.devdojo.springboot2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

    /**
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInterceptor
     * Authentication -> Authorization
     * @param http
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csfr -> csfr.disable())
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .formLogin(withDefaults())
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {

        UserDetails user = User.builder()
                .username("admin")
                .password(encoder.encode("1234"))
                .roles("ADMIN")
                .build();

        UserDetails user2 = User.builder()
                .username("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user,user2);
    }



}




