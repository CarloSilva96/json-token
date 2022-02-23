package com.web.jsontoken.services;

import com.web.jsontoken.model.DetalheUsuario;
import com.web.jsontoken.model.Usuario;
import com.web.jsontoken.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetalheUsuarioServiceImplement implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /** MÉTODO QUE FAZ A CONSULTA DO USUÁRIO **/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        System.out.println(usuario);
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário [" + usuario + "] não existe.");
        }
        /** Atribuindo Usuario a classe UsuarioDetalhes que implementa UserDetails **/
        /** Com isso tenho acesso a todos os métodos getSenha, getUserName, getPermissoes e etc... **/
        return new DetalheUsuario(usuario);
    }
}
