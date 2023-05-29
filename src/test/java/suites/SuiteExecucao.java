package suites;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import services.CalculadoraTeste;
import services.CalcularValorLocacaoTeste;
import services.LocacaoServiceTeste;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculadoraTeste.class,
        CalcularValorLocacaoTeste.class,
        LocacaoServiceTeste.class
})
public class SuiteExecucao {
}
