package dao;

import model.Conta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContaDAO {

    public void inserir(Conta conta) throws SQLException {
        String sql = "INSERT INTO contas (numero, titular, saldo) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, conta.getNumero());
            stmt.setString(2, conta.getTitular());
            stmt.setDouble(3, conta.getSaldo());
            stmt.executeUpdate();
        }
    }

    public List<Conta> listar() throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas ORDER BY numero";
        List<Conta> contas = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                contas.add(new Conta(rs.getInt("numero"),
                        rs.getString("titular"), rs.getDouble("saldo")));
            }
        }
        return contas;
    }

    public Conta buscarPorNumero(int numero) throws SQLException {
        try (Connection conn = Conexao.getConnection()) {
            return buscarPorNumero(conn, numero);
        }
    }

    public Conta buscarPorNumero(Connection conn, int numero) throws SQLException {
        String sql = "SELECT numero, titular, saldo FROM contas WHERE numero = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numero);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Conta(rs.getInt("numero"),
                            rs.getString("titular"), rs.getDouble("saldo"));
                }
            }
        }
        return null;
    }
}
