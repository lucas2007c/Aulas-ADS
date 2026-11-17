package model;

import exception.ValidacaoException;
import java.time.LocalDate;

public class Carro extends Veiculo {

    private int quantidadePortas;

    public Carro() {
        super();
        setTipoVeiculo(TipoVeiculo.CARRO);
    }

    public Carro(String marca, String modelo, int anoModelo, String cor, String placa,
                 int quilometragem, double precoAquisicao, LocalDate dataEntrada,
                 StatusVeiculo status, int quantidadePortas) {
        super(marca, modelo, anoModelo, cor, placa, quilometragem, TipoVeiculo.CARRO,
              precoAquisicao, dataEntrada, status);
        this.quantidadePortas = quantidadePortas;
    }

    public Carro(Integer id, String marca, String modelo, int anoModelo, String cor, String placa,
                 int quilometragem, double precoAquisicao, LocalDate dataEntrada,
                 StatusVeiculo status, int quantidadePortas) {
        super(id, marca, modelo, anoModelo, cor, placa, quilometragem, TipoVeiculo.CARRO,
              precoAquisicao, dataEntrada, status);
        this.quantidadePortas = quantidadePortas;
    }

    public int getQuantidadePortas() {
        return quantidadePortas;
    }

    public void setQuantidadePortas(int quantidadePortas) {
        this.quantidadePortas = quantidadePortas;
    }

    @Override
    public String getEspecificacaoDetalhada() {
        return quantidadePortas + " portas";
    }

    @Override
    public void validarEspecificidades() throws ValidacaoException {
        if (quantidadePortas < 2 || quantidadePortas > 6) {
            throw new ValidacaoException("Quantidade de portas inválida para um carro (deve ser entre 2 e 6).");
        }
    }
}
