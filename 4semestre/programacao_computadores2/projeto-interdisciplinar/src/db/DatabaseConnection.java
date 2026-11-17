package db;

import exception.DatabaseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String url;
    private static String user;
    private static String password;

    static {
        carregarConfiguracoes();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL nao encontrado no classpath: " + e.getMessage());
        }
    }

    private DatabaseConnection() {
    }

    private static void carregarConfiguracoes() {
        Properties props = new Properties();
        File propFile = new File("db.properties");

        if (propFile.exists()) {
            try (InputStream input = new FileInputStream(propFile)) {
                props.load(input);
            } catch (Exception e) {
                System.err.println("Aviso: Falha ao ler db.properties do disco. Usando padroes. " + e.getMessage());
            }
        } else {
            try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (input != null) {
                    props.load(input);
                }
            } catch (Exception ignored) {
            }
        }

        url = props.getProperty("db.url", "jdbc:mysql://localhost:3306/concessionaria_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true");
        user = props.getProperty("db.user", "root");
        password = props.getProperty("db.password", "root");
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            if ("root".equals(password)) {
                try {
                    Connection conn = DriverManager.getConnection(url, user, "");
                    password = "";
                    return conn;
                } catch (SQLException ignored) {
                }
            }
            throw new DatabaseException("Não foi possível conectar ao banco de dados MySQL. " +
                    "Verifique se o servidor está ativo e as credenciais em db.properties estão corretas.\nDetalhes: " + e.getMessage(), e);
        }
    }

    public static void fecharRecursos(AutoCloseable... recursos) {
        if (recursos == null) return;
        for (AutoCloseable recurso : recursos) {
            if (recurso != null) {
                try {
                    recurso.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }
}
