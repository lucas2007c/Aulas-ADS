package db;

import exception.DatabaseException;
import model.Carro;
import model.Moto;
import model.StatusVeiculo;
import model.TipoVeiculo;
import model.Veiculo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void inserir(Veiculo veiculo) {
        String sql = "INSERT INTO veiculos (" +
                "marca, modelo, ano_modelo, cor, placa, quilometragem, tipo_veiculo, " +
                "preco_aquisicao, data_entrada, status, quantidade_portas, cilindradas" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preencherStatement(ps, veiculo);

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                veiculo.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao cadastrar veículo no banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public void atualizar(Veiculo veiculo) {
        String sql = "UPDATE veiculos SET " +
                "marca = ?, modelo = ?, ano_modelo = ?, cor = ?, placa = ?, " +
                "quilometragem = ?, tipo_veiculo = ?, preco_aquisicao = ?, data_entrada = ?, " +
                "status = ?, quantidade_portas = ?, cilindradas = ? " +
                "WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            preencherStatement(ps, veiculo);
            ps.setInt(13, veiculo.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao atualizar veículo no banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(ps, conn);
        }
    }

    public void remover(int id) {
        String sql = "DELETE FROM veiculos WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao remover veículo do banco de dados: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(ps, conn);
        }
    }

    public Veiculo buscarPorId(int id) {
        String sql = "SELECT * FROM veiculos WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearVeiculo(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar veículo por ID: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public Veiculo buscarPorPlaca(String placa) {
        String sql = "SELECT * FROM veiculos WHERE placa = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, placa != null ? placa.trim().toUpperCase() : "");
            rs = ps.executeQuery();

            if (rs.next()) {
                return mapearVeiculo(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar veículo por placa: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public boolean existeOutroComPlaca(String placa, Integer idIgnorado) {
        String sql = "SELECT COUNT(*) FROM veiculos WHERE placa = ?" + (idIgnorado != null ? " AND id <> ?" : "");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, placa != null ? placa.trim().toUpperCase() : "");
            if (idIgnorado != null) {
                ps.setInt(2, idIgnorado);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao verificar duplicidade de placa: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    public List<Veiculo> listarTodos() {
        return listarComFiltros(null, null, null, null, null, null, null, null);
    }

    public List<Veiculo> listarComFiltros(String textoBusca, String marca, String modelo,
                                          String placa, Integer ano, String cor,
                                          TipoVeiculo tipoVeiculo, StatusVeiculo status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM veiculos WHERE 1=1 ");
        List<Object> parametros = new ArrayList<>();

        if (textoBusca != null && !textoBusca.trim().isEmpty()) {
            sql.append("AND (marca LIKE ? OR modelo LIKE ? OR placa LIKE ?) ");
            String term = "%" + textoBusca.trim() + "%";
            parametros.add(term);
            parametros.add(term);
            parametros.add(term);
        }

        if (marca != null && !marca.trim().isEmpty()) {
            sql.append("AND marca LIKE ? ");
            parametros.add("%" + marca.trim() + "%");
        }

        if (modelo != null && !modelo.trim().isEmpty()) {
            sql.append("AND modelo LIKE ? ");
            parametros.add("%" + modelo.trim() + "%");
        }

        if (placa != null && !placa.trim().isEmpty()) {
            sql.append("AND placa LIKE ? ");
            parametros.add("%" + placa.trim().toUpperCase() + "%");
        }

        if (ano != null && ano > 0) {
            sql.append("AND ano_modelo = ? ");
            parametros.add(ano);
        }

        if (cor != null && !cor.trim().isEmpty()) {
            sql.append("AND cor LIKE ? ");
            parametros.add("%" + cor.trim() + "%");
        }

        if (tipoVeiculo != null) {
            sql.append("AND tipo_veiculo = ? ");
            parametros.add(tipoVeiculo.name());
        }

        if (status != null) {
            sql.append("AND status = ? ");
            parametros.add(status.name());
        }

        sql.append("ORDER BY id DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Veiculo> veiculos = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                veiculos.add(mapearVeiculo(rs));
            }
            return veiculos;
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao consultar veículos: " + e.getMessage(), e);
        } finally {
            DatabaseConnection.fecharRecursos(rs, ps, conn);
        }
    }

    private void preencherStatement(PreparedStatement ps, Veiculo v) throws SQLException {
        ps.setString(1, v.getMarca());
        ps.setString(2, v.getModelo());
        ps.setInt(3, v.getAnoModelo());
        ps.setString(4, v.getCor());
        ps.setString(5, v.getPlaca());
        ps.setInt(6, v.getQuilometragem());
        ps.setString(7, v.getTipoVeiculo().name());
        ps.setDouble(8, v.getPrecoAquisicao());
        ps.setDate(9, v.getDataEntrada() != null ? Date.valueOf(v.getDataEntrada()) : Date.valueOf(java.time.LocalDate.now()));
        ps.setString(10, v.getStatus().name());

        if (v instanceof Carro) {
            Carro carro = (Carro) v;
            ps.setInt(11, carro.getQuantidadePortas());
            ps.setNull(12, Types.INTEGER);
        } else if (v instanceof Moto) {
            Moto moto = (Moto) v;
            ps.setNull(11, Types.INTEGER);
            ps.setInt(12, moto.getCilindradas());
        } else {
            ps.setNull(11, Types.INTEGER);
            ps.setNull(12, Types.INTEGER);
        }
    }

    private Veiculo mapearVeiculo(ResultSet rs) throws SQLException {
        TipoVeiculo tipo = TipoVeiculo.valueOf(rs.getString("tipo_veiculo"));
        Veiculo veiculo;

        if (tipo == TipoVeiculo.CARRO) {
            Carro c = new Carro();
            c.setQuantidadePortas(rs.getInt("quantidade_portas"));
            veiculo = c;
        } else {
            Moto m = new Moto();
            m.setCilindradas(rs.getInt("cilindradas"));
            veiculo = m;
        }

        veiculo.setId(rs.getInt("id"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setAnoModelo(rs.getInt("ano_modelo"));
        veiculo.setCor(rs.getString("cor"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setQuilometragem(rs.getInt("quilometragem"));
        veiculo.setPrecoAquisicao(rs.getDouble("preco_aquisicao"));
        Date data = rs.getDate("data_entrada");
        if (data != null) {
            veiculo.setDataEntrada(data.toLocalDate());
        }
        veiculo.setStatus(StatusVeiculo.valueOf(rs.getString("status")));

        return veiculo;
    }
}
