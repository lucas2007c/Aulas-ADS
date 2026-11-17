package model;

public enum TipoVeiculo {
    CARRO("Carro"),
    MOTO("Moto");

    private final String descricao;

    TipoVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static TipoVeiculo fromString(String text) {
        if (text == null) return null;
        for (TipoVeiculo tipo : TipoVeiculo.values()) {
            if (tipo.name().equalsIgnoreCase(text.trim()) || tipo.descricao.equalsIgnoreCase(text.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de veículo desconhecido: " + text);
    }
}
