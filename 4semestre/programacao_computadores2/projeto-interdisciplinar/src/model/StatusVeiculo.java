package model;

public enum StatusVeiculo {
    DISPONIVEL("Disponível"),
    VENDIDO("Vendido");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static StatusVeiculo fromString(String text) {
        if (text == null) return null;
        for (StatusVeiculo status : StatusVeiculo.values()) {
            if (status.name().equalsIgnoreCase(text.trim()) || status.descricao.equalsIgnoreCase(text.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + text);
    }
}
