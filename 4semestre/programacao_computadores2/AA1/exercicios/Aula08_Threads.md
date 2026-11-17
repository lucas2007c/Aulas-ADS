# Aula 08 — Threads e Concorrência

## O que o desafio pede

Simular atendimentos bancários acontecendo **ao mesmo tempo**, onde cada atendimento é executado por uma **thread** separada.

---

## Conceito principal: Runnable + Thread

Em Java, para executar uma tarefa em paralelo você precisa de duas coisas:

- **Runnable** → define *o que* a thread vai fazer (a tarefa em si)
- **Thread** → executa a tarefa em um fluxo separado

```
Runnable  =  a tarefa (o trabalho)
Thread    =  o trabalhador que executa a tarefa
```

---

## Estrutura mínima

### 1. Crie a classe que representa a tarefa (implements Runnable)

```java
public class AtendimentoBancario implements Runnable {

    private final int numeroConta;

    public AtendimentoBancario(int numeroConta) {
        this.numeroConta = numeroConta;
    }

    @Override
    public void run() {
        // tudo aqui dentro roda na thread separada
        String nome = Thread.currentThread().getName();

        System.out.println("[" + nome + "] Início - Conta: " + numeroConta);

        try {
            Thread.sleep(1000); // simula tempo de atendimento (1 segundo)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[" + nome + "] Fim - Conta: " + numeroConta);
    }
}
```

**Pontos-chave:**
- `implements Runnable` → obriga a implementar o método `run()`
- `Thread.currentThread().getName()` → pega o nome da thread atual
- `Thread.sleep(1000)` → pausa a thread por 1000 ms (1 segundo)
- O `InterruptedException` SEMPRE precisa de try-catch quando usa sleep

---

### 2. No main, crie as threads e execute

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {

        // 1. Cria as tarefas (Runnable)
        AtendimentoBancario a1 = new AtendimentoBancario(1001);
        AtendimentoBancario a2 = new AtendimentoBancario(1002);
        AtendimentoBancario a3 = new AtendimentoBancario(1003);

        // 2. Cria as threads, passando a tarefa e um nome
        Thread t1 = new Thread(a1, "Atendimento-1");
        Thread t2 = new Thread(a2, "Atendimento-2");
        Thread t3 = new Thread(a3, "Atendimento-3");

        // 3. Inicia as threads (NÃO chame run() diretamente!)
        t1.start();
        t2.start();
        t3.start();

        // 4. Aguarda todas terminarem antes de continuar
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Todos os atendimentos concluídos.");
    }
}
```

---

## Por que start() e não run()?

| `run()` | `start()` |
|---|---|
| Executa na thread atual (sequencial) | Cria uma nova thread (paralelo) |
| Não tem concorrência | Tem concorrência de verdade |

---

## Por que join()?

Sem `join()`, o `main` imprime "Todos concluídos" antes das threads terminarem. O `join()` faz o `main` **esperar** cada thread terminar.

---

## O que observar ao executar

Execute mais de uma vez. A **ordem das mensagens muda** a cada execução — isso mostra que a ordem das threads não é garantida, quem o sistema operacional decide quem roda primeiro.

---

## Resumo para replicar

```
1. Crie uma classe implements Runnable
2. Implemente o método run() com a lógica da tarefa
3. Use Thread.sleep() para simular tempo
4. Use Thread.currentThread().getName() para identificar
5. No main: new Thread(runnable, "nome") → start() → join()
```
