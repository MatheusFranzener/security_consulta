package br.senai.sc.security_consulta.service;

import br.senai.sc.security_consulta.model.entities.Usuario;
import br.senai.sc.security_consulta.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository pessoaRepository;

    public UsuarioService(UsuarioRepository pessoaRepository){
        this.pessoaRepository = pessoaRepository;
    }

    public boolean existsById(Long cpf) {
        return pessoaRepository.existsById(cpf);
    }

    public List<Usuario> findAll() {
        return pessoaRepository.findAll();
    }

    public <S extends Usuario> S save(S entity) {
        return pessoaRepository.save(entity);
    }

    public Optional<Usuario> findById(Long cpf) {
        return pessoaRepository.findById(cpf);
    }

    public void deleteById(Long cpf) {
        pessoaRepository.deleteById(cpf);
    }

    public Optional<Usuario> findByEmail(String email) {
        return pessoaRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return pessoaRepository.existsByEmail(email);
    }

}
