package com.web.jsontoken.repository;

import com.web.jsontoken.model.DTO.UsuarioCreateDTO;
import com.web.jsontoken.model.DTO.UsuarioRespostaDTO;
import com.web.jsontoken.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT new com.web.jsontoken.model.DTO.UsuarioRespostaDTO(u) FROM Usuario u")
    List<UsuarioRespostaDTO> findAllUsuariosSDTO();
}
