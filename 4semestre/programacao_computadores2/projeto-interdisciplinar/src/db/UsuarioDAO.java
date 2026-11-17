package db;

import exception.DatabaseException;
import model.TipoAcesso;
import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, usuario, senha, tipo_acesso) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipoAcesso().name());

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao cadastrar usuário no banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public void atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, usuario = ?, senha = ?, tipo_acesso = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getUsuario());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipoAcesso().name());
            ps.setInt(5, usuario.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar usuário no banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(ps, conn);
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao remover usuário do banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(ps, conn);
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, nome, usuario, senha, tipo_acesso FROM usuarios WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public Usuario buscarPorUsuario(String nomeUsuario) {
        String sql = "SELECT id, nome, usuario, senha, tipo_acesso FROM usuarios WHERE usuario = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nomeUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar usuário por nome de usuário: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public List<Usuario> listarTodos() {
        String sql = "SELECT id, nome, usuario, senha, tipo_acesso FROM usuarios ORDER BY nome ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Usuario> lista = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao listar usuários: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public int contarAdministradores() {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE tipo_acesso = 'ADMINISTRADOR'";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar administradores: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public boolean existeOutroComUsuario(String login, Integer idIgnorado) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ?" + (idIgnorado != null ? " AND id <> ?" : "");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            if (idIgnorado != null) {
                ps.setInt(2, idIgnorado);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao validar existência de nome de usuário: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("usuario"),
                rs.getString("senha"),
                TipoAcesso.valueOf(rs.getString("tipo_acesso"))
        );
    }
}
