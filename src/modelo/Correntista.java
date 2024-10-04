package modelo;

import java.util.ArrayList;

public class Correntista {
    private String cpf;
    private String nome;
    private String senha;
    private ArrayList<Conta> contas;

    public Correntista(String cpf, String nome, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.senha = senha;
        this.contas = new ArrayList<>();
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public ArrayList<Conta> getContas() {
        return contas;
    }

    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    public void removerConta(Conta conta) {
        contas.remove(conta);
    }

    public ArrayList<Integer> getIdsContas() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Conta conta : contas) {
            ids.add(conta.getId());
        }
        return ids;
    }
}
