package com.web.jsontoken.services;

import com.web.jsontoken.model.DTO.UsuarioCreateDTO;
import com.web.jsontoken.model.DTO.UsuarioRespostaDTO;
import com.web.jsontoken.model.DTO.UsuarioUpdateDTO;
import com.web.jsontoken.model.Usuario;
import com.web.jsontoken.repository.UsuarioRepository;
import com.web.jsontoken.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario findByUsuario(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElseThrow(() -> new ObjectNotFoundException(Usuario.class.getName() + " com o id: " + id + " não existe!"));
    }

     public Usuario findByUsuarioEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        return usuario.orElseThrow(() -> new ObjectNotFoundException(Usuario.class.getName() + " com o email: " + email + " não existe!"));
    }

    public List<UsuarioRespostaDTO> findAllUsuarios() {
        return usuarioRepository.findAllUsuariosSDTO();
    }

    @Transactional
    public UsuarioRespostaDTO insertUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        String senhaEncode = encodeSenha(usuarioCreateDTO.getSenha());
        Usuario usuario = usuarioCreateDTO.dtoParaUsuario(senhaEncode);
        usuario = usuarioRepository.save(usuario);
        return new UsuarioRespostaDTO(usuario);
    }

    @Transactional
    public UsuarioRespostaDTO updateUsuario(UsuarioUpdateDTO usuarioUpdateDTO, Integer id) {
        Usuario usuario = findByUsuario(id);
        if (usuarioUpdateDTO.getSenha() != null) {
            usuarioUpdateDTO.setSenha(encodeSenha(usuarioUpdateDTO.getSenha()));
        }
        usuario = usuarioUpdateDTO.dtoParaUsuario(usuario);
        usuario = usuarioRepository.save(usuario);
        return new UsuarioRespostaDTO(usuario);
    }

    private String encodeSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public Boolean validarUsuario(String email, String senha) {
        boolean usuarioValidado = false;
        Usuario usuario = findByUsuarioEmail(email);
        if (usuario != null) {
            usuarioValidado = passwordEncoder.matches(senha, usuario.getSenha());
        }
        return usuarioValidado;
    }
}
