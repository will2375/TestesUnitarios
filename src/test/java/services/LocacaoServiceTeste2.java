package services;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.servicos.EmailServices;
import br.ce.wcaquino.servicos.LocacaoService2;
import br.ce.wcaquino.servicos.SpcService;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.verification.VerificationMode;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static builder.FilmeBuilder.umFilme;
import static builder.FilmeBuilder.umFilmeSemEstoque;
import static builder.LocacaoBuilder.umLocacao;
import static builder.UsuarioBuilder.umUsuario;
import static matchers.MatchersProprios.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService2.class})
public class LocacaoServiceTeste2 {

    @InjectMocks
    private LocacaoService2 service;

    @Mock
    private LocacaoDAO dao;
    @Mock
    private SpcService spcService;
    @Mock
    private EmailServices email;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        System.out.println("antes");
        MockitoAnnotations.initMocks(this);
        service = PowerMockito.spy(service);
    }

    @After
    public void tearDowns() {
        System.out.println("depois");
    }

    @Test
    public void teste() throws Exception {
        //cenario

        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(9, 6, 2023));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.YEAR, 2023);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        error.checkThat(locacao.getValor(), is(equalTo(5.0)));

        error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(9, 6, 2023)), is(true));
        error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(10, 6, 2023)), is(true));
    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

        //acao
        service.alugarFilme(usuario, filmes);
        System.out.println("forma elegante");
    }

    @Test
    public void testLocacao_UsuarioVazio() throws FilmeSemEstoqueException {
        //cenario
        List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

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
        Usuario usuario = umUsuario().agora();
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario, null);
        System.out.println("forma nova");
    }

    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), new Filme("Filme 2", 2, 4.0), new Filme("Filme 3", 2, 4.0));

        //acao
        Locacao resultado = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario
        Usuario usuario = umUsuario().agora();
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
        Usuario usuario = umUsuario().agora();
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
        Usuario usuario = umUsuario().agora();
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
    public void naoDevolverFilmeNoDomingo() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(new Filme("filme 1", 1, 5.0));

//        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(10, 6, 2023));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 10);
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.YEAR, 2023);
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);

        //acao
        Locacao retorno = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(retorno.getDataRetorno(), caiNumaSegunda());
//        PowerMockito.verifyNew(Date.class,Mockito.times(2)).withNoArguments();

        verifyStatic(Mockito.times(2));
        Calendar.getInstance();
    }

    private void verifyStatic(VerificationMode times) {
    }

    @Test
    public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("rob").agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(ArgumentMatchers.<Usuario>any(Usuario.class))).thenReturn(true);


        //acao
        try {
            service.alugarFilme(usuario, filmes);
            //verificacao
            fail();
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario Negativado"));
        }

        verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {
        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("usuario atrasado").agora();
        List<Locacao> locacaos = Arrays.asList(
                umLocacao().atrasado().comUsuario(usuario).agora(),
                umLocacao().comUsuario(usuario2).agora(),
                umLocacao().atrasado().comUsuario(usuario3).agora(),
                umLocacao().atrasado().comUsuario(usuario3).agora());
        when(dao.obterLocacoesPendentes()).thenReturn(locacaos);

        //acao
        service.notificarAtrasos();

        //verificacao
        verify(email, times(2)).notificarAtraso(ArgumentMatchers.<Usuario>any(Usuario.class));
        verify(email).notificarAtraso(usuario2);
        verify(email, times(2)).notificarAtraso(usuario3);
        verify(email, never()).notificarAtraso(usuario2);
        verifyNoMoreInteractions(email);
    }


    @Test
    public void deveTratarErroNoSpc() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("falha catrastrofica"));


        exception.expect(LocadoraException.class);
        exception.expectMessage("problemas com spc, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao(){
        //cenario
        Locacao locacao = umLocacao().agora();

        //acao
        service.prorrogarLocacao(locacao, 3);

        //verificacao
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetorno = argCapt.getValue();

        error.checkThat(locacaoRetorno.getValor(), is(12.0));
        error.checkThat(locacaoRetorno.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetorno.getDataRetorno(), ehHojeComDiferencaDias(3));
    }

    @org.junit.jupiter.api.Test
    public void deveAlugarFilme_SemCalcularValor() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes= Arrays.asList(umFilme().agora());

        PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(1.0));
    }

    @Test
    public void deveCalcularValorLocacao() throws Exception {
        //cenario
        List<Filme> filmes= Arrays.asList(umFilme().agora());

        //acao
        Double valor = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);

        //verificacao
        assertThat(valor, is(4.0));
    }

}
