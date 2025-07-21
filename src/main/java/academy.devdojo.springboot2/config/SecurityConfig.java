package academy.devdojo.springboot2.config;

import academy.devdojo.springboot2.service.DomainUsersDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {

    private final DomainUsersDetailsService domainUsersDetailsService;

    /**
     * BasicAuthenticationFilter
     * UsernamePasswordAuthenticationFilter
     * DefaultLoginPageGeneratingFilter
     * DefaultLogoutPageGeneratingFilter
     * FilterSecurityInterceptor
     * Authentication -> Authorization
     *
     * @param http
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csfr -> csfr.disable())
                //.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests(authz -> authz.
                        requestMatchers("animes/admin/**").hasRole("ADMIN")
                        .requestMatchers("/animes/**").hasRole("USER")
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
               .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   // @Bean
    //public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {

        //UserDetails user = User.builder()
              //  .username("admin")
               // .password(encoder.encode("1234"))
              //  .roles("ADMIN")
               // .build();

       // UserDetails user2 = User.builder()
              //  .username("user")
              //  .password(encoder.encode("1234"))
              //  .roles("USER")
               // .build();

      //  return new InMemoryUserDetailsManager(user, user2);
   // }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        log.info("Password Encode {}", passwordEncoder().encode("1234"));
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(domainUsersDetailsService)
                .passwordEncoder(passwordEncoder());

        return builder.build();
    }


}




