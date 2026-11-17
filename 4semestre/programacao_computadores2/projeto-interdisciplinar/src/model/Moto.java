package model;

import exception.ValidacaoException;
import java.time.LocalDate;

public class Moto extends Veiculo {

    private int cilindradas;

    public Moto() {
        super();
        setTipoVeiculo(TipoVeiculo.MOTO);
    }

    public Moto(String marca, String modelo, int anoModelo, String cor, String placa,
                int quilometragem, double precoAquisicao, LocalDate dataEntrada,
                StatusVeiculo status, int cilindradas) {
        super(marca, modelo, anoModelo, cor, placa, quilometragem, TipoVeiculo.MOTO,
              precoAquisicao, dataEntrada, status);
        this.cilindradas = cilindradas;
    }

    public Moto(Integer id, String marca, String modelo, int anoModelo, String cor, String placa,
                int quilometragem, double precoAquisicao, LocalDate dataEntrada,
                StatusVeiculo status, int cilindradas) {
        super(id, marca, modelo, anoModelo, cor, placa, quilometragem, TipoVeiculo.MOTO,
              precoAquisicao, dataEntrada, status);
        this.cilindradas = cilindradas;
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }

    @Override
    public String getEspecificacaoDetalhada() {
        return cilindradas + " cc";
    }

    @Override
    public void validarEspecificidades() throws ValidacaoException {
        if (cilindradas <= 0) {
            throw new ValidacaoException("As cilindradas de uma moto devem ser maiores que zero.");
        }
    }
}
