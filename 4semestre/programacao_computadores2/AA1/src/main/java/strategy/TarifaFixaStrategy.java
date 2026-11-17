package strategy;

public class TarifaFixaStrategy implements TarifaTransferenciaStrategy {
    @Override
    public double calcularTarifa(double valor) {
        return 5.00;
    }
}
