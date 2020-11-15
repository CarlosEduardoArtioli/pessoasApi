package br.com.pessoas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.pessoas.model.Pessoa;
import br.com.pessoas.repository.PessoaRepository;
import br.com.pessoas.repository.filter.PessoaFilter;
import br.com.pessoas.service.exceptions.ObjectNotFoundException;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Pessoa> listarPessoas() {
		return pessoaRepository.findAll();
	}

	public Page<Pessoa> pesquisar(PessoaFilter pessoaFilter, Pageable pageable) {
		return pessoaRepository.filtrar(pessoaFilter, pageable);
	}

	public Pessoa insert(Pessoa pessoa) {
		pessoa.getContatos().forEach(p -> p.setPessoa(pessoa));
		return pessoaRepository.save(pessoa);
	}

	public Pessoa update(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = find(codigo);

		pessoaSalva.getContatos().clear();
		pessoaSalva.getContatos().addAll(pessoa.getContatos());
		pessoaSalva.getContatos().forEach(p -> p.setPessoa(pessoaSalva));
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo", "contatos");
		return pessoaRepository.save(pessoaSalva);
	}

	public void delete(Long codigo) {
		find(codigo);
		pessoaRepository.deleteById(codigo);
	}

	public Pessoa find(Long codigo) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);

		return pessoa.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Codigo: " + codigo + " Tipo: " + Pessoa.class.getName()));
	}
}
