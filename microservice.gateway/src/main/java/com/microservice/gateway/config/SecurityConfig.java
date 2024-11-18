package com.microservice.gateway.config;

import com.microservice.gateway.security.AuthotityConstant;
import com.microservice.gateway.security.jwt.JwtFilter;
import com.microservice.gateway.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    public SecurityConfig( TokenProvider tokenProvider ) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain( final HttpSecurity http ) throws Exception {
        http
            .csrf( AbstractHttpConfigurer::disable );
        http
            .sessionManagement( s -> s.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );
        http
            .securityMatcher("/api/**" )
            .authorizeHttpRequests( authz -> authz
                    .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/usuarios").permitAll()


                    //USUARIOS Y CUENTAS_MP

                    .requestMatchers(HttpMethod.GET, "/api/usuarios/monopatines-cercanos").authenticated()

                    .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyAuthority(
                            AuthotityConstant._ADMIN)

                    .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN)

                    .requestMatchers(HttpMethod.PATCH, "/api/mercadopago/anular/{id}").hasAnyAuthority(
                            AuthotityConstant._ADMIN)

                    .requestMatchers(HttpMethod.GET, "/api/mercadopago").hasAnyAuthority(
                            AuthotityConstant._ADMIN)

                    .requestMatchers(HttpMethod.GET, "/api/mercadopago/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN)


                    //MANTENIMIENTO
                    .requestMatchers("/api/mantenimientos/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN, AuthotityConstant._MANTENIMIENTO)

                    //TARIFAS
                    .requestMatchers(HttpMethod.POST, "/api/tarifas").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.POST, "/api/tarifas/ajustar-precios").hasAnyAuthority(
                            AuthotityConstant._ADMIN)

                    //VIAJES

                    .requestMatchers(HttpMethod.GET, "/api/viajes/monopatines/anio/{anio}/cantidad/{cantidad}").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/viajes").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/viajes/{id}").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.GET, "/api/viajes/total-facturado").hasAnyAuthority(
                            AuthotityConstant._ADMIN)


                    //MONOPATINES Y PARADAS
                    .requestMatchers(HttpMethod.PUT, "/api/monopatin/{id}/mover/posX/{posX}/posY/{posY}").authenticated()

                    .requestMatchers(HttpMethod.GET, "/api/monopatines/cantidad-operacion-mantenimiento").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.POST, "/api/monopatines").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.PUT, "/api/monopatines/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.DELETE, "/api/monopatines/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN)

                    .requestMatchers(HttpMethod.POST, "/api/paradas").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.PUT, "/api/paradas/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    .requestMatchers(HttpMethod.DELETE, "/api/paradas/**").hasAnyAuthority(
                            AuthotityConstant._ADMIN)
                    //FIN MONOPATINES Y PARADAS
                    .anyRequest().authenticated()
            )
            .httpBasic( Customizer.withDefaults() )
            .addFilterBefore( new JwtFilter( this.tokenProvider ), UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

}
