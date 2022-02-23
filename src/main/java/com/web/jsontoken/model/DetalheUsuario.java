package com.web.jsontoken.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/** Classe base que implementa os detalhes do usuário do SPRING-SECURITY **/
public class DetalheUsuario implements UserDetails {

    private final Optional<Usuario> usuario;

    /** RECEBENDO USUÁRIO MODEL **/
    public DetalheUsuario(Optional<Usuario> usuario) {
        this.usuario = usuario;
    }


    /** RETORNA AS PERMISSÕES DOS USUÁRIOS **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    /** RETORNA SENHA DO USUÁRIO **/
    @Override
    public String getPassword() {
        return usuario.orElse(new Usuario()).getSenha();
    }

    /** RETORNA LOGIN DO USUÁRIO **/
    @Override
    public String getUsername() {
        return usuario.orElse(new Usuario()).getEmail();
    }

    /** RETORNA SE A CONTA NÃO ESTÁ EXPIRADA **/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** RETORNA SE A CONTA NÃO ESTÁ BLOQUEADA **/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** RETORNA SE AS CREDENCIAIS NÃO ESTÃO EXPIRADAS **/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** RETORNA SE O USUÁRIO ESTÁ ATIVO **/
    @Override
    public boolean isEnabled() {
        return true;
    }
}
