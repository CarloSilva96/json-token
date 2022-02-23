package com.web.jsontoken.model.DTO;

import com.web.jsontoken.model.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String senha;

    public Usuario dtoParaUsuario(Usuario usuario) {
        if (nome != null) {
            usuario.setNome(nome);
        }
        if (email != null) {
            usuario.setEmail(email);
        }
        if (senha != null) {
            usuario.setSenha(senha);
        }
        return usuario;
    }
}
