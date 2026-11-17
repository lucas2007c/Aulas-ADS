package services;

import db.VeiculoDAO;
import exception.PermissaoNegadaException;
import exception.ValidacaoException;
import model.StatusVeiculo;
import model.TipoVeiculo;
import model.Veiculo;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.regex.Pattern;

public class VeiculoService {

    private final VeiculoDAO veiculoDAO;

    private static final Pattern PADRAO_PLACA = Pattern.compile("^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$");

    public VeiculoService() {
        this.veiculoDAO = new VeiculoDAO();
    }

    public VeiculoService(VeiculoDAO veiculoDAO) {
        this.veiculoDAO = veiculoDAO;
    }

    public void cadastrar(Veiculo veiculo) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAutenticacao();
        validarVeiculo(veiculo);

        if (veiculoDAO.existeOutroComPlaca(veiculo.getPlaca(), null)) {
            throw new ValidacaoException("Já existe um veículo cadastrado com a placa '" + veiculo.getPlaca() + "'.");
        }

        veiculoDAO.inserir(veiculo);
    }

    public void atualizar(Veiculo veiculo) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAutenticacao();
        if (veiculo.getId() == null) {
            throw new ValidacaoException("O ID do veículo é obrigatório para atualização.");
        }
        validarVeiculo(veiculo);

        if (veiculoDAO.existeOutroComPlaca(veiculo.getPlaca(), veiculo.getId())) {
            throw new ValidacaoException("Já existe outro veículo cadastrado com a placa '" + veiculo.getPlaca() + "'.");
        }

        veiculoDAO.atualizar(veiculo);
    }

    public void remover(int id) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAutenticacao();
        Veiculo existente = veiculoDAO.buscarPorId(id);
        if (existente == null) {
            throw new ValidacaoException("Veículo não encontrado para remoção.");
        }
        veiculoDAO.remover(id);
    }

    public void atualizarStatus(int id, StatusVeiculo novoStatus) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAutenticacao();
        if (novoStatus == null) {
            throw new ValidacaoException("O novo status não pode ser nulo.");
        }
        Veiculo veiculo = veiculoDAO.buscarPorId(id);
        if (veiculo == null) {
            throw new ValidacaoException("Veículo não encontrado.");
        }
        veiculo.setStatus(novoStatus);
        veiculoDAO.atualizar(veiculo);
    }

    public Veiculo buscarPorId(int id) throws PermissaoNegadaException {
        SessaoService.exigirAutenticacao();
        return veiculoDAO.buscarPorId(id);
    }

    public List<Veiculo> listarTodos() throws PermissaoNegadaException {
        SessaoService.exigirAutenticacao();
        return veiculoDAO.listarTodos();
    }

    public List<Veiculo> listarComFiltros(String textoBusca, String marca, String modelo,
                                          String placa, Integer ano, String cor,
                                          TipoVeiculo tipoVeiculo, StatusVeiculo status) throws PermissaoNegadaException {
        SessaoService.exigirAutenticacao();
        return veiculoDAO.listarComFiltros(textoBusca, marca, modelo, placa, ano, cor, tipoVeiculo, status);
    }

    private void validarVeiculo(Veiculo v) throws ValidacaoException {
        if (v == null) {
            throw new ValidacaoException("Os dados do veículo não podem ser nulos.");
        }

        if (v.getMarca() == null || v.getMarca().trim().isEmpty()) {
            throw new ValidacaoException("A marca do veículo é obrigatória.");
        }

        if (v.getModelo() == null || v.getModelo().trim().isEmpty()) {
            throw new ValidacaoException("O modelo do veículo é obrigatório.");
        }

        int anoMaximoPermitido = Year.now().getValue() + 1;
        if (v.getAnoModelo() < 1900 || v.getAnoModelo() > anoMaximoPermitido) {
            throw new ValidacaoException("Ano do modelo inválido. Deve estar entre 1900 e " + anoMaximoPermitido + ".");
        }

        if (v.getCor() == null || v.getCor().trim().isEmpty()) {
            throw new ValidacaoException("A cor do veículo é obrigatória.");
        }

        if (v.getPlaca() == null || v.getPlaca().trim().isEmpty()) {
            throw new ValidacaoException("A placa do veículo é obrigatória.");
        }

        String placaNormalizada = v.getPlaca().trim().toUpperCase().replace("-", "");
        if (!PADRAO_PLACA.matcher(placaNormalizada).matches()) {
            throw new ValidacaoException("Placa inválida. Utilize o formato tradicional (ABC1234) ou Mercosul (ABC1D23).");
        }
        v.setPlaca(placaNormalizada);

        if (v.getQuilometragem() < 0) {
            throw new ValidacaoException("A quilometragem não pode ser negativa.");
        }

        if (v.getPrecoAquisicao() <= 0) {
            throw new ValidacaoException("O preço de aquisição deve ser maior que zero.");
        }

        if (v.getTipoVeiculo() == null) {
            throw new ValidacaoException("O tipo de veículo deve ser informado (Carro ou Moto).");
        }

        if (v.getDataEntrada() == null) {
            v.setDataEntrada(LocalDate.now());
        }

        if (v.getStatus() == null) {
            v.setStatus(StatusVeiculo.DISPONIVEL);
        }

        v.validarEspecificidades();
    }
}
