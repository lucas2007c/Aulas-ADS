package strategy;

public class TarifaIsentaStrategy implements TarifaTransferenciaStrategy {
    @Override
    public double calcularTarifa(double valor) {
        return 0.00;
    }
}
