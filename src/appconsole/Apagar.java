package appconsole;

import modelo.Correntista;
import modelo.Conta;
import repositorio.Repositorio;

public class Apagar {

    private Repositorio repositorio;

    public Apagar() {
        repositorio = new Repositorio();

        try {
            // Supondo que já existam correntistas e contas no repositório
            Correntista correntistaParaRemover = repositorio.localizarCorrentista("10987654321");
            if (correntistaParaRemover != null) {
                repositorio.remover(correntistaParaRemover);
                System.out.println("Apagou correntista: Maria");
            } else {
                System.out.println("Correntista não encontrado: Maria");
            }

            // Supondo que já existam contas e desejamos apagar uma conta específica
            Conta contaParaRemover = repositorio.localizarConta(1); // ID da conta a ser removida
            if (contaParaRemover != null) {
                repositorio.remover(contaParaRemover);
                System.out.println("Apagou conta com ID: 1");
            } else {
                System.out.println("Conta não encontrada com ID: 1");
            }

        } catch (Exception e) {
            System.out.println("---> " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Apagar();
    }
}
