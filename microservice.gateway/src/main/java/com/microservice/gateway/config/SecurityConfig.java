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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Deshabilitar sesiones, usar solo JWT
                .authorizeRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()  // Ruta pública para login

                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()   // Ruta pública para crear usuarios

                        .requestMatchers(HttpMethod.POST, "/api/auth/usuarios").permitAll() // Ruta pública para acceder a controller de auth


                        .requestMatchers(HttpMethod.GET, "/api/auth/usuarios").permitAll() //METODO DE PRUEBA
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/prueba").permitAll() // METODO DE PRUEBA EN USUARIOS



                        // Rutas protegidas para usuarios y cuentas
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/monopatines-cercanos").authenticated() // Requiere autenticación
                        .requestMatchers(HttpMethod.GET, "/api/usuarios").hasAnyAuthority(AuthotityConstant._ADMIN) // Solo ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority(AuthotityConstant._ADMIN) // Solo ADMIN

                        // Rutas protegidas para MercadoPago
                        .requestMatchers(HttpMethod.PATCH, "/api/mercadopago/anular/{id}").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/mercadopago").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/mercadopago/**").hasAnyAuthority(AuthotityConstant._ADMIN)

                        // Rutas para mantenimiento
                        .requestMatchers("/api/mantenimientos/**").hasAnyAuthority(AuthotityConstant._ADMIN, AuthotityConstant._MANTENIMIENTO)

                        // Rutas protegidas para tarifas
                        .requestMatchers(HttpMethod.POST, "/api/tarifas").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/tarifas/ajustar-precios").hasAnyAuthority(AuthotityConstant._ADMIN)

                        // Rutas protegidas para viajes
                        .requestMatchers(HttpMethod.GET, "/api/viajes/monopatines/anio/{anio}/cantidad/{cantidad}").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/viajes").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/viajes/{id}").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/viajes/total-facturado").hasAnyAuthority(AuthotityConstant._ADMIN)

                        // Rutas para monopatines
                        .requestMatchers(HttpMethod.PUT, "/api/monopatin/{id}/mover/posX/{posX}/posY/{posY}").authenticated()  // Requiere autenticación
                        .requestMatchers(HttpMethod.GET, "/api/monopatines/cantidad-operacion-mantenimiento").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/monopatines").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/monopatines/**").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/monopatines/**").hasAnyAuthority(AuthotityConstant._ADMIN)

                        // Rutas para paradas
                        .requestMatchers(HttpMethod.POST, "/api/paradas").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/paradas/**").hasAnyAuthority(AuthotityConstant._ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/paradas/**").hasAnyAuthority(AuthotityConstant._ADMIN)

                        // Por defecto todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                //.httpBasic(Customizer.withDefaults()) // Configuración para autenticación básica (si es necesario)
                .addFilterBefore(new JwtFilter(this.tokenProvider), UsernamePasswordAuthenticationFilter.class); // Agregar el filtro JWT antes de la autenticación básica

        return http.build();
    }
}
