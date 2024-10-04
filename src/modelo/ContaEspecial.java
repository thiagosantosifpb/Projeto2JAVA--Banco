package modelo;

public class ContaEspecial extends Conta {
    protected double limite;

    // Construtor da ContaEspecial
    public ContaEspecial(int id, String data, double saldo, double limite, Correntista titular) {
        super(id, data, saldo, titular); // Chamando o construtor da classe base Conta
        this.limite = limite;
    }
    @Override
    public boolean debitar(double valor) {
        if (valor > 0 && (getSaldo() + limite) >= valor) {
            super.debitar(valor); // Chama o método debitar da classe pai
            return true; // Indica que o débito foi bem-sucedido
        } else {
            System.out.println("Saldo insuficiente ou limite excedido para débito.");
            return false; // Indica que o débito falhou
        }
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    @Override
    public String toString() {
        return "Conta Especial ID: " + getId() + 
               ", Saldo: R$ " + getSaldo() + 
               ", Limite: R$ " + limite + 
               ", Data de Abertura: " + getData();
    }
}
