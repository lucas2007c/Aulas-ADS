package view;

import exception.ValidacaoException;
import model.Carro;
import model.Moto;
import model.StatusVeiculo;
import model.TipoVeiculo;
import model.Veiculo;
import services.VeiculoService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class EstoqueFrame extends JFrame {

    private final VeiculoService veiculoService;
    private static final NumberFormat MOEDA_FORMAT = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTable tabelaVeiculos;
    private DefaultTableModel tableModel;

    private JTextField txtBuscaTexto;
    private JComboBox<String> cbFiltroTipo;
    private JComboBox<String> cbFiltroStatus;
    private JTextField txtFiltroMarca;
    private JTextField txtFiltroModelo;
    private JTextField txtFiltroPlaca;
    private JTextField txtFiltroAno;
    private JTextField txtFiltroCor;

    private JButton btnFiltrar;
    private JButton btnLimparFiltros;
    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnAlternarStatus;
    private JButton btnRemover;
    private JButton btnAtualizar;

    private JLabel lblTotalRegistros;

    public EstoqueFrame() {
        this.veiculoService = new VeiculoService();
        inicializarComponentes();
        carregarTabela();
    }

    private void inicializarComponentes() {
        setTitle("AutoEstoque - Gerenciamento de Estoque de Veículos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        JPanel northPanel = new JPanel(new BorderLayout(5, 10));
        northPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("Estoque de Veículos (Carros e Motos)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(33, 37, 41));
        northPanel.add(lblTitulo, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210)),
                "Filtros de Pesquisa",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(73, 80, 87)
        ));
        filterPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Busca Geral:"), gbc);
        txtBuscaTexto = new JTextField();
        txtBuscaTexto.setToolTipText("Pesquise por marca, modelo ou placa...");
        gbc.gridx = 1; gbc.weightx = 0.4;
        filterPanel.add(txtBuscaTexto, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Tipo:"), gbc);
        cbFiltroTipo = new JComboBox<>(new String[]{"Todos", "Carro", "Moto"});
        gbc.gridx = 3; gbc.weightx = 0.2;
        filterPanel.add(cbFiltroTipo, gbc);

        gbc.gridx = 4; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Status:"), gbc);
        cbFiltroStatus = new JComboBox<>(new String[]{"Todos", "Disponível", "Vendido"});
        gbc.gridx = 5; gbc.weightx = 0.2;
        filterPanel.add(cbFiltroStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Marca:"), gbc);
        txtFiltroMarca = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.2;
        filterPanel.add(txtFiltroMarca, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Modelo:"), gbc);
        txtFiltroModelo = new JTextField();
        gbc.gridx = 3; gbc.weightx = 0.2;
        filterPanel.add(txtFiltroModelo, gbc);

        gbc.gridx = 4; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Placa:"), gbc);
        txtFiltroPlaca = new JTextField();
        gbc.gridx = 5; gbc.weightx = 0.2;
        filterPanel.add(txtFiltroPlaca, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Ano:"), gbc);
        txtFiltroAno = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.2;
        filterPanel.add(txtFiltroAno, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        filterPanel.add(new JLabel("Cor:"), gbc);
        txtFiltroCor = new JTextField();
        gbc.gridx = 3; gbc.weightx = 0.2;
        filterPanel.add(txtFiltroCor, gbc);

        JPanel filterButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        filterButtons.setOpaque(false);
        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setForeground(Color.BLACK);
        btnFiltrar.setFocusPainted(false);

        btnLimparFiltros = new JButton("Limpar");
        btnLimparFiltros.setForeground(Color.BLACK);
        btnLimparFiltros.setFocusPainted(false);

        filterButtons.add(btnFiltrar);
        filterButtons.add(btnLimparFiltros);

        gbc.gridx = 4; gbc.gridy = 2; gbc.gridwidth = 2;
        filterPanel.add(filterButtons, gbc);

        northPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(northPanel, BorderLayout.NORTH);

        String[] colunas = {"ID", "Tipo", "Marca", "Modelo", "Ano", "Cor", "Placa", "KM", "Preço Aquisição", "Especificação", "Data Entrada", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaVeiculos = new JTable(tableModel);
        tabelaVeiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaVeiculos.setRowHeight(25);
        tabelaVeiculos.getTableHeader().setReorderingAllowed(false);
        tabelaVeiculos.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaVeiculos.getColumnModel().getColumn(0).setPreferredWidth(45);
        tabelaVeiculos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabelaVeiculos.getColumnModel().getColumn(1).setPreferredWidth(65);
        tabelaVeiculos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabelaVeiculos.getColumnModel().getColumn(4).setPreferredWidth(55);
        tabelaVeiculos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tabelaVeiculos.getColumnModel().getColumn(6).setPreferredWidth(85);
        tabelaVeiculos.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
        tabelaVeiculos.getColumnModel().getColumn(10).setPreferredWidth(90);
        tabelaVeiculos.getColumnModel().getColumn(10).setCellRenderer(centerRenderer);
        tabelaVeiculos.getColumnModel().getColumn(11).setPreferredWidth(85);
        tabelaVeiculos.getColumnModel().getColumn(11).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tabelaVeiculos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);

        lblTotalRegistros = new JLabel("Total de veículos: 0");
        lblTotalRegistros.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTotalRegistros.setForeground(new Color(108, 117, 125));
        southPanel.add(lblTotalRegistros, BorderLayout.WEST);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setOpaque(false);

        btnNovo = new JButton("Novo Veículo");
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNovo.setForeground(Color.BLACK);
        btnNovo.setFocusPainted(false);

        btnEditar = new JButton("Editar");
        btnEditar.setForeground(Color.BLACK);
        btnEditar.setFocusPainted(false);

        btnAlternarStatus = new JButton("Alternar Status");
        btnAlternarStatus.setForeground(Color.BLACK);
        btnAlternarStatus.setFocusPainted(false);

        btnRemover = new JButton("Remover");
        btnRemover.setForeground(Color.BLACK);
        btnRemover.setFocusPainted(false);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setForeground(Color.BLACK);
        btnAtualizar.setFocusPainted(false);

        actionsPanel.add(btnNovo);
        actionsPanel.add(btnEditar);
        actionsPanel.add(btnAlternarStatus);
        actionsPanel.add(btnRemover);
        actionsPanel.add(btnAtualizar);
        southPanel.add(actionsPanel, BorderLayout.EAST);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);

        btnFiltrar.addActionListener(e -> carregarTabela());
        btnLimparFiltros.addActionListener(e -> limparFiltros());
        btnAtualizar.addActionListener(e -> carregarTabela());

        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> editarSelecionado());
        btnAlternarStatus.addActionListener(e -> alternarStatusSelecionado());
        btnRemover.addActionListener(e -> removerSelecionado());

        tabelaVeiculos.addMouseListener(new java.awt.event.MouseAdapter() {
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
            String textoBusca = txtBuscaTexto.getText().trim();
            String marca = txtFiltroMarca.getText().trim();
            String modelo = txtFiltroModelo.getText().trim();
            String placa = txtFiltroPlaca.getText().trim();
            String cor = txtFiltroCor.getText().trim();

            Integer ano = null;
            if (!txtFiltroAno.getText().trim().isEmpty()) {
                try {
                    ano = Integer.parseInt(txtFiltroAno.getText().trim());
                } catch (NumberFormatException ignored) {
                }
            }

            TipoVeiculo tipo = null;
            String tipoStr = (String) cbFiltroTipo.getSelectedItem();
            if ("Carro".equals(tipoStr)) tipo = TipoVeiculo.CARRO;
            if ("Moto".equals(tipoStr)) tipo = TipoVeiculo.MOTO;

            StatusVeiculo status = null;
            String statusStr = (String) cbFiltroStatus.getSelectedItem();
            if ("Disponível".equals(statusStr)) status = StatusVeiculo.DISPONIVEL;
            if ("Vendido".equals(statusStr)) status = StatusVeiculo.VENDIDO;

            List<Veiculo> lista = veiculoService.listarComFiltros(textoBusca, marca, modelo, placa, ano, cor, tipo, status);

            tableModel.setRowCount(0);
            for (Veiculo v : lista) {
                tableModel.addRow(new Object[]{
                        v.getId(),
                        v.getTipoVeiculo().getDescricao(),
                        v.getMarca(),
                        v.getModelo(),
                        v.getAnoModelo(),
                        v.getCor(),
                        v.getPlaca(),
                        v.getQuilometragem() + " km",
                        MOEDA_FORMAT.format(v.getPrecoAquisicao()),
                        v.getEspecificacaoDetalhada(),
                        v.getDataEntrada() != null ? v.getDataEntrada().format(DATA_FORMAT) : "-",
                        v.getStatus().getDescricao()
                });
            }

            lblTotalRegistros.setText("Total de veículos: " + lista.size());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados do estoque: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFiltros() {
        txtBuscaTexto.setText("");
        cbFiltroTipo.setSelectedIndex(0);
        cbFiltroStatus.setSelectedIndex(0);
        txtFiltroMarca.setText("");
        txtFiltroModelo.setText("");
        txtFiltroPlaca.setText("");
        txtFiltroAno.setText("");
        txtFiltroCor.setText("");
        carregarTabela();
    }

    private Integer getIdVeiculoSelecionado() {
        int selectedRow = tabelaVeiculos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um veículo na tabela primeiro.",
                    "Nenhuma Seleção",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return (Integer) tableModel.getValueAt(selectedRow, 0);
    }

    private void abrirFormulario(Veiculo veiculo) {
        VeiculoFormDialog dialog = new VeiculoFormDialog(this, veiculo);
        dialog.setVisible(true);
        if (dialog.isSalvoComSucesso()) {
            carregarTabela();
        }
    }

    private void editarSelecionado() {
        Integer id = getIdVeiculoSelecionado();
        if (id == null) return;

        try {
            Veiculo v = veiculoService.buscarPorId(id);
            if (v != null) {
                abrirFormulario(v);
            } else {
                JOptionPane.showMessageDialog(this, "Veículo não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículo para edição: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alternarStatusSelecionado() {
        Integer id = getIdVeiculoSelecionado();
        if (id == null) return;

        int selectedRow = tabelaVeiculos.getSelectedRow();
        String statusAtualStr = (String) tableModel.getValueAt(selectedRow, 11);
        StatusVeiculo statusAtual = StatusVeiculo.fromString(statusAtualStr);
        StatusVeiculo novoStatus = (statusAtual == StatusVeiculo.DISPONIVEL) ? StatusVeiculo.VENDIDO : StatusVeiculo.DISPONIVEL;

        int resposta = JOptionPane.showConfirmDialog(this,
                "Deseja alterar o status do veículo para '" + novoStatus.getDescricao() + "'?",
                "Confirmar Alteração de Status",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                veiculoService.atualizarStatus(id, novoStatus);
                JOptionPane.showMessageDialog(this, "Status atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar status: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerSelecionado() {
        Integer id = getIdVeiculoSelecionado();
        if (id == null) return;

        int selectedRow = tabelaVeiculos.getSelectedRow();
        String marcaModelo = tableModel.getValueAt(selectedRow, 2) + " " + tableModel.getValueAt(selectedRow, 3);
        String placa = (String) tableModel.getValueAt(selectedRow, 6);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "ATENÇÃO: Deseja realmente remover o veículo '" + marcaModelo + "' (Placa: " + placa + ")?\nEsta ação não poderá ser desfeita.",
                "Confirmar Remoção de Veículo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                veiculoService.remover(id);
                JOptionPane.showMessageDialog(this, "Veículo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarTabela();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao remover veículo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
