package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario maria;
	private Usuario jose;

	@Before
	public void setUp() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("Joao");
		this.maria = new Usuario("Maria");
		this.jose = new Usuario("Jose");
	}

	@Test(expected = RuntimeException.class)
	public void validarLeiloesSemNenhumLanceDado() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 novo")
				.construir();

		leiloeiro.avaliar(leilao);
	}

	@Test
	public void validarLancesEmOrdemCrescente() {
		// Parte 1: cenario
		Leilao leilao = new Leilao("Playstation 3 novo");

		leilao.propor(new Lance(joao, 250.0));
		leilao.propor(new Lance(maria, 300.0));
		leilao.propor(new Lance(jose, 400.0));

		// Parte 2: Acao
		leiloeiro.avaliar(leilao);

		// Parte 3: Validacao
		
		// Usando JUnit
		// double maiorEsperado = 400;
		// double menorEsperado = 250;
		// assertEquals(menorEsperado, leiloeiro.getMenorLance(), 0.00001);
		// assertEquals(maiorEsperado, leiloeiro.getMenorLance(), 0.00001);

		// Usando Hamcrest
		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));

	}

	@Test
	public void validarLeilaoComApenasUmLance() {

		Leilao leilao = new Leilao("Playstation 3 novo");

		leilao.propor(new Lance(joao, 1000.0));

		leiloeiro.avaliar(leilao);

		assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(1000.0, leiloeiro.getMenorLance(), 0.00001);

	}

	@Test
	public void encontrarTresMaioresLances() {

		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 100.0).lance(maria, 200.0).lance(joao, 300.0)
				.lance(maria, 400.0).construir();

		leiloeiro.avaliar(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();
		assertEquals(3, maiores.size());
//		Validando com JUnit
//		assertEquals(400.0, maiores.get(0).getValor(), 0.00001);
//		assertEquals(300.0, maiores.get(1).getValor(), 0.00001);
//		assertEquals(200.0, maiores.get(2).getValor(), 0.00001);
		
//		Validando com Hamcrest
		assertThat(maiores, hasItems(
				new Lance(maria, 400),
				new Lance(joao, 300),
				new Lance(maria, 200)
				));

	}

}
