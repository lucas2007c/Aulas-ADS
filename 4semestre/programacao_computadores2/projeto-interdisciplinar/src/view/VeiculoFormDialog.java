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
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class VeiculoFormDialog extends JDialog {

    private final VeiculoService veiculoService;
    private final Veiculo veiculoOriginal;
    private boolean salvoComSucesso = false;

    private JComboBox<TipoVeiculo> cbTipoVeiculo;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtAnoModelo;
    private JTextField txtCor;
    private JTextField txtPlaca;
    private JTextField txtQuilometragem;
    private JTextField txtPrecoAquisicao;
    private JTextField txtDataEntrada;
    private JComboBox<StatusVeiculo> cbStatus;

    private JPanel painelEspecifico;
    private JPanel painelCarro;
    private JPanel painelMoto;
    private JTextField txtQuantidadePortas;
    private JTextField txtCilindradas;

    private JButton btnSalvar;
    private JButton btnCancelar;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VeiculoFormDialog(Frame parent, Veiculo veiculoParaEdicao) {
        super(parent, veiculoParaEdicao == null ? "Cadastrar Novo Veículo" : "Editar Veículo", true);
        this.veiculoService = new VeiculoService();
        this.veiculoOriginal = veiculoParaEdicao;

        inicializarComponentes();
        if (veiculoOriginal != null) {
            preencherCamposParaEdicao();
        }
    }

    private void inicializarComponentes() {
        setSize(520, 580);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        formPanel.setBackground(new Color(250, 250, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Tipo de Veículo:"), gbc);

        cbTipoVeiculo = new JComboBox<>(TipoVeiculo.values());
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(cbTipoVeiculo, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Marca: *"), gbc);
        txtMarca = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtMarca, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Modelo: *"), gbc);
        txtModelo = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtModelo, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Ano do Modelo: *"), gbc);
        txtAnoModelo = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtAnoModelo, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Cor: *"), gbc);
        txtCor = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtCor, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Placa (ex: ABC1234): *"), gbc);
        txtPlaca = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtPlaca, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Quilometragem (km): *"), gbc);
        txtQuilometragem = new JTextField("0");
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtQuilometragem, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Preço Aquisição (R$): *"), gbc);
        txtPrecoAquisicao = new JTextField();
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtPrecoAquisicao, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Data Entrada (DD/MM/AAAA):"), gbc);
        txtDataEntrada = new JTextField(LocalDate.now().format(DATE_FORMATTER));
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(txtDataEntrada, gbc);
        row++;

        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        formPanel.add(new JLabel("Status de Estoque:"), gbc);
        cbStatus = new JComboBox<>(StatusVeiculo.values());
        gbc.gridx = 1; gbc.weightx = 0.65;
        formPanel.add(cbStatus, gbc);
        row++;

        painelEspecifico = new JPanel(new CardLayout());
        painelEspecifico.setOpaque(false);

        painelCarro = new JPanel(new BorderLayout(5, 5));
        painelCarro.setOpaque(false);
        painelCarro.add(new JLabel("Portas (2 a 6):"), BorderLayout.WEST);
        txtQuantidadePortas = new JTextField("4");
        painelCarro.add(txtQuantidadePortas, BorderLayout.CENTER);

        painelMoto = new JPanel(new BorderLayout(5, 5));
        painelMoto.setOpaque(false);
        painelMoto.add(new JLabel("Cilindradas (cc):"), BorderLayout.WEST);
        txtCilindradas = new JTextField("160");
        painelMoto.add(txtCilindradas, BorderLayout.CENTER);

        painelEspecifico.add(painelCarro, TipoVeiculo.CARRO.name());
        painelEspecifico.add(painelMoto, TipoVeiculo.MOTO.name());

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        formPanel.add(painelEspecifico, gbc);

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

        cbTipoVeiculo.addActionListener(e -> alternarPainelEspecifico());

        btnSalvar.addActionListener(e -> salvar());
        btnCancelar.addActionListener(e -> dispose());

        alternarPainelEspecifico();
    }

    private void alternarPainelEspecifico() {
        TipoVeiculo tipo = (TipoVeiculo) cbTipoVeiculo.getSelectedItem();
        CardLayout cl = (CardLayout) (painelEspecifico.getLayout());
        if (tipo != null) {
            cl.show(painelEspecifico, tipo.name());
        }
    }

    private void preencherCamposParaEdicao() {
        cbTipoVeiculo.setSelectedItem(veiculoOriginal.getTipoVeiculo());
        cbTipoVeiculo.setEnabled(false);
        txtMarca.setText(veiculoOriginal.getMarca());
        txtModelo.setText(veiculoOriginal.getModelo());
        txtAnoModelo.setText(String.valueOf(veiculoOriginal.getAnoModelo()));
        txtCor.setText(veiculoOriginal.getCor());
        txtPlaca.setText(veiculoOriginal.getPlaca());
        txtQuilometragem.setText(String.valueOf(veiculoOriginal.getQuilometragem()));
        txtPrecoAquisicao.setText(String.format(Locale.US, "%.2f", veiculoOriginal.getPrecoAquisicao()));
        if (veiculoOriginal.getDataEntrada() != null) {
            txtDataEntrada.setText(veiculoOriginal.getDataEntrada().format(DATE_FORMATTER));
        }
        cbStatus.setSelectedItem(veiculoOriginal.getStatus());

        if (veiculoOriginal instanceof Carro) {
            txtQuantidadePortas.setText(String.valueOf(((Carro) veiculoOriginal).getQuantidadePortas()));
        } else if (veiculoOriginal instanceof Moto) {
            txtCilindradas.setText(String.valueOf(((Moto) veiculoOriginal).getCilindradas()));
        }
    }

    private void salvar() {
        try {
            TipoVeiculo tipo = (TipoVeiculo) cbTipoVeiculo.getSelectedItem();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String anoStr = txtAnoModelo.getText().trim();
            String cor = txtCor.getText().trim();
            String placa = txtPlaca.getText().trim();
            String kmStr = txtQuilometragem.getText().trim();
            String precoStr = txtPrecoAquisicao.getText().trim().replace(",", ".");
            String dataStr = txtDataEntrada.getText().trim();
            StatusVeiculo status = (StatusVeiculo) cbStatus.getSelectedItem();

            int ano;
            try {
                ano = Integer.parseInt(anoStr);
            } catch (NumberFormatException e) {
                throw new ValidacaoException("Ano do modelo deve ser um número inteiro válido.");
            }

            int km;
            try {
                km = Integer.parseInt(kmStr);
            } catch (NumberFormatException e) {
                throw new ValidacaoException("Quilometragem deve ser um número inteiro válido.");
            }

            double preco;
            try {
                preco = Double.parseDouble(precoStr);
            } catch (NumberFormatException e) {
                throw new ValidacaoException("Preço de aquisição deve ser um valor numérico válido (ex: 45000.00).");
            }

            LocalDate dataEntrada;
            try {
                dataEntrada = LocalDate.parse(dataStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new ValidacaoException("Data de entrada inválida. Utilize o formato DD/MM/AAAA.");
            }

            Veiculo veiculo;
            if (tipo == TipoVeiculo.CARRO) {
                int portas;
                try {
                    portas = Integer.parseInt(txtQuantidadePortas.getText().trim());
                } catch (NumberFormatException e) {
                    throw new ValidacaoException("Quantidade de portas deve ser um número inteiro válido.");
                }

                Carro carro;
                if (veiculoOriginal instanceof Carro) {
                    carro = (Carro) veiculoOriginal;
                } else {
                    carro = new Carro();
                }
                carro.setQuantidadePortas(portas);
                veiculo = carro;

            } else {
                int cilindradas;
                try {
                    cilindradas = Integer.parseInt(txtCilindradas.getText().trim());
                } catch (NumberFormatException e) {
                    throw new ValidacaoException("Cilindradas deve ser um número inteiro válido.");
                }

                Moto moto;
                if (veiculoOriginal instanceof Moto) {
                    moto = (Moto) veiculoOriginal;
                } else {
                    moto = new Moto();
                }
                moto.setCilindradas(cilindradas);
                veiculo = moto;
            }

            veiculo.setMarca(marca);
            veiculo.setModelo(modelo);
            veiculo.setAnoModelo(ano);
            veiculo.setCor(cor);
            veiculo.setPlaca(placa);
            veiculo.setQuilometragem(km);
            veiculo.setPrecoAquisicao(preco);
            veiculo.setDataEntrada(dataEntrada);
            veiculo.setStatus(status);

            if (veiculo.getId() == null) {
                veiculoService.cadastrar(veiculo);
                JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                veiculoService.atualizar(veiculo);
                JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }

            salvoComSucesso = true;
            dispose();

        } catch (ValidacaoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validação dos Dados", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar veículo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSalvoComSucesso() {
        return salvoComSucesso;
    }
}
