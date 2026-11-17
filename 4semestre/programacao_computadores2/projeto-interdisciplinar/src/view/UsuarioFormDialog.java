package view;

import exception.ValidacaoException;
import model.TipoAcesso;
import model.Usuario;
import services.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UsuarioFormDialog extends JDialog {

    private final UsuarioService usuarioService;
    private final Usuario usuarioOriginal;
    private boolean salvoComSucesso = false;

    private JTextField txtNome;
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JComboBox<TipoAcesso> cbTipoAcesso;
    private JLabel lblDicaSenha;

    private JButton btnSalvar;
    private JButton btnCancelar;

    public UsuarioFormDialog(Frame parent, Usuario usuarioParaEdicao) {
        super(parent, usuarioParaEdicao == null ? "Cadastrar Novo Usuário" : "Editar Usuário", true);
        this.usuarioService = new UsuarioService();
        this.usuarioOriginal = usuarioParaEdicao;

        inicializarComponentes();
        if (usuarioOriginal != null) {
            preencherCamposParaEdicao();
        }
    }

    private void inicializarComponentes() {
        setSize(460, 350);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        formPanel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 6, 8, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Nome Completo: *"), gbc);
        txtNome = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtNome, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Nome de Usuário: *"), gbc);
        txtUsuario = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtUsuario, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Senha: *"), gbc);
        txtSenha = new JPasswordField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtSenha, gbc);
        row++;

        lblDicaSenha = new JLabel(usuarioOriginal == null ? "Mínimo de 4 caracteres." : "Deixe em branco para manter a senha atual.");
        lblDicaSenha.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDicaSenha.setForeground(new Color(108, 117, 125));
        gbc.gridx = 1; gbc.gridy = row;
        formPanel.add(lblDicaSenha, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Tipo de Acesso: *"), gbc);
        cbTipoAcesso = new JComboBox<>(TipoAcesso.values());
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(cbTipoAcesso, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setFocusPainted(false);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setForeground(Color.BLACK);
        btnCancelar.setFocusPainted(false);

        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void preencherCamposParaEdicao() {
        txtNome.setText(usuarioOriginal.getNome());
        txtUsuario.setText(usuarioOriginal.getUsuario());
        cbTipoAcesso.setSelectedItem(usuarioOriginal.getTipoAcesso());
    }

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String usuarioLogin = txtUsuario.getText().trim();
            String senha = new String(txtSenha.getPassword());
            TipoAcesso tipoAcesso = (TipoAcesso) cbTipoAcesso.getSelectedItem();

            if (usuarioOriginal == null) {
                Usuario novo = new Usuario(nome, usuarioLogin, null, tipoAcesso);
                usuarioService.cadastrar(novo, senha);
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                usuarioOriginal.setNome(nome);
                usuarioOriginal.setUsuario(usuarioLogin);
                usuarioOriginal.setTipoAcesso(tipoAcesso);
                usuarioService.atualizar(usuarioOriginal, senha);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            salvoComSucesso = true;
            dispose();

        } catch (ValidacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvoComSucesso() {
        return salvoComSucesso;
    }
}
