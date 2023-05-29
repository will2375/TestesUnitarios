package services;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculadoraTeste {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        //cenario
        int a = 5;
        int b = 3;
        calc = new Calculadora();

        //acao
        int resultado = calc.somar(a, b);

        //verificacao
        assertEquals(8, resultado);
    }

    @Test
    public void deveSubitrairDoisValores() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calc.subtrair(a, b);

        //verificacao
        assertEquals(2, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 6;
        int b = 3;

        //acao
        int resultado = calc.dividir(a, b);

        //verificacao
        assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 6;
        int b = 0;

        //acao
        int resultado = calc.dividir(a, b);

        //verificacao
    }
}
