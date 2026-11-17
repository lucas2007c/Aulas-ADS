package model;

public enum TipoTarifa {
    FIXA("Tarifa Fixa"),
    PERCENTUAL("Tarifa Percentual"),
    ISENTA("Tarifa Isenta");

    private final String descricao;

    TipoTarifa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }
}
