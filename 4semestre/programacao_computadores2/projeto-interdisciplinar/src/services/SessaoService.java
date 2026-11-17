package services;

import exception.PermissaoNegadaException;
import model.TipoAcesso;
import model.Usuario;

public class SessaoService {

    private static Usuario usuarioLogado;

    private SessaoService() {
    }

    public static void iniciarSessao(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void encerrarSessao() {
        usuarioLogado = null;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static boolean isAutenticado() {
        return usuarioLogado != null;
    }

    public static boolean isAdministrador() {
        return usuarioLogado != null && TipoAcesso.ADMINISTRADOR.equals(usuarioLogado.getTipoAcesso());
    }

    public static void exigirAutenticacao() throws PermissaoNegadaException {
        if (!isAutenticado()) {
            throw new PermissaoNegadaException("Nenhum usuário está autenticado no momento.");
        }
    }

    public static void exigirAdministrador() throws PermissaoNegadaException {
        exigirAutenticacao();
        if (!isAdministrador()) {
            throw new PermissaoNegadaException("Acesso negado: operação permitida exclusivamente para Administradores.");
        }
    }
}
