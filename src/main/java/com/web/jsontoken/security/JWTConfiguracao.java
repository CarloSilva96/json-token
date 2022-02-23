package com.web.jsontoken.security;

import com.web.jsontoken.services.DetalheUsuarioServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class JWTConfiguracao extends WebSecurityConfigurerAdapter {

    @Autowired
    private DetalheUsuarioServiceImplement detalheUsuarioServiceImplement;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** CONFIGURACAO DE SERVIÇO **/

    /** CRIANDO METODO PARA INFORMAR AO SPRING SECURTIRY QUE DEVE USAR O SERVIÇO DE USUARIO E PASSWORDENCODER PARA VALIDAR SENHAR **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /** INFORMANDO AO SPRING SECURITY PARA USAR AS MINHAS CLASSES COMO CALSSES BASES DE IMPLEMENTACAO PARA O TOKEN E AUTENTICAR **/
        auth.userDetailsService(detalheUsuarioServiceImplement).passwordEncoder(passwordEncoder);
    }

    /** CONFIGURACAO DE SERVIÇO **/

    /** CONFIGURAR COMO O SPRING SECURITY DEVE ENTENDER A MINHA PÁGINA **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                /** INFORMANDO COMO DESEJO QUE SEJA FEITO AS AUTORIZACOES DAS REQUISICOES **/
                .authorizeRequests()
                /** PERMITINDO QUE SEJA POSSIVEL FAZER LOGIN SEM ESTÁ AUTENTICADO -- /login URL DE LOGIN PADRAO DO SPRING SECURITY **/
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                /** PARA QUALQUER OUTRA SOLICITACAO É NECESSÁRIO ESTÁ AUTENTICADO **/
                .anyRequest().authenticated()
                .and()
                /** AQUI ADICIONO OS FILTROS. UM VAI ADICIONAR AUTENTICACAO E O OUTRO A VALIDACAO **/
                .addFilter(new JWTAutenticarFilter(authenticationManager()))
                .addFilter(new JWTValidarFilter(authenticationManager()))
                /** não salvando a sessão do usuario no servidor **/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    /** CONFIGURAR COMO O SPRING SECURITY DEVE ENTENDER A MINHA PÁGINA **/

    @Bean
    CorsConfigurationSource corsConfiguration() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
