package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ContaCorrente;

public class ContaDAO {

    public void inserir(ContaCorrente conta) throws SQLException {
        String sql = "INSERT INTO contas (numero, titular , saldo) VALUES (?, ?, ?)";

        Conexao conexao = new Conexao();

        try (Connection conn = conexao.getConnection();
                PreparedStatement comando = conn.prepareStatement(sql)) {

            comando.setInt(1, conta.getNumero());
            comando.setString(2, conta.getTitular());
            comando.setDouble(3, conta.getSaldo());

            comando.executeUpdate();
        }
    } 
   
   public List<ContaCorrente> listar() throws SQLException {
        String sql = "SELECT * FROM contas";

        List<ContaCorrente> contas = new ArrayList<>();

        Conexao conexao = new Conexao();

        try (Connection conn = conexao.getConnection();
                PreparedStatement comando = conn.prepareStatement(sql); ResultSet resultado = comando.executeQuery()) {

            while (resultado.next()) {
                int numero = resultado.getInt("numero");
                String titular = resultado.getString("titular");
                double saldo = resultado.getDouble("saldo");

                ContaCorrente conta = new ContaCorrente(
                        numero,
                        titular,
                        saldo
                );

                contas.add(conta);
            }
        }

        return contas;
    }

    public ContaCorrente buscaPorNumero(int numero) throws SQLException {
        String sql = "SELECT * FROM contas WHERE numero = ?";

        Conexao conexao = new Conexao();

        try (Connection conn = conexao.getConnection(); 
                PreparedStatement comando = conn.prepareStatement(sql)) {

            comando.setInt(1, numero);

            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                int numeroConta = resultado.getInt("numero");
                String titular = resultado.getString("titular");
                double saldo = resultado.getDouble("saldo");

                return new ContaCorrente(numeroConta, titular, saldo);
            }
        }

        return null;
    }

    public void atualizarSaldo(int numero, double novoSaldo) throws SQLException {
        String sql = "UPDATE contas SET saldo = ? WHERE numero = ?";

        try (Connection conn = new Conexao().getConnection();
                PreparedStatement comando = conn.prepareStatement(sql)) {

            comando.setDouble(1, novoSaldo);
            comando.setInt(2, numero);

            comando.executeUpdate();
        }
    }

    public boolean remover(int numero) throws SQLException {
        String sql = "DELETE FROM contas WHERE numero = ?";

        try (Connection conn = new Conexao().getConnection();
                PreparedStatement comando = conn.prepareStatement(sql)) {

            comando.setInt(1, numero);

            int linhasAfetadas = comando.executeUpdate();

            return linhasAfetadas > 0;
        }
    }
    
    public boolean transferir(int numeroOrigem, int numeroDestino, double valor) throws SQLException {
        String sqlSaque = "UPDATE contas SET saldo = saldo - ? WHERE numero = ? AND saldo >= ?";
        String sqlDeposito = "UPDATE contas SET saldo = saldo + ? WHERE numero = ?";

        try (Connection conn = new Conexao().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtSaque = conn.prepareStatement(sqlSaque);
                 PreparedStatement stmtDeposito = conn.prepareStatement(sqlDeposito)) {

                stmtSaque.setDouble(1, valor);
                stmtSaque.setInt(2, numeroOrigem);
                stmtSaque.setDouble(3, valor); //
                
                int linhasSaque = stmtSaque.executeUpdate();

                if (linhasSaque == 0) {
                    conn.rollback();
                    return false;
                }
                
                stmtDeposito.setDouble(1, valor);
                stmtDeposito.setInt(2, numeroDestino);
                
                int linhasDeposito = stmtDeposito.executeUpdate();

                if (linhasDeposito == 0) {
                    conn.rollback();
                    return false;
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                throw e; 
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
