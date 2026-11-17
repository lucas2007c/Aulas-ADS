package model;

import java.time.LocalDateTime;

public class Transferencia {
    private final int id;
    private final int contaOrigem;
    private final int contaDestino;
    private final double valor;
    private final double tarifa;
    private final String modalidadeTarifa;
    private final LocalDateTime dataHora;

    public Transferencia(int id, int contaOrigem, int contaDestino,
                         double valor, double tarifa,
                         String modalidadeTarifa, LocalDateTime dataHora) {
        this.id = id;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
        this.tarifa = tarifa;
        this.modalidadeTarifa = modalidadeTarifa;
        this.dataHora = dataHora;
    }

    public int getId() { return id; }
    public int getContaOrigem() { return contaOrigem; }
    public int getContaDestino() { return contaDestino; }
    public double getValor() { return valor; }
    public double getTarifa() { return tarifa; }
    public String getModalidadeTarifa() { return modalidadeTarifa; }
    public LocalDateTime getDataHora() { return dataHora; }
}
