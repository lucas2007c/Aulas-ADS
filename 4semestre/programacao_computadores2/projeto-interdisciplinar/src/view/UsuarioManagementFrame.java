package view;

import exception.PermissaoNegadaException;
import model.Usuario;
import services.SessaoService;
import services.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioManagementFrame extends JFrame {

    private final UsuarioService usuarioService;

    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;

    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnAtualizar;
    private JButton btnFechar;

    private JLabel lblTotalUsuarios;

    public UsuarioManagementFrame() {
        this.usuarioService = new UsuarioService();

        if (!SessaoService.isAdministrador()) {
            JOptionPane.showMessageDialog(null,
                    "Acesso negado: esta funcionalidade é restrita a Administradores.",
                    "Permissão Negada",
                    JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        setTitle("AutoEstoque - Gerenciamento de Usuários");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 480);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Controle de Usuários e Funcionários");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 37, 41));

        JLabel lblSub = new JLabel("Gerencie as contas de acesso e níveis de permissão dos colaboradores.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(108, 117, 125));

        northPanel.add(lblTitulo, BorderLayout.NORTH);
        northPanel.add(lblSub, BorderLayout.SOUTH);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        String[] colunas = {"ID", "Nome Completo", "Nome de Usuário", "Nível de Acesso"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaUsuarios = new JTable(tableModel);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaUsuarios.setRowHeight(25);
        tabelaUsuarios.getTableHeader().setReorderingAllowed(false);
        tabelaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaUsuarios.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaUsuarios.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);

        lblTotalUsuarios = new JLabel("Total: 0");
        lblTotalUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalUsuarios.setForeground(new Color(108, 117, 125));
        southPanel.add(lblTotalUsuarios, BorderLayout.WEST);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);

        btnNovo = new JButton("Novo Usuário");
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNovo.setForeground(Color.BLACK);
        btnNovo.setFocusPainted(false);

        btnEditar = new JButton("Editar");
        btnEditar.setForeground(Color.BLACK);
        btnEditar.setFocusPainted(false);

        btnRemover = new JButton("Remover");
        btnRemover.setForeground(Color.BLACK);
        btnRemover.setFocusPainted(false);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setForeground(Color.BLACK);
        btnAtualizar.setFocusPainted(false);

        btnFechar = new JButton("Fechar");
        btnFechar.setForeground(Color.BLACK);
        btnFechar.setFocusPainted(false);

        actionsPanel.add(btnNovo);
        actionsPanel.add(btnEditar);
        actionsPanel.add(btnRemover);
        actionsPanel.add(btnAtualizar);
        actionsPanel.add(btnFechar);
        southPanel.add(actionsPanel, BorderLayout.EAST);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnRemover.addActionListener(e -> removerSelecionado());
        btnAtualizar.addActionListener(e -> carregarTabela());
        btnFechar.addActionListener(e -> dispose());

        tabelaUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarSelecionado();
                }
            }
        });
    }

    private void carregarTabela() {
        try {
            List<Usuario> lista = usuarioService.listarTodos();
            tableModel.setRowCount(0);
            for (Usuario u : lista) {
                tableModel.addRow(new Object[]{
                        u.getId(),
                        u.getNome(),
                        u.getUsuario(),
                        u.getTipoAcesso().getDescricao()
                });
            }
            lblTotalUsuarios.setText("Total de usuários: " + lista.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao listar usuários: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer getIdUsuarioSelecionado() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Integer) tableModel.getValueAt(selectedRow, 0);
    }

    private void abrirFormulario(Usuario usuario) {
        UsuarioFormDialog dialog = new UsuarioFormDialog(this, usuario);
        dialog.setVisible(true);
        if (dialog.isSalvoComSucesso()) {
            carregarTabela();
        }
    }

    private void editarSelecionado() {
        Integer id = getIdUsuarioSelecionado();
        if (id == null) return;

        try {
            Usuario u = usuarioService.buscarPorId(id);
            if (u != null) {
                abrirFormulario(u);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerSelecionado() {
        Integer id = getIdUsuarioSelecionado();
        if (id == null) return;

        int selectedRow = tabelaUsuarios.getSelectedRow();
        String nome = (String) tableModel.getValueAt(selectedRow, 1);
        String usuario = (String) tableModel.getValueAt(selectedRow, 2);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente remover o usuário '" + nome + "' (" + usuario + ")?",
                "Confirmar Remoção de Usuário",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                usuarioService.remover(id);
                JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
