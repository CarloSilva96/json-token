package com.web.jsontoken.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.jsontoken.model.DTO.UsuarioCreateDTO;
import com.web.jsontoken.model.DetalheUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;

public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAutenticarFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            /** RECEBENDO O JSON NO FORMATO USUARIO **/
            UsuarioCreateDTO usuario = new ObjectMapper() /** OBJETO DE MANIPULAMENTO DE JSON **/
                                    .readValue(request.getInputStream(), UsuarioCreateDTO.class);

            /** FAZENDO VALIDAÇÃO DO LOGIN DO USUARIO **/
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    usuario.getEmail(),
                    usuario.getSenha(),
                    new ArrayList<>()
            ));
        } catch (IOException e) {
            throw new RuntimeException("Falha ao autenticar usuário.");
        }
    }

    /** METODO RESPONSAVEL PARA AGIR CASO DÊ CERTO A AUTENTICAÇÃO **/
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        DetalheUsuario usuarioDados = (DetalheUsuario) authResult.getPrincipal();
        String email = usuarioDados.getUsername();

        /** GERAR TOKEN **/
        String token = JWT.create().withSubject(usuarioDados.getUsername()) /** NOME DO USUÁRIO **/
                                   .withExpiresAt( new Date(System.currentTimeMillis() + JWTUtil.TEMPO_EXPIRACAO)) /** TEMPO DE EXPIRAÇÃO DO TOKEN **/
                                   .sign(Algorithm.HMAC512(JWTUtil.CHAVE_SECRETA)); /** ASSINAR O TOKEN **/

        /** REGISTRANDO TOKEN NO CORPO DA PÁGINA **/
        response.addHeader(JWTUtil.HEADER_AUTHORIZATION, JWTUtil.BEARER + token);
    }
}
