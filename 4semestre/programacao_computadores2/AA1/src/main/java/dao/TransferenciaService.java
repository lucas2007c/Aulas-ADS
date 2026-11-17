package dao;

import exception.SaldoInsuficienteException;
import model.Transferencia;
import strategy.TarifaTransferenciaStrategy;

import java.sql.*;
import java.time.LocalDateTime;

public class TransferenciaService {

    public Transferencia transferir(int origem, int destino, double valor,
                                    TarifaTransferenciaStrategy strategy)
            throws SQLException, SaldoInsuficienteException {

        if (strategy == null)
            throw new IllegalArgumentException("Selecione uma modalidade de tarifa.");
        if (origem == destino)
            throw new IllegalArgumentException(
                "A conta de origem e a conta de destino devem ser diferentes.");
        if (valor <= 0)
            throw new IllegalArgumentException(
                "O valor da transferência deve ser positivo.");

        double tarifa = strategy.calcularTarifa(valor);
        double total = valor + tarifa;

        String debito = "UPDATE contas SET saldo = saldo - ? " +
                        "WHERE numero = ? AND saldo >= ?";
        String credito = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";

        try (Connection conn = Conexao.getConnection()) {
            conn.setAutoCommit(false);

            try {
                ContaDAO contaDAO = new ContaDAO();

                if (contaDAO.buscarPorNumero(conn, origem) == null)
                    throw new SQLException("Conta de origem não existe.");

                if (contaDAO.buscarPorNumero(conn, destino) == null)
                    throw new SQLException("Conta de destino não existe.");

                try (PreparedStatement stmt = conn.prepareStatement(debito)) {
                    stmt.setDouble(1, total);
                    stmt.setInt(2, origem);
                    stmt.setDouble(3, total);

                    if (stmt.executeUpdate() == 0)
                        throw new SaldoInsuficienteException(
                            String.format("Saldo insuficiente. Necessário: R$ %.2f.", total));
                }

                try (PreparedStatement stmt = conn.prepareStatement(credito)) {
                    stmt.setDouble(1, valor);
                    stmt.setInt(2, destino);

                    if (stmt.executeUpdate() == 0)
                        throw new SQLException(
                            "Não foi possível creditar a conta de destino.");
                }

                Transferencia t = new Transferencia(
                    0, origem, destino, valor, tarifa,
                    strategy.getClass().getSimpleName(),
                    LocalDateTime.now());

                new TransferenciaDAO().inserir(conn, t);

                conn.commit();
                return t;

            } catch (SQLException | SaldoInsuficienteException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
