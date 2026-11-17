package view;

import exception.AutenticacaoException;
import exception.ValidacaoException;
import model.Usuario;
import services.SessaoService;
import services.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private final UsuarioService usuarioService;

    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnSair;

    public LoginFrame() {
        this.usuarioService = new UsuarioService();
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Concessionária - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 320);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("AutoEstoque", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 37, 41));

        JLabel lblSubtitulo = new JLabel("Gerenciamento de Estoque de Veículos", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(108, 117, 125));

        headerPanel.add(lblTitulo);
        headerPanel.add(lblSubtitulo);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(lblUsuario, gbc);

        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        formPanel.add(txtUsuario, gbc);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        formPanel.add(lblSenha, gbc);

        txtSenha = new JPasswordField();
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        formPanel.add(txtSenha, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEntrar.setPreferredSize(new Dimension(110, 35));
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setFocusPainted(false);

        btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSair.setPreferredSize(new Dimension(100, 35));
        btnSair.setForeground(Color.BLACK);
        btnSair.setFocusPainted(false);

        buttonPanel.add(btnEntrar);
        buttonPanel.add(btnSair);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnEntrar.addActionListener(e -> executarLogin());
        btnSair.addActionListener(e -> System.exit(0));

        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    executarLogin();
                }
            }
        };

        txtUsuario.addKeyListener(enterKeyListener);
        txtSenha.addKeyListener(enterKeyListener);
    }

    private void executarLogin() {
        String login = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        try {
            Usuario usuario = usuarioService.autenticar(login, senha);
            SessaoService.iniciarSessao(usuario);

            JOptionPane.showMessageDialog(this,
                    "Bem-vindo(a), " + usuario.getNome() + "!\nAcesso: " + usuario.getTipoAcesso().getDescricao(),
                    "Autenticação com Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            new MenuPrincipalFrame().setVisible(true);

        } catch (ValidacaoException | AutenticacaoException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Falha no Login",
                    JOptionPane.WARNING_MESSAGE);
            txtSenha.setText("");
            txtSenha.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocorreu um erro ao tentar conectar: " + ex.getMessage(),
                    "Erro de Conexão",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
