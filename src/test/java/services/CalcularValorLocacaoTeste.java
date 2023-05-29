package services;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class CalcularValorLocacaoTeste {

    private LocacaoService service;

    @Parameterized.Parameter
    public List<Filme> filmes;

    @Parameterized.Parameter(value = 1)
    public Double valorLocacao;

    @Parameterized.Parameter(value = 2)
    public String cenario;

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    private static Filme filme1 = new Filme("filme 1", 2, 4.0);
    private static Filme filme2 = new Filme("filme 2", 2, 4.0);
    private static Filme filme3 = new Filme("filme 3", 2, 4.0);
    private static Filme filme4 = new Filme("filme 4", 2, 4.0);
    private static Filme filme5 = new Filme("filme 5", 2, 4.0);
    private static Filme filme6 = new Filme("filme 6", 2, 4.0);
    private static Filme filme7 = new Filme("filme 7", 2, 4.0);

    @Parameterized.Parameters(name = "{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(filme1, filme2), 8.0, "2 filmes: sem desconto"},
                {Arrays.asList(filme1, filme2, filme3), 11.0, "3 filmes: 25%"},
                {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 filmes: 50%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 filmes: 75%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 filmes: 100%"},
                {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 filmes: sem desconto"}
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("usuario1");


        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        Assert.assertThat(resultado.getValor(), is(valorLocacao));

    }

}
