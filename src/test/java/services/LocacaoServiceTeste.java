package services;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;
import matchers.DiaSemanaMatchers;
import matchers.MatchersProprios;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.*;
import static matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class LocacaoServiceTeste {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        System.out.println("antes");
        service = new LocacaoService();
    }

    @After
    public void tearDowns() {
        System.out.println("depois");
    }

    @Test
    public void teste() throws Exception {
        //cenario

        Usuario usuario = new Usuario("usuario");
        List<Filme> filmes = Arrays.asList(new Filme("filme", 4, 3.5));

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(3.5)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), obterDataComDiferencaDias(1)), is(false));
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
        error.checkThat(locacao.getDataLocacao(), ehHoje());
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = new Usuario("usuario");
        List<Filme> filmes = Arrays.asList(new Filme("filme", 0, 3.5));

        //acao
        service.alugarFilme(usuario, filmes);
        System.out.println("forma elegante");
    }

    @Test
    public void testLocacao_UsuarioVazio() throws FilmeSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(new Filme("filme", 1, 3.5));

        //acao
        try {
            service.alugarFilme(null, filmes);
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
        System.out.println("forma robusta");
    }

    @Test
    public void testLocacao_filmeVaio() throws FilmeSemEstoqueException, LocadoraException {

        //cenario
        Usuario usuario = new Usuario("usuario");
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);
        System.out.println("forma nova");
    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(13.0));
    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),
                new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0),
                new Filme("Filme 4", 2, 4.0), new Filme("Filme 5", 2, 4.0),
                new Filme("Filme 6", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(14.0));
    }

    @Test
    public void naoDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        List<Filme> filmes = Arrays.asList(new Filme("filme 1", 1, 5.0));

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao

        //assertThat(retorno.getDataRetorno(), new DiaSemanaMatchers(Calendar.MONDAY));
        //assertThat(retorno.getDataRetorno(), MatchersProprios.cairEm(Calendar.MONDAY));
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
    }
}
