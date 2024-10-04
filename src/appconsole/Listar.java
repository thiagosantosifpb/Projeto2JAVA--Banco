package appconsole;

import modelo.Conta;
import modelo.Correntista;
import repositorio.Repositorio;

public class Listar {

    private Repositorio repositorio;

    public Listar() {
        repositorio = new Repositorio();

        try {
            System.out.println("\n--------- Listagem de Correntistas -----");
            for (Correntista c : repositorio.getCorrentistas()) {
                System.out.println(c);
                
                for (Conta conta : c.getContas()) {
                    System.out.println("  - Conta ID: " + conta.getId() + ", Saldo: " + conta.getSaldo());
                }
            }

            System.out.println("\n--------- Listagem de Contas -----");
            for (Conta ct : repositorio.getContas()) {
                System.out.println("Conta ID: " + ct.getId() + ", Data: " + ct.getData() + ", Saldo: " + ct.getSaldo());
            }

        } catch (Exception e) {
            System.out.println("---> " + e.getMessage());
        }   
    }

    public static void main(String[] args) {
        new Listar();
    }
}
