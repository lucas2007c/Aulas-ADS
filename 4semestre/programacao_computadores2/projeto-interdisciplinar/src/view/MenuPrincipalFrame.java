package view;

import model.Usuario;
import services.SessaoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    private JButton btnEstoque;
    private JButton btnUsuarios;
    private JButton btnLogout;
    private JButton btnSair;

    public MenuPrincipalFrame() {
        if (!SessaoService.isAutenticado()) {
            JOptionPane.showMessageDialog(null, "Sessão expirada ou não autenticada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
            return;
        }
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        Usuario usuario = SessaoService.getUsuarioLogado();

        setTitle("AutoEstoque - Sistema de Concessionária");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 25, 15, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("AutoEstoque Concessionária");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 37, 41));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel lblUser = new JLabel("Conectado como: " + usuario.getNome() + " (" + usuario.getTipoAcesso().getDescricao() + ")");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(73, 80, 87));
        userPanel.add(lblUser);

        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(userPanel, BorderLayout.EAST);

        JSeparator sep = new JSeparator();
        JPanel northWrapper = new JPanel(new BorderLayout(0, 10));
        northWrapper.setOpaque(false);
        northWrapper.add(topPanel, BorderLayout.NORTH);
        northWrapper.add(sep, BorderLayout.SOUTH);
        mainPanel.add(northWrapper, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        btnEstoque = criarBotaoMenu("Estoque de Veículos", "Consultar, cadastrar e gerenciar carros e motos", new Color(13, 110, 253));
        btnUsuarios = criarBotaoMenu("Gerenciamento de Usuários", "Cadastro e controle de funcionários (Admin)", new Color(25, 135, 84));
        btnLogout = criarBotaoMenu("Trocar de Usuário", "Encerrar a sessão atual e retornar ao login", new Color(108, 117, 125));
        btnSair = criarBotaoMenu("Sair do Sistema", "Fechar a aplicação com segurança", new Color(220, 53, 69));

        if (!SessaoService.isAdministrador()) {
            btnUsuarios.setEnabled(false);
            btnUsuarios.setToolTipText("Apenas Administradores podem gerenciar usuários.");
        }

        centerPanel.add(btnEstoque);
        centerPanel.add(btnUsuarios);
        centerPanel.add(btnLogout);
        centerPanel.add(btnSair);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setOpaque(false);
        JLabel lblStatus = new JLabel("Disciplina: Programação Orientada a Objetos - IFSP");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(108, 117, 125));
        statusPanel.add(lblStatus, BorderLayout.WEST);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnEstoque.addActionListener(e -> {
            new EstoqueFrame().setVisible(true);
        });

        btnUsuarios.addActionListener(e -> {
            if (SessaoService.isAdministrador()) {
                new UsuarioManagementFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Você não possui permissão de Administrador para acessar esta funcionalidade.",
                        "Acesso Restrito",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente trocar de usuário?",
                    "Confirmar Logout",
                    JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                SessaoService.encerrarSessao();
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        btnSair.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Deseja encerrar o sistema?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private JButton criarBotaoMenu(String titulo, String subtitulo, Color corDestaque) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(5, 5));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(corDestaque);

        JLabel lblSub = new JLabel("<html><p style='width: 190px;'>" + subtitulo + "</p></html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(108, 117, 125));

        btn.add(lblTitle, BorderLayout.NORTH);
        btn.add(lblSub, BorderLayout.CENTER);

        return btn;
    }
}
