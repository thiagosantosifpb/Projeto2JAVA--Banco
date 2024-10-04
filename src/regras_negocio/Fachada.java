package regras_negocio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import modelo.Correntista;
import modelo.Conta;
import modelo.ContaEspecial;
import repositorio.Repositorio;

public class Fachada {
    private static Repositorio repositorio = new Repositorio();
    private static final double LIMITE_CONTA_ESPECIAL_MINIMO = 50.0;

    // Requisito 1: Listar Correntistas
    public static ArrayList<Correntista> listarCorrentistas() {
        return repositorio.getCorrentistas();
    }

    // Requisito 2: Listar Contas
    public static ArrayList<Conta> listarContas() {
        return repositorio.getContas();
    }

    // Requisito 3: Criar Correntista
    public static void criarCorrentista(String cpf, String nome, String senha) throws Exception {
        cpf = cpf.trim();
        nome = nome.trim();
        senha = senha.trim();

        if (repositorio.localizarCorrentista(cpf) != null) {
            throw new Exception("Correntista já existe com o CPF: " + cpf);
        }

        if (senha.length() != 4 || !senha.matches("\\d+")) {
            throw new Exception("A senha deve ser numérica e conter 4 dígitos.");
        }

        Correntista novoCorrentista = new Correntista(cpf, nome, senha);
        repositorio.adicionar(novoCorrentista);
        System.out.println("Correntista criado com sucesso: " + nome);
    }

    // Requisito 4: Criar Conta Simples
    public static int criarContaSimples(String cpf) throws Exception {
        cpf = cpf.trim();

        Correntista correntista = repositorio.localizarCorrentista(cpf);
        if (correntista == null) {
            throw new Exception("Correntista não encontrado com o CPF: " + cpf);
        }

        // Verifique se o correntista já tem uma conta
        if (!correntista.getIdsContas().isEmpty()) {
            throw new Exception("Um correntista pode ser titular de no máximo uma conta.");
        }

        int novoId = repositorio.gerarIdConta();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        double saldoInicial = 0.0;

        // Criando uma nova conta com o correntista como titular
        Conta novaConta = new Conta(novoId, data, saldoInicial, correntista);
        repositorio.adicionar(novaConta);

        // Adicionando a nova conta à lista de contas do correntista
        correntista.adicionarConta(novaConta);
        System.out.println("Conta simples criada com sucesso para o correntista: " + correntista.getNome());
        return novoId; // Retornar o ID da nova conta
    }

    // Requisito 5: Criar Conta Especial
    public static int criarContaEspecial(String cpf, double limite) throws Exception {
        cpf = cpf.trim();

        Correntista correntista = repositorio.localizarCorrentista(cpf);
        if (correntista == null) {
            throw new Exception("Correntista não encontrado com o CPF: " + cpf);
        }

        // Verifique se o correntista já tem uma conta
        if (!correntista.getIdsContas().isEmpty()) {
            throw new Exception("Um correntista pode ser titular de no máximo uma conta.");
        }

        if (limite < LIMITE_CONTA_ESPECIAL_MINIMO) {
            throw new Exception("O limite de uma conta especial deve ser maior ou igual a R$ 50,00.");
        }

        int novoId = repositorio.gerarIdConta();
        String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        double saldoInicial = 0.0;

        // Criando uma nova conta especial com o correntista como titular
        ContaEspecial novaContaEspecial = new ContaEspecial(novoId, data, saldoInicial, limite, correntista);
        repositorio.adicionar(novaContaEspecial);

        // Adicionando a nova conta especial à lista de contas do correntista
        correntista.adicionarConta(novaContaEspecial);
        System.out.println("Conta especial criada com sucesso para o correntista: " + correntista.getNome());
        return novoId; // Retornar o ID da nova conta
    }

    // Requisito 6: Inserir Correntista na Conta
    public static void inserirCorrentistaConta(int id, String cpf) throws Exception {
        Correntista correntista = repositorio.localizarCorrentista(cpf);
        if (correntista == null) {
            throw new Exception("Correntista não encontrado com o CPF: " + cpf);
        }

        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }

        if (conta.getCorrentistas().size() >= 2) {
            throw new Exception("Não é possível adicionar um cotitular a uma conta que já tem dois correntistas.");
        }

        conta.adicionarCorrentista(correntista);
    }

    // Requisito 7: Remover Correntista da Conta
    public static void removerCorrentistaConta(int id, String cpf) throws Exception {
        Correntista correntista = repositorio.localizarCorrentista(cpf);
        if (correntista == null) {
            throw new Exception("Correntista não encontrado com o CPF: " + cpf);
        }

        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }

        if (conta.getTitular().equals(correntista)) {
            throw new Exception("O correntista titular não pode ser removido da conta, a não ser que a conta seja apagada.");
        }

        conta.getCorrentistas().remove(correntista);
        System.out.println("Correntista " + correntista.getNome() + " removido da conta com ID " + id);
    }

    // Requisito 8: Apagar Conta
    public static void apagarConta(int id) throws Exception {
        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }

        repositorio.remover(conta);
        System.out.println("Conta com ID " + id + " apagada com sucesso.");
    }

    // Requisito 9: Transferir Valores entre Contas
    public static void transferirValores(int idOrigem, int idDestino, double valor) throws Exception {
        Conta contaOrigem = repositorio.localizarConta(idOrigem);
        if (contaOrigem == null) {
            throw new Exception("Conta de origem não encontrada com o ID: " + idOrigem);
        }

        Conta contaDestino = repositorio.localizarConta(idDestino);
        if (contaDestino == null) {
            throw new Exception("Conta de destino não encontrada com o ID: " + idDestino);
        }

        contaOrigem.transferir(valor, contaDestino);
    }

    // Requisito 10: Debitar Valor
    public static void debitarValor(int id, String cpf, String senha, double valor) throws Exception {
        Correntista correntista = repositorio.localizarCorrentista(cpf);
        if (correntista == null) {
            throw new Exception("Correntista não encontrado com o CPF: " + cpf);
        }

        if (!correntista.getSenha().equals(senha)) {
            throw new Exception("Senha incorreta para o correntista: " + correntista.getNome());
        }

        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }

        // Verifica se a conta é especial e permite saldo negativo
        if (conta instanceof ContaEspecial) {
            if (valor > conta.getSaldo() + ((ContaEspecial) conta).getLimite()) {
                throw new Exception("Saldo insuficiente para débito na conta especial.");
            }
        } else {
            if (valor > conta.getSaldo()) {
                throw new Exception("Saldo insuficiente para débito.");
            }
        }

        // Debitar o valor
        conta.debitar(valor);
        System.out.println("R$ " + valor + " debitado da conta com ID " + id);
    }

    // Requisito 11: Creditar Valor
    public static void creditarValor(int id, double valor) throws Exception {
        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }

        conta.creditar(valor);
        System.out.println("R$ " + valor + " creditado na conta com ID " + id);
    }

    // Requisito 12: Atualizar Saldo
    public static void atualizarSaldo(int id, double novoSaldo) throws Exception {
        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }

        conta.setSaldo(novoSaldo);
        System.out.println("Saldo da conta com ID " + id + " atualizado para R$ " + novoSaldo);
    }

    // Requisito 13: Consultar Saldo
    public static double consultarSaldo(int id) throws Exception {
        Conta conta = repositorio.localizarConta(id);
        if (conta == null) {
            throw new Exception("Conta não encontrada com o ID: " + id);
        }
        return conta.getSaldo();
    }

    // Localizar Correntista pelo CPF
    public static Correntista localizarCorrentista(String cpf) {
        return repositorio.localizarCorrentista(cpf);
    }
 // Método para salvar objetos no repositório
    public static void salvarObjetos() {
        repositorio.salvarCorrentistas(); 
        repositorio.salvarContas(); 
    }
}
