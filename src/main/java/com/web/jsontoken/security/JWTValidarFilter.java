package com.web.jsontoken.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTValidarFilter extends BasicAuthenticationFilter {


    public JWTValidarFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        /** BUSCANDO NO CABECALHO O ATRIBUTO Authorization **/
        String authorization = request.getHeader(JWTUtil.HEADER_AUTHORIZATION);
        /** SAI DO BLOCO CASO NAO ENCONTRE O Authorization TOKEN INVÁLIDO **/
        if (authorization == null) {
            chain.doFilter(request, response);
            return;
        }
        /** SAI DO BLOCO CASO NÃO TENHA O PREFIXO DO TIPO DO TOKEN Bearer TOKEN INVÁLIDO **/
        if (!authorization.startsWith(JWTUtil.BEARER)) {
            chain.doFilter(request, response);
            return;
        }
        /** REMOVENDO PREFIXO DO TOKEN **/
        String token = authorization.replace(JWTUtil.BEARER, "");
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token); /** RETORNANDO TOKEN **/
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    /** MÉTODO QUE CRIA O TOKEN E RETORNA OS DADOS DO USUÁRIO GARANTINDO QUE SEJA UM USUÁRIO VÁLIDO **/
    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        String usuario = JWT.require(Algorithm.HMAC512(JWTUtil.CHAVE_SECRETA))
                .build()
                .verify(token)
                .getSubject();
        /** SE USUÁRIO FOR NULO RETORNA NULO **/
        if (usuario == null) {
            return  null;
        }
        /** CASO TENHA O USUÁRIO RETORNA O TOKEN COM O USUÁRIO ENCONTRADO **/
        return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
    }
}
