package appconsole;

import modelo.Correntista;
import modelo.Conta;
import repositorio.Repositorio;

public class Cadastrar {

    private Repositorio repositorio;

    public Cadastrar() {
        repositorio = new Repositorio();

        try {
            // Criando contas
            Conta conta1 = new Conta(repositorio.gerarIdConta(), "24/10/2024", 100.0, null);
            Conta conta2 = new Conta(repositorio.gerarIdConta(), "01/11/2024", 200.0, null);
            repositorio.adicionar(conta1);
            repositorio.adicionar(conta2);
            
            // Criando correntistas
            Correntista correntista1 = new Correntista("10987654321", "João", "senha123"); 
            Correntista correntista2 = new Correntista("12345678901", "Maria", "senha456");
            
            // Associando contas aos correntistas
            correntista1.getContas().add(conta1);
            correntista2.getContas().add(conta2);
            
            // Adicionando correntistas ao repositório
            repositorio.adicionar(correntista1);
            repositorio.adicionar(correntista2);

            System.out.println("Cadastrou correntistas e contas com sucesso");
        } catch (Exception e) {
            System.out.println("---> " + e.getMessage());
        }		
    }

    public static void main(String[] args) {
        new Cadastrar();
    }
}
