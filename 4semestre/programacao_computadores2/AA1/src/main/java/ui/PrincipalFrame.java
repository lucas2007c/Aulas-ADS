package ui;

import dao.ContaDAO;
import dao.TransferenciaDAO;
import dao.TransferenciaService;
import exception.SaldoInsuficienteException;
import model.Conta;
import model.TipoTarifa;
import model.Transferencia;
import strategy.TarifaStrategyFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrincipalFrame extends JFrame {
    private final JTextField txtOrigem = new JTextField();
    private final JTextField txtDestino = new JTextField();
    private final JTextField txtValor = new JTextField();
    private final JComboBox<TipoTarifa> cbTarifa =
        new JComboBox<>(TipoTarifa.values());
    private final JTextField txtHistoricoConta = new JTextField();

    private final DefaultTableModel modelo = new DefaultTableModel(
        new Object[]{"Data/Hora", "Origem", "Destino", "Valor", "Tarifa", "Modalidade"}, 0);
    private final JTable tabela = new JTable(modelo);
    private final TransferenciaService service = new TransferenciaService();

    public PrincipalFrame() {
        setTitle("Banco Digital - Transferências");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);

        JPanel transferencia = new JPanel(new GridLayout(5, 2, 8, 8));
        transferencia.setBorder(BorderFactory.createTitledBorder("Nova transferência"));

        transferencia.add(new JLabel("Conta origem:"));
        transferencia.add(txtOrigem);
        transferencia.add(new JLabel("Conta destino:"));
        transferencia.add(txtDestino);
        transferencia.add(new JLabel("Valor:"));
        transferencia.add(txtValor);
        transferencia.add(new JLabel("Tarifa:"));
        transferencia.add(cbTarifa);

        JButton btnTransferir = new JButton("Transferir");
        btnTransferir.addActionListener(e -> realizarTransferencia());
        transferencia.add(new JLabel());
        transferencia.add(btnTransferir);

        JPanel historico = new JPanel(new BorderLayout(8, 8));
        historico.setBorder(BorderFactory.createTitledBorder("Histórico"));

        JPanel topo = new JPanel(new BorderLayout(8, 8));
        topo.add(new JLabel("Conta:"), BorderLayout.WEST);
        topo.add(txtHistoricoConta, BorderLayout.CENTER);

        JButton btnConsultar = new JButton("Consultar");
        btnConsultar.addActionListener(e -> consultarHistorico());
        topo.add(btnConsultar, BorderLayout.EAST);

        historico.add(topo, BorderLayout.NORTH);
        historico.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JButton btnContas = new JButton("Listar contas");
        btnContas.addActionListener(e -> listarContas());

        setLayout(new BorderLayout(10, 10));
        add(transferencia, BorderLayout.NORTH);
        add(historico, BorderLayout.CENTER);
        add(btnContas, BorderLayout.SOUTH);
    }

    private void realizarTransferencia() {
        try {
            int origem = Integer.parseInt(txtOrigem.getText().trim());
            int destino = Integer.parseInt(txtDestino.getText().trim());
            double valor = Double.parseDouble(
                txtValor.getText().trim().replace(",", "."));
            TipoTarifa tipo = (TipoTarifa) cbTarifa.getSelectedItem();

            Transferencia t = service.transferir(
                origem, destino, valor, TarifaStrategyFactory.criar(tipo));

            JOptionPane.showMessageDialog(this,
                String.format("Transferência realizada!%nTarifa: R$ %.2f",
                    t.getTarifa()));

            txtHistoricoConta.setText(String.valueOf(origem));
            consultarHistorico();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe números válidos.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SaldoInsuficienteException | IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consultarHistorico() {
        try {
            int conta = Integer.parseInt(txtHistoricoConta.getText().trim());
            List<Transferencia> lista =
                new TransferenciaDAO().listarPorConta(conta);

            modelo.setRowCount(0);
            DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Transferencia t : lista) {
                modelo.addRow(new Object[]{
                    t.getDataHora().format(fmt),
                    t.getContaOrigem(),
                    t.getContaDestino(),
                    String.format("R$ %.2f", t.getValor()),
                    String.format("R$ %.2f", t.getTarifa()),
                    t.getModalidadeTarifa()
                });
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe uma conta válida.",
                "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarContas() {
        try {
            List<Conta> contas = new ContaDAO().listar();
            String texto = String.join("\n",
                contas.stream().map(Conta::toString).toList());
            JOptionPane.showMessageDialog(this, texto,
                "Contas", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
