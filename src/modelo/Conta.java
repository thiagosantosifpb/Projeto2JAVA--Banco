package modelo;

import java.util.ArrayList;

public class Conta {
    private int id;
    private String data;
    private double saldo;
    private ArrayList<Correntista> correntistas = new ArrayList<>();

    // Adicionando o titular diretamente
    public Conta(int id, String data, double saldo, Correntista titular) {
        this.id = id;
        this.data = data;
        this.saldo = saldo;
        this.adicionarCorrentista(titular); // Adiciona o titular automaticamente
    }

    // Creditar
    public void creditar(double valor) {
        saldo += valor;
    }

    // Debitar
    public boolean debitar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
            return true;
        }
        return false;
    }

    // Método para transferir valores para outra conta
    public boolean transferir(double valor, Conta contaDestino) throws Exception {
        // Verifica se o valor a ser transferido é válido
        if (valor <= 0) {
            throw new Exception("O valor da transferência deve ser positivo.");
        }

        // Verifica se há saldo suficiente para a transferência
        if (this.saldo >= valor) {
            // Debita o valor da conta de origem
            this.debitar(valor);
            // Credita o valor na conta de destino
            contaDestino.creditar(valor);
            return true;
        } else {
            throw new Exception("Saldo insuficiente para a transferência.");
        }
    }

    // Método para adicionar um correntista à lista
    public void adicionarCorrentista(Correntista correntista) {
        if (correntista != null && !correntistas.contains(correntista)) {
            correntistas.add(correntista);
            System.out.println("Correntista adicionado à conta: " + correntista.getNome());
        } else {
            System.out.println("Correntista inválido ou já existe na conta.");
        }
    }

    public Correntista getTitular() {
        // Retorna o primeiro correntista como titular
        return correntistas.isEmpty() ? null : correntistas.get(0);
    }

    @Override
    public String toString() {
        return "Conta ID: " + id +
               ", Saldo: R$ " + saldo +
               ", Data de Abertura: " + data;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    

    public ArrayList<Correntista> getCorrentistas() {
        return correntistas;
    }

    public void setCorrentistas(ArrayList<Correntista> correntistas) {
        this.correntistas = correntistas;
    }
}
