package org.sber.resourcereservation.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

    //TODO
/*    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/", "/home", "/signup").permitAll()
                                .requestMatchers("/user/{id}").authenticated()
                                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                                .anyRequest().denyAll()
                )
                .formLogin(form ->
                        form
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                )
                .httpBasic(Customizer.withDefaults())
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/")
                )
                .rememberMe(rememberMe ->
                        rememberMe
                                .rememberMeServices(rememberMeServices)
                                .tokenValiditySeconds(86400)
                )
        ;

        return http.build();
    }*/

/*    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }*/

/*    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public ProviderManager authenticationManager(RememberMeAuthenticationProvider rememberMeAuthenticationProvider, DaoAuthenticationProvider daoAuthenticationProvider) {
        return new ProviderManager(
                rememberMeAuthenticationProvider,
                daoAuthenticationProvider
        );
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("key", userDetailsService);
        services.setAlwaysRemember(true);
        return services;
    }

    @Bean
    public RememberMeAuthenticationFilter rememberMeFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("key");
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(authenticationManager);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }*/
}
