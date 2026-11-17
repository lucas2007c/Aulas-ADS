package app;

import db.DatabaseInitializer;
import view.LoginFrame;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        configurarAparencia();

        try {
            System.out.println("Inicializando banco de dados MySQL...");
            DatabaseInitializer.inicializarBanco();
            System.out.println("Banco de dados pronto para operacao.");
        } catch (Exception e) {
            System.err.println("Erro na inicializacao do banco de dados: " + e.getMessage());
            int opcao = JOptionPane.showConfirmDialog(null,
                    "Não foi possível conectar ao banco de dados MySQL:\n" + e.getMessage() +
                    "\n\nCertifique-se de que o servidor MySQL está ativo localmente e as configurações " +
                    "em 'db.properties' estão corretas.\n\nDeseja continuar mesmo assim?",
                    "Aviso de Conexão com o Banco",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (opcao != JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    private static void configurarAparencia() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
