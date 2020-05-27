package br.com.gouvea.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Produto {

	private String nome;

	private String descricao;

	private Integer quantidade;
	
}
