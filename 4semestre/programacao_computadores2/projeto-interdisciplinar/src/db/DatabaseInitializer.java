package db;

import exception.DatabaseException;
import services.SecurityUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void inicializarBanco() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();

            String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL, " +
                    "usuario VARCHAR(50) NOT NULL UNIQUE, " +
                    "senha VARCHAR(64) NOT NULL, " +
                    "tipo_acesso VARCHAR(20) NOT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            stmt.executeUpdate(sqlUsuarios);

            String sqlVeiculos = "CREATE TABLE IF NOT EXISTS veiculos (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "marca VARCHAR(50) NOT NULL, " +
                    "modelo VARCHAR(50) NOT NULL, " +
                    "ano_modelo INT NOT NULL, " +
                    "cor VARCHAR(30) NOT NULL, " +
                    "placa VARCHAR(10) NOT NULL UNIQUE, " +
                    "quilometragem INT NOT NULL, " +
                    "tipo_veiculo VARCHAR(10) NOT NULL, " +
                    "preco_aquisicao DECIMAL(12, 2) NOT NULL, " +
                    "data_entrada DATE NOT NULL, " +
                    "status VARCHAR(20) NOT NULL, " +
                    "quantidade_portas INT NULL, " +
                    "cilindradas INT NULL" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            stmt.executeUpdate(sqlVeiculos);

            garantirAdministradorInicial(conn);

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao inicializar as tabelas do banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(stmt, conn);
        }
    }

    private static void garantirAdministradorInicial(Connection conn) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) FROM usuarios WHERE tipo_acesso = 'ADMINISTRADOR'";
        PreparedStatement psCheck = null;
        ResultSet rs = null;
        PreparedStatement psInsert = null;

        try {
            psCheck = conn.prepareStatement(sqlCheck);
            rs = psCheck.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                String hashSenhaPadrao = SecurityUtils.hashSenha("admin123");
                String sqlInsert = "INSERT INTO usuarios (nome, usuario, senha, tipo_acesso) VALUES (?, ?, ?, ?)";
                psInsert = conn.prepareStatement(sqlInsert);
                psInsert.setString(1, "Administrador Inicial");
                psInsert.setString(2, "admin");
                psInsert.setString(3, hashSenhaPadrao);
                psInsert.setString(4, "ADMINISTRADOR");
                psInsert.executeUpdate();
                System.out.println("Usuario administrador inicial criado: usuario='admin' / senha='admin123'");
            }
        } finally {
            DatabaseConnection.fecharRecursos(rs, psCheck, psInsert);
        }
    }
}
