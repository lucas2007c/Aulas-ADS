package model;

public enum TipoAcesso {
    ADMINISTRADOR("Administrador"),
    FUNCIONARIO("Funcionário");

    private final String descricao;

    TipoAcesso(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static TipoAcesso fromString(String text) {
        if (text == null) return null;
        for (TipoAcesso tipo : TipoAcesso.values()) {
            if (tipo.name().equalsIgnoreCase(text.trim()) || tipo.descricao.equalsIgnoreCase(text.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de acesso desconhecido: " + text);
    }
}
