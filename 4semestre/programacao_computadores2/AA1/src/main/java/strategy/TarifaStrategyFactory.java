package strategy;

import model.TipoTarifa;

public final class TarifaStrategyFactory {
    private TarifaStrategyFactory() {}

    public static TarifaTransferenciaStrategy criar(TipoTarifa tipo) {
        return switch (tipo) {
            case FIXA -> new TarifaFixaStrategy();
            case PERCENTUAL -> new TarifaPercentualStrategy();
            case ISENTA -> new TarifaIsentaStrategy();
        };
    }
}
