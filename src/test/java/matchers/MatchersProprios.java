package matchers;

import java.util.Calendar;

public class MatchersProprios {

    public static DiaSemanaMatchers cairEm(Integer diaSemana) {
        return new DiaSemanaMatchers(diaSemana);
    }

    public static DiaSemanaMatchers caiNumaSegunda(){
        return new DiaSemanaMatchers(Calendar.MONDAY);
    }

    public static DataDiferencaDiasMatcher ehHojeComDiferencaDias(Integer qtdDias) {
        return new DataDiferencaDiasMatcher(qtdDias);
    }

    public static DataDiferencaDiasMatcher ehHoje() {
        return new DataDiferencaDiasMatcher(0);
    }
}
