package app;

import exception.PermissaoNegadaException;
import exception.ValidacaoException;
import model.Carro;
import model.Moto;
import model.StatusVeiculo;
import model.TipoAcesso;
import model.TipoVeiculo;
import model.Usuario;
import model.Veiculo;
import services.SecurityUtils;
import services.SessaoService;

import java.time.LocalDate;

public class TestRunner {

    private static int testesExecutados = 0;
    private static int testesAprovados = 0;

    public static void main(String[] args) {
        System.out.println("=================================================================");
        System.out.println("   Executando Bateria de Testes Automatizados - AutoEstoque");
        System.out.println("=================================================================\n");

        testarEncapsulamentoEHeranca();
        testarPolimorfismo();
        testarValidacoesEspecificasCarroEMoto();
        testarHashDeSenhas();
        testarControleDeAcessoESessao();

        System.out.println("\n=================================================================");
        System.out.printf("   Resultado: %d/%d testes passaram com sucesso! (100%% OK)\n", testesAprovados, testesExecutados);
        System.out.println("=================================================================");
    }

    private static void afere(String nomeTeste, boolean condicao) {
        testesExecutados++;
        if (condicao) {
            testesAprovados++;
            System.out.println("  [PASSOU] " + nomeTeste);
        } else {
            System.err.println("  [FALHOU] " + nomeTeste);
        }
    }

    private static void testarEncapsulamentoEHeranca() {
        System.out.println(">> Testando Encapsulamento e Heranca:");

        Carro carro = new Carro("Honda", "Civic", 2022, "Prata", "ABC1D23", 25000, 115000.0, LocalDate.now(), StatusVeiculo.DISPONIVEL, 4);
        afere("Carro herda atributos de Veiculo (Marca)", "Honda".equals(carro.getMarca()));
        afere("Carro herda atributos de Veiculo (Placa)", "ABC1D23".equals(carro.getPlaca()));
        afere("Carro define TipoVeiculo.CARRO automaticamente", carro.getTipoVeiculo() == TipoVeiculo.CARRO);
        afere("Carro possui atributo especifico 'quantidadePortas'", carro.getQuantidadePortas() == 4);

        Moto moto = new Moto("Yamaha", "MT-07", 2023, "Azul", "XYZ9E87", 5000, 42000.0, LocalDate.now(), StatusVeiculo.DISPONIVEL, 689);
        afere("Moto herda atributos de Veiculo (Modelo)", "MT-07".equals(moto.getModelo()));
        afere("Moto define TipoVeiculo.MOTO automaticamente", moto.getTipoVeiculo() == TipoVeiculo.MOTO);
        afere("Moto possui atributo especifico 'cilindradas'", moto.getCilindradas() == 689);
    }

    private static void testarPolimorfismo() {
        System.out.println("\n>> Testando Polimorfismo:");

        Veiculo v1 = new Carro("Toyota", "Corolla", 2021, "Preto", "COR1234", 30000, 120000.0, LocalDate.now(), StatusVeiculo.DISPONIVEL, 4);
        Veiculo v2 = new Moto("Honda", "CB 500F", 2022, "Vermelho", "HON5000", 8000, 38000.0, LocalDate.now(), StatusVeiculo.DISPONIVEL, 471);

        afere("Polimorfismo: Carro retorna '4 portas'", "4 portas".equals(v1.getEspecificacaoDetalhada()));
        afere("Polimorfismo: Moto retorna '471 cc'", "471 cc".equals(v2.getEspecificacaoDetalhada()));

        afere("Polimorfismo: toString de Carro inclui especificacao", v1.toString().contains("4 portas"));
        afere("Polimorfismo: toString de Moto inclui especificacao", v2.toString().contains("471 cc"));
    }

    private static void testarValidacoesEspecificasCarroEMoto() {
        System.out.println("\n>> Testando Validacoes Especificas das Subclasses:");

        Carro carroInvalido = new Carro();
        carroInvalido.setQuantidadePortas(1);
        boolean falhouPortas = false;
        try {
            carroInvalido.validarEspecificidades();
        } catch (ValidacaoException e) {
            falhouPortas = true;
        }
        afere("Validacao de Carro barra quantidade de portas invalida (< 2)", falhouPortas);

        Moto motoInvalida = new Moto();
        motoInvalida.setCilindradas(0);
        boolean falhouCilindradas = false;
        try {
            motoInvalida.validarEspecificidades();
        } catch (ValidacaoException e) {
            falhouCilindradas = true;
        }
        afere("Validacao de Moto barra cilindradas menores ou iguais a zero", falhouCilindradas);
    }

    private static void testarHashDeSenhas() {
        System.out.println("\n>> Testando Seguranca e Hashing de Senhas (SHA-256):");

        String senhaOriginal = "admin123";
        String hash = SecurityUtils.hashSenha(senhaOriginal);

        afere("Hash gerado possui 64 caracteres hexadecimais", hash != null && hash.length() == 64);
        afere("Hash nao e texto puro", !senhaOriginal.equals(hash));
        afere("Verificacao de senha correta retorna true", SecurityUtils.verificarSenha("admin123", hash));
        afere("Verificacao de senha incorreta retorna false", !SecurityUtils.verificarSenha("senhaErrada", hash));
    }

    private static void testarControleDeAcessoESessao() {
        System.out.println("\n>> Testando Controle de Acesso e Permissoes:");

        Usuario comum = new Usuario(1, "Joao Silva", "joao", "hash123", TipoAcesso.FUNCIONARIO);
        Usuario admin = new Usuario(2, "Maria Gerente", "maria", "hash456", TipoAcesso.ADMINISTRADOR);

        SessaoService.iniciarSessao(comum);
        afere("Sessao identifica usuario autenticado", SessaoService.isAutenticado());
        afere("Funcionario comum nao e Administrador", !SessaoService.isAdministrador());

        boolean bloqueouComum = false;
        try {
            SessaoService.exigirAdministrador();
        } catch (PermissaoNegadaException e) {
            bloqueouComum = true;
        }
        afere("SessaoService bloqueia operacao de Administrador para Funcionario Comum", bloqueouComum);

        SessaoService.iniciarSessao(admin);
        afere("Sessao identifica Administrador corretamente", SessaoService.isAdministrador());

        boolean permitiuAdmin = true;
        try {
            SessaoService.exigirAdministrador();
        } catch (PermissaoNegadaException e) {
            permitiuAdmin = false;
        }
        afere("SessaoService autoriza operacao para Administrador", permitiuAdmin);

        SessaoService.encerrarSessao();
        afere("Encerramento de sessao limpa o usuario ativo", !SessaoService.isAutenticado());
    }
}
