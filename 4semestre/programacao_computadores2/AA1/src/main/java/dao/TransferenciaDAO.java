package dao;

import model.Transferencia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaDAO {

    public void inserir(Connection conn, Transferencia t) throws SQLException {
        String sql = "INSERT INTO transferencias " +
            "(conta_origem, conta_destino, valor, tarifa, modalidade_tarifa, data_hora) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getContaOrigem());
            stmt.setInt(2, t.getContaDestino());
            stmt.setDouble(3, t.getValor());
            stmt.setDouble(4, t.getTarifa());
            stmt.setString(5, t.getModalidadeTarifa());
            stmt.setTimestamp(6, Timestamp.valueOf(t.getDataHora()));
            stmt.executeUpdate();
        }
    }

    public List<Transferencia> listarPorConta(int numeroConta) throws SQLException {
        String sql = "SELECT id, conta_origem, conta_destino, valor, tarifa, " +
            "modalidade_tarifa, data_hora FROM transferencias " +
            "WHERE conta_origem = ? OR conta_destino = ? ORDER BY data_hora DESC";

        List<Transferencia> lista = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numeroConta);
            stmt.setInt(2, numeroConta);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Transferencia(
                        rs.getInt("id"),
                        rs.getInt("conta_origem"),
                        rs.getInt("conta_destino"),
                        rs.getDouble("valor"),
                        rs.getDouble("tarifa"),
                        rs.getString("modalidade_tarifa"),
                        rs.getTimestamp("data_hora").toLocalDateTime()));
                }
            }
        }
        return lista;
    }
}
