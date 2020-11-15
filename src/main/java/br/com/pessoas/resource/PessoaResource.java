package br.com.pessoas.resource;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.pessoas.model.Pessoa;
import br.com.pessoas.repository.filter.PessoaFilter;
import br.com.pessoas.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaService PessoaService;

	@GetMapping("/todos")
	public List<Pessoa> listarTodosPessoas() {
		return PessoaService.listarPessoas();
	}

	@GetMapping()
	public Page<Pessoa> pesquisar(PessoaFilter PessoaFilter, Pageable pageable) {
		return PessoaService.pesquisar(PessoaFilter, pageable);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> porCodigo(@PathVariable Long codigo) {
		Pessoa Pessoa = PessoaService.find(codigo);
		return ResponseEntity.ok().body(Pessoa);
	}

	@PostMapping()
	public ResponseEntity<Void> insert(@RequestBody Pessoa Pessoa) {
		Pessoa = PessoaService.insert(Pessoa);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{codigo}").buildAndExpand(Pessoa.getCodigo())
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> update(@PathVariable Long codigo, @RequestBody Pessoa Pessoa) {
		try {
			Pessoa PessoaSalvo = PessoaService.update(codigo, Pessoa);
			return ResponseEntity.ok(PessoaSalvo);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> delete(@PathVariable Long codigo) {
		PessoaService.delete(codigo);
		return ResponseEntity.noContent().build();
	}
}
