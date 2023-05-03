package br.senai.sc.security_consulta.controller;


import br.senai.sc.security_consulta.model.dto.LivroDTO;
import br.senai.sc.security_consulta.model.dto.UsuarioDTO;
import br.senai.sc.security_consulta.model.entities.Livro;
import br.senai.sc.security_consulta.model.entities.Usuario;
import br.senai.sc.security_consulta.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/editora-livros/usuario")
@CrossOrigin
public class UsuarioController {

    private UsuarioService pessoaService;

    @GetMapping("/email/{email}")
    public ResponseEntity<Object> findByEmail(@PathVariable(value = "email") String email) {
        Optional<Usuario> pessoaOptional = pessoaService.findByEmail(email);

        if (pessoaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrada nenhuma pessoa com este E-mail");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pessoaOptional.get());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> findById(@PathVariable(value = "cpf") Long cpf) {
        Optional<Usuario> pessoaOptional = pessoaService.findById(cpf);

        return pessoaOptional.<ResponseEntity<Object>>map(pessoa -> ResponseEntity.status(HttpStatus.OK).body(pessoa)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrada nenhuma pessoa com este CPF"));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.findAll());
    }

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid UsuarioDTO pessoaDTO) {
        if (pessoaService.existsByEmail(pessoaDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este E-mail já está cadastrado.");
        }

        Usuario pessoa = new Usuario();
        BeanUtils.copyProperties(pessoaDTO, pessoa);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        pessoa.setSenha(encoder.encode(pessoa.getSenha()));

        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.save(pessoa));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "cpf") Long cpf) {
        if (!pessoaService.existsById(cpf)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi encontrada nenhuma pessoa com este CPF");
        }

        pessoaService.deleteById(cpf);

        return ResponseEntity.status(HttpStatus.OK).body("Pessoa deletada.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        if (!pessoaService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        Usuario usuario = pessoaService.findById(id).get();
        BeanUtils.copyProperties(usuarioDTO, usuario, "id");

        return ResponseEntity.status(HttpStatus.OK).body(pessoaService.save(usuario));
    }

}
