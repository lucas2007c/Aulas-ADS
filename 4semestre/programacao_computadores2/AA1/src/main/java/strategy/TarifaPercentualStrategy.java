package strategy;

public class TarifaPercentualStrategy implements TarifaTransferenciaStrategy {
    @Override
    public double calcularTarifa(double valor) {
        return valor * 0.01;
    }
}
