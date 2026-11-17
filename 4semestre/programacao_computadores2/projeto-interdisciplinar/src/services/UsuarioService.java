package services;

import db.UsuarioDAO;
import exception.AutenticacaoException;
import exception.PermissaoNegadaException;
import exception.ValidacaoException;
import model.TipoAcesso;
import model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String login, String senhaPura) throws AutenticacaoException, ValidacaoException {
        if (login == null || login.trim().isEmpty()) {
            throw new ValidacaoException("Por favor, informe o nome de usuário.");
        }
        if (senhaPura == null || senhaPura.isEmpty()) {
            throw new ValidacaoException("Por favor, informe a senha.");
        }

        Usuario usuario = usuarioDAO.buscarPorUsuario(login.trim());
        if (usuario == null) {
            throw new AutenticacaoException("Usuário ou senha inválidos.");
        }

        if (!SecurityUtils.verificarSenha(senhaPura, usuario.getSenha())) {
            throw new AutenticacaoException("Usuário ou senha inválidos.");
        }

        return usuario;
    }

    public void cadastrar(Usuario usuario, String senhaPura) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAdministrador();
        validarDadosUsuario(usuario);

        if (senhaPura == null || senhaPura.trim().length() < 4) {
            throw new ValidacaoException("A senha inicial deve possuir no mínimo 4 caracteres.");
        }

        if (usuarioDAO.existeOutroComUsuario(usuario.getUsuario(), null)) {
            throw new ValidacaoException("Já existe um usuário cadastrado com o login '" + usuario.getUsuario() + "'.");
        }

        usuario.setSenha(SecurityUtils.hashSenha(senhaPura));
        usuarioDAO.inserir(usuario);
    }

    public void atualizar(Usuario usuario, String novaSenhaPura) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAdministrador();
        if (usuario.getId() == null) {
            throw new ValidacaoException("ID do usuário é obrigatório para atualização.");
        }
        validarDadosUsuario(usuario);

        if (usuarioDAO.existeOutroComUsuario(usuario.getUsuario(), usuario.getId())) {
            throw new ValidacaoException("Já existe outro usuário cadastrado com o login '" + usuario.getUsuario() + "'.");
        }

        Usuario existente = usuarioDAO.buscarPorId(usuario.getId());
        if (existente != null && existente.isAdministrador() && !usuario.isAdministrador()) {
            if (usuarioDAO.contarAdministradores() <= 1) {
                throw new ValidacaoException("Não é permitido alterar o nível de acesso do único Administrador cadastrado.");
            }
        }

        if (novaSenhaPura != null && !novaSenhaPura.trim().isEmpty()) {
            if (novaSenhaPura.trim().length() < 4) {
                throw new ValidacaoException("A nova senha deve possuir no mínimo 4 caracteres.");
            }
            usuario.setSenha(SecurityUtils.hashSenha(novaSenhaPura));
        } else if (existente != null) {
            usuario.setSenha(existente.getSenha());
        }

        usuarioDAO.atualizar(usuario);
    }

    public void remover(int idUsuario) throws PermissaoNegadaException, ValidacaoException {
        SessaoService.exigirAdministrador();

        Usuario atual = SessaoService.getUsuarioLogado();
        if (atual != null && atual.getId() != null && atual.getId().equals(idUsuario)) {
            throw new ValidacaoException("Você não pode remover a própria conta que está atualmente em uso.");
        }

        Usuario alvo = usuarioDAO.buscarPorId(idUsuario);
        if (alvo == null) {
            throw new ValidacaoException("Usuário não encontrado.");
        }

        if (alvo.isAdministrador() && usuarioDAO.contarAdministradores() <= 1) {
            throw new ValidacaoException("Não é permitido excluir o único Administrador do sistema.");
        }

        usuarioDAO.remover(idUsuario);
    }

    public List<Usuario> listarTodos() throws PermissaoNegadaException {
        SessaoService.exigirAdministrador();
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(int id) throws PermissaoNegadaException {
        SessaoService.exigirAdministrador();
        return usuarioDAO.buscarPorId(id);
    }

    private void validarDadosUsuario(Usuario usuario) throws ValidacaoException {
        if (usuario == null) {
            throw new ValidacaoException("Dados do usuário não podem ser nulos.");
        }
        if (usuario.getNome() == null || usuario.getNome().trim().length() < 3) {
            throw new ValidacaoException("O nome completo deve possuir no mínimo 3 caracteres.");
        }
        if (usuario.getUsuario() == null || usuario.getUsuario().trim().length() < 3) {
            throw new ValidacaoException("O nome de usuário deve possuir no mínimo 3 caracteres.");
        }
        if (usuario.getUsuario().contains(" ")) {
            throw new ValidacaoException("O nome de usuário não pode conter espaços em branco.");
        }
        if (usuario.getTipoAcesso() == null) {
            throw new ValidacaoException("Selecione um nível de acesso válido.");
        }
    }
}
