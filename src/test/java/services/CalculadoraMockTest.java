package services;

import br.ce.wcaquino.servicos.Calculadora;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {
    @Mock
    private  Calculadora calcMock;

    @Spy
    private Calculadora calcSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void devoMostrarDiferencaEntreMockESpy(){
//        Mockito.when(calcMock.somar(1, 2)).thenCallRealMethod();
        Mockito.when(calcMock.somar(1, 2)).thenReturn(5);
//        Mockito.when(calcSpy.somar(1, 2)).thenReturn(8);
        Mockito.doReturn(5).when(calcSpy).somar(1,2);
        Mockito.doNothing().when(calcSpy).imprime();

        System.out.println("Mock: " + calcMock.somar(1, 2));
        System.out.println("Spy: " + calcSpy.somar(1, 2));

        System.out.println("mock");
        calcMock.imprime();
        System.out.println("spy");
        calcSpy.imprime();
    }
    @Test
    public void teste() {
        Calculadora calc = Mockito.mock(Calculadora.class);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);

        Assert.assertEquals(5, calc.somar(2431,56));

        System.out.println(argumentCaptor.getAllValues());
    }
}
