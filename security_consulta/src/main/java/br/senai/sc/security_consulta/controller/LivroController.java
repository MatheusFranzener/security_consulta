package br.senai.sc.security_consulta.controller;

import br.senai.sc.security_consulta.model.entities.Autor;
import br.senai.sc.security_consulta.model.entities.Livro;
import br.senai.sc.security_consulta.service.LivroService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RequestMapping("/editora-livros/livro")
@RestController
public class LivroController {

    private LivroService livroService;

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Object> findById(@PathVariable(value = "isbn") Long isbn) {
        Optional<Livro> livroOptional =  livroService.findById(isbn);

        if (livroOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O livro de ISBN " + isbn + " não foi encontrado.");
        }

        System.out.println(livroOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(livroOptional.get());
    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<Livro>> findByAutor(@PathVariable(value = "autor") Autor autor) {
        return ResponseEntity.status(HttpStatus.FOUND).body(livroService.findByAutor(autor));
    }

    @GetMapping
    public ResponseEntity<List<Livro>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.findAll());
    }

//    @PostMapping
//    public ResponseEntity<Object> save(@RequestParam("livro") String livroJson) {
//        if (livroService.existsById(livro.getIsbn())){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(
//                    "Há um livro com o ISBN " + livro.getIsbn() + " cadastrado.");
//        }
//        System.out.println(files);
//        livro.setArquivos(livroUtil.convertMultiPartFilesToArquivos(files));
//        livro.setStatus(Status.AGUARDANDO_REVISAO);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                livroService.save(livro));
//    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Object> deleteById(@PathVariable(value = "isbn") Long isbn) {
        if (livroService.existsById(isbn)) {
            livroService.deleteById(isbn);
            return ResponseEntity.status(HttpStatus.OK).body("Livro deletado!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");
    }

//    @PutMapping("/{isbn}")
//    public ResponseEntity<Object> update(@PathVariable(value = "isbn") Long isbn, @RequestParam("livro") String livroJson) {
//        LivroUtil util = new LivroUtil();
//        Livro livro = util.convertJsonToModel(livroJson);
//        if (livroService.existsById(isbn)){
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(
//                    "Há um livro com o ISBN " + livro.getIsbn() + " cadastrado.");
//        }
//
//        livro.setArquivos(livroUtil.convertMultiPartFilesToArquivos(files));
//        return ResponseEntity.status(HttpStatus.OK).body(
//                livroService.save(livro));
//    }

}
