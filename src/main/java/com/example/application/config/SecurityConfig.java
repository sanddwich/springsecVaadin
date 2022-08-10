package com.example.application.config;

import com.example.application.security.jwt.JwtConfigurer;
import com.example.application.security.jwt.JwtTokenProvider;
import com.example.application.views.auth.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(@Qualifier("UserDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = new JwtTokenProvider(this.userDetailsService);
    }

    @Bean
    public JwtTokenProvider jwtTokenProviderBean() {
        return this.jwtTokenProvider;
    }

    @Configuration
    @Order(2)
    public class SecurityConfiguration extends VaadinWebSecurityConfigurerAdapter {

        @Value("${params.LOGIN_URL}")
        public String LOGOUT_URL;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            setLoginView(http, LoginView.class, LOGOUT_URL);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
            web.ignoring().antMatchers(
              "/images/*.png"
            );

        }

//        @Override   //FormLogin
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//              .csrf().disable()
//              .authorizeRequests()
//              .antMatchers("/").permitAll()
//              .anyRequest()
//              .authenticated()
//              .and()
//              .formLogin()
//              .loginPage("/auth/login").permitAll()
//              .defaultSuccessUrl("/auth/success")
//              .and()
//              .logout()
//              .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"))
//              .invalidateHttpSession(true)
//              .clearAuthentication(true)
//              .deleteCookies("JSESSIONID")
//              .logoutSuccessUrl("/auth/login");
//        }
    }

    @Configuration
    @Order(1)
    public class JWTSecurityConfig extends WebSecurityConfigurerAdapter {
        @Override   //REST
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/api/**")
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/auth/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .apply(new JwtConfigurer(jwtTokenProvider));
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

}
