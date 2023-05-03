package br.senai.sc.security_consulta.service;

import br.senai.sc.security_consulta.model.entities.Autor;
import br.senai.sc.security_consulta.model.entities.Livro;
import br.senai.sc.security_consulta.repository.LivroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {
    private LivroRepository livroRepository;

    public <S extends Livro> S save(S entity) {
        return livroRepository.save(entity);
    }

    @Transactional
    public Optional<Livro> findById(Long isbn){
        return livroRepository.findById(isbn);
    }

    public List<Livro> findByAutor(Autor autor){
        return livroRepository.findByAutor(autor);
    }

    public List<Livro> findAll(){
        return livroRepository.findAll();
    }

    public void deleteById(Long isbn){
        livroRepository.deleteById(isbn);
    }

    public boolean existsById(Long isbn) {
        return livroRepository.existsById(isbn);
    }

}
