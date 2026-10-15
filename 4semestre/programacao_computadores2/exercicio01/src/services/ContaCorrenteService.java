package services;

import db.ContaDAO;
import exception.ContaExistenteException;
import exception.SaldoInsuficienteException;
import exception.ValorNegativoException;
import java.sql.SQLException;
import model.ContaCorrente;

import java.util.ArrayList;
import java.util.List;

public class ContaCorrenteService {

    List<ContaCorrente> contas = new ArrayList<>();
    ContaDAO dao = new ContaDAO();

    public void carregarDados() throws SQLException {
        contas = dao.listar();
    }
    
    public ContaCorrente buscarConta(int numero) {

        for (ContaCorrente conta : contas) {
            if (conta.getNumero() == numero) {
                return conta;
            }
        }

        System.out.println("Conta não encontrada.\n");
        return null;
    }

    public void sacar(int numero, double valor)
            throws SaldoInsuficienteException, ValorNegativoException, SQLException {

        ContaCorrente conta = buscarConta(numero);

        if (conta != null) {
            conta.sacar(valor);

            dao.atualizarSaldo(conta.getNumero(), conta.getSaldo());

            System.out.println(
                "Saque realizado com sucesso! Novo saldo: "
                + conta.getSaldo()
            );
        }
    }

    public void depositar(int numero, double valor)
            throws ValorNegativoException, SQLException {

        ContaCorrente conta = buscarConta(numero);

        if (conta != null) {
            conta.depositar(valor);

            dao.atualizarSaldo(conta.getNumero(), conta.getSaldo());

            System.out.println(
                "Depósito realizado com sucesso! Novo saldo: "
                + conta.getSaldo()
            );
        }
    }

    public void adicionarConta(ContaCorrente conta)
            throws ContaExistenteException, SQLException {

        if (buscarConta(conta.getNumero()) != null) {
            throw new ContaExistenteException();
        }

        dao.inserir(conta);
        contas.add(conta);

        System.out.println("Conta adicionada com sucesso.\n");
    }

    public boolean atualizarConta(int numero, ContaCorrente novaConta)
            throws SQLException {

        for (int i = 0; i < contas.size(); i++) {
            if (contas.get(i).getNumero() == numero) {
                dao.atualizarSaldo(numero, novaConta.getSaldo());

                contas.set(i, novaConta);

                System.out.println("Conta atualizada com sucesso.\n");
                return true;
            }
        }

        System.out.println("Conta não encontrada.\n");
        return false;
    }

    public boolean removerConta(int numero) throws SQLException {
        ContaCorrente conta = buscarConta(numero);

        if (conta != null) {
            dao.remover(numero);
            contas.remove(conta);

            System.out.println("Conta removida com sucesso.\n");
            return true;
        }

        System.out.println("Conta não encontrada.");
        return false;
    }

    public void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.\n");
            return;
        }

        for (ContaCorrente conta : contas) {
            conta.imprimirDados();
        }
    }

    public List<ContaCorrente> getContas() {
        return contas;
    }
    
    public void transferir(int numeroOrigem, int numeroDestino, double valor) 
            throws SaldoInsuficienteException, ValorNegativoException, SQLException {
        
        ContaCorrente contaOrigem = buscarConta(numeroOrigem);
        ContaCorrente contaDestino = buscarConta(numeroDestino);

        if (contaOrigem == null || contaDestino == null) {
            throw new Error("Conta não encontrada.");
        }

        if (valor <= 0) {
            throw new ValorNegativoException();
        }
        
        if (contaOrigem.getSaldo() < valor) {
            throw new SaldoInsuficienteException();
        }

        boolean sucesso = dao.transferir(numeroOrigem, numeroDestino, valor);

        if (sucesso) {
            contaOrigem.sacar(valor);  
            contaDestino.depositar(valor);
        } else {
            throw new Error("Falha na transferência.");
        }
    }
}
