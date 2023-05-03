package br.senai.sc.security_consulta.repository;

import br.senai.sc.security_consulta.model.entities.Autor;
import br.senai.sc.security_consulta.model.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutor(Autor autor);

}
