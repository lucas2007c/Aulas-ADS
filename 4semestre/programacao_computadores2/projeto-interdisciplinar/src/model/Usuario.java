package model;

import java.util.Objects;

public class Usuario {

    private Integer id;
    private String nome;
    private String usuario;
    private String senha;
    private TipoAcesso tipoAcesso;

    public Usuario() {
    }

    public Usuario(String nome, String usuario, String senha, TipoAcesso tipoAcesso) {
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.tipoAcesso = tipoAcesso;
    }

    public Usuario(Integer id, String nome, String usuario, String senha, TipoAcesso tipoAcesso) {
        this.id = id;
        this.nome = nome;
        this.usuario = usuario;
        this.senha = senha;
        this.tipoAcesso = tipoAcesso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoAcesso getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(TipoAcesso tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }

    public boolean isAdministrador() {
        return TipoAcesso.ADMINISTRADOR.equals(this.tipoAcesso);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario1 = (Usuario) o;
        return Objects.equals(id, usuario1.id) || Objects.equals(usuario, usuario1.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario);
    }

    @Override
    public String toString() {
        return nome + " (" + usuario + ") - " + (tipoAcesso != null ? tipoAcesso.getDescricao() : "Indefinido");
    }
}
