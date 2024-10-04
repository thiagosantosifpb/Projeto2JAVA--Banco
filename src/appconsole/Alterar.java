package appconsole;

import modelo.Correntista;
import modelo.Conta;
import repositorio.Repositorio;

public class Alterar {
    
    private Conta conta1; // Declarar conta1 como variável de instância
    private Conta conta2; // Declarar conta2 como variável de instância
    private Repositorio repositorio; // Variável de instância para o repositório

    public Alterar() {
        repositorio = new Repositorio();

        // Criar e adicionar correntistas
        try {
            Correntista correntista1 = new Correntista("12345678901", "João", "senha123");
            Correntista correntista2 = new Correntista("10987654321", "Maria", "senha456");
            Correntista correntista3 = new Correntista("12312312312", "José", "senha789");
            Correntista correntista4 = new Correntista("32132132132", "Ana", "senha101");

            repositorio.adicionar(correntista1);
            repositorio.adicionar(correntista2);
            repositorio.adicionar(correntista3);
            repositorio.adicionar(correntista4);
            System.out.println("Adicionados correntistas");

            // Remover correntista
            repositorio.remover(correntista4);
            System.out.println("Removido correntista: Ana");

            // Criar e adicionar contas
            conta1 = new Conta(repositorio.gerarIdConta(), "01/01/2024", 1000.0, correntista2);
            conta2 = new Conta(repositorio.gerarIdConta(), "02/01/2024", 2000.0, correntista1);
            repositorio.adicionar(conta1);
            repositorio.adicionar(conta2);
            correntista1.getContas().add(conta1);
            correntista2.getContas().add(conta2);
            System.out.println("Adicionadas contas aos correntistas");

        } catch (Exception e) {
            System.out.println("---> " + e.getMessage());
        }

        // Exemplo de operação em conta
        try {
            // conta1 está acessível aqui
            Conta contaParaOperar = repositorio.localizarConta(conta1.getId());
            if (contaParaOperar != null) {
                // operações na conta, como debitar ou creditar
                System.out.println("Operação na conta realizada com sucesso");
            }
        } catch (Exception e) {
            System.out.println("---> " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Alterar();
    }
}
