package com.web.jsontoken.model.DTO;

import com.web.jsontoken.model.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String senha;

    public Usuario dtoParaUsuario(String senha) {
        return new Usuario(nome, email, senha);
    }
}
