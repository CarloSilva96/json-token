package com.web.jsontoken.controller;

import com.web.jsontoken.model.DTO.UsuarioCreateDTO;
import com.web.jsontoken.model.DTO.UsuarioRespostaDTO;
import com.web.jsontoken.model.DTO.UsuarioUpdateDTO;
import com.web.jsontoken.model.Usuario;
import com.web.jsontoken.repository.UsuarioRepository;
import com.web.jsontoken.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioRespostaDTO>> listarUsuarios() {
        List<UsuarioRespostaDTO> usuarios = usuarioService.findAllUsuarios();
        return ResponseEntity.ok().body(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioRespostaDTO> criarUsuario(@RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioRespostaDTO usuarioRespostaDTO = usuarioService.insertUsuario(usuarioCreateDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                                                .buildAndExpand(usuarioRespostaDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(usuarioRespostaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizarUsuario(@RequestBody UsuarioUpdateDTO usuarioUpdateDTO,
                                                               @PathVariable(value = "id") Integer id) {
        UsuarioRespostaDTO usuarioRespostaDTO = usuarioService.updateUsuario(usuarioUpdateDTO, id);
        return ResponseEntity.ok().body(usuarioRespostaDTO);
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> validarUsuario(@RequestParam String email, @RequestParam String senha) {
        Boolean usuarioValidado = usuarioService.validarUsuario(email, senha);
        HttpStatus status = usuarioValidado ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(usuarioValidado);
    }
}
