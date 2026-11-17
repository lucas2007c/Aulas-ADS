package model;

import exception.ValidacaoException;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Veiculo {

    private Integer id;
    private String marca;
    private String modelo;
    private int anoModelo;
    private String cor;
    private String placa;
    private int quilometragem;
    private TipoVeiculo tipoVeiculo;
    private double precoAquisicao;
    private LocalDate dataEntrada;
    private StatusVeiculo status;

    public Veiculo() {
        this.status = StatusVeiculo.DISPONIVEL;
        this.dataEntrada = LocalDate.now();
    }

    public Veiculo(String marca, String modelo, int anoModelo, String cor, String placa,
                   int quilometragem, TipoVeiculo tipoVeiculo, double precoAquisicao,
                   LocalDate dataEntrada, StatusVeiculo status) {
        this.marca = marca;
        this.modelo = modelo;
        this.anoModelo = anoModelo;
        this.cor = cor;
        this.placa = placa;
        this.quilometragem = quilometragem;
        this.tipoVeiculo = tipoVeiculo;
        this.precoAquisicao = precoAquisicao;
        this.dataEntrada = dataEntrada != null ? dataEntrada : LocalDate.now();
        this.status = status != null ? status : StatusVeiculo.DISPONIVEL;
    }

    public Veiculo(Integer id, String marca, String modelo, int anoModelo, String cor, String placa,
                   int quilometragem, TipoVeiculo tipoVeiculo, double precoAquisicao,
                   LocalDate dataEntrada, StatusVeiculo status) {
        this(marca, modelo, anoModelo, cor, placa, quilometragem, tipoVeiculo, precoAquisicao, dataEntrada, status);
        this.id = id;
    }

    public abstract String getEspecificacaoDetalhada();

    public abstract void validarEspecificidades() throws ValidacaoException;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(int anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = (placa != null) ? placa.trim().toUpperCase() : null;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public TipoVeiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    protected void setTipoVeiculo(TipoVeiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public double getPrecoAquisicao() {
        return precoAquisicao;
    }

    public void setPrecoAquisicao(double precoAquisicao) {
        this.precoAquisicao = precoAquisicao;
    }

    public LocalDate getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDate dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id) || (placa != null && placa.equalsIgnoreCase(veiculo.placa));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, placa != null ? placa.toUpperCase() : 0);
    }

    @Override
    public String toString() {
        return tipoVeiculo + ": " + marca + " " + modelo + " (" + anoModelo + ") - Placa: " + placa + 
               " - " + getEspecificacaoDetalhada() + " - Status: " + status;
    }
}
