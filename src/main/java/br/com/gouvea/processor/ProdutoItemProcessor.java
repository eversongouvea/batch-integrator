package br.com.gouvea.processor;

import org.springframework.batch.item.ItemProcessor;

import br.com.gouvea.model.Produto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProdutoItemProcessor implements ItemProcessor<Produto, Produto>{

	@Override
	public Produto process(Produto produto) throws Exception {
		
		final String nome = produto.getNome().toUpperCase();
        final String descricao = produto.getDescricao().toUpperCase();

        final Produto produtoProcessor = new Produto(nome, descricao,produto.getQuantidade());

        log.info("Convertendo {} -> {}",produto,produtoProcessor);

        return produtoProcessor;
		
	}

}
