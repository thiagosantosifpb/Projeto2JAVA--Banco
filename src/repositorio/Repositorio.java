package repositorio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Conta;
import modelo.ContaEspecial;
import modelo.Correntista;

public class Repositorio {
    private ArrayList<Correntista> correntistas = new ArrayList<>();
    private ArrayList<Conta> contas = new ArrayList<>();

    // Caminhos relativos para os arquivos
    private static final String PATH_CONTAS = "src/Contas.csv";
    private static final String PATH_CORRENTISTAS = "src/Correntistas.csv";

    public Repositorio() {
        carregarObjetos();
    }

    public void adicionar(Correntista c) {
        correntistas.add(c);
        salvarCorrentistas(); // Salva o correntista no arquivo CSV
    }

    public void remover(Correntista c) {
        correntistas.remove(c);
        salvarCorrentistas(); // Atualiza o arquivo após a remoção
    }

    public Correntista localizarCorrentista(String cpf) {
        for (Correntista c : correntistas) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }

    public void adicionar(Conta ct) {
        contas.add(ct);
        salvarContas(); // Salva a conta no arquivo CSV
    }

    public void remover(Conta ct) {
        contas.remove(ct);
        salvarContas(); // Atualiza o arquivo após a remoção
    }

    public void removerConta(int id) {
        Conta conta = localizarConta(id);
        if (conta != null) {
            contas.remove(conta);
            salvarContas(); // Atualiza o arquivo após a remoção
        }
    }

    public Conta localizarConta(int id) {
        for (Conta ct : contas) {
            if (ct.getId() == id) {
                return ct;
            }
        }
        return null;
    }

    public ArrayList<Correntista> getCorrentistas() {
        return correntistas;
    }

    public ArrayList<Conta> getContas() {
        return contas;
    }

    public int getTotalCorrentistas() {
        return correntistas.size();
    }

    public int getTotalContas() {
        return contas.size();
    }

    public int gerarIdConta() {
        if (contas.isEmpty()) {
            return 1;
        } else {
            Conta ultima = contas.get(contas.size() - 1);
            return ultima.getId() + 1;
        }
    }

    public void carregarObjetos() {
        // Carregar para o repositório os objetos dos arquivos CSV
        File f1 = new File(PATH_CONTAS);
        File f2 = new File(PATH_CORRENTISTAS);

        try {
            if (!f1.exists() || !f2.exists()) {
                // Criando arquivos vazios
                if (!f1.exists()) new FileWriter(f1).close();
                if (!f2.exists()) new FileWriter(f2).close();
                return;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Criação dos arquivos vazios: " + ex.getMessage());
        }

        // Carregar contas
        try (Scanner arquivo1 = new Scanner(f1)) {
            while (arquivo1.hasNextLine()) {
                String linha = arquivo1.nextLine().trim();
                String[] partes = linha.split(";");

                // Verificando se o número correto de partes foi obtido
                if (partes.length >= 4) {
                    String tipoConta = partes[0];
                    int id = Integer.parseInt(partes[1]);
                    String data = partes[2];
                    double saldo = Double.parseDouble(partes[3]);

                    // Verifica se a conta é especial
                    if (tipoConta.equals("ContaEspecial") && partes.length == 5) {
                        double limite = Double.parseDouble(partes[4]);
                        ContaEspecial conta = new ContaEspecial(id, data, saldo, limite, null);
                        this.adicionar(conta);
                    } else if (tipoConta.equals("Conta")) {
                        Conta conta = new Conta(id, data, saldo, null);
                        this.adicionar(conta);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Carregar correntistas
        try (Scanner arquivo2 = new Scanner(f2)) {
            while (arquivo2.hasNextLine()) {
                String linha = arquivo2.nextLine().trim();
                String[] partes = linha.split(";");

                if (partes.length >= 3) {
                    String cpf = partes[0];
                    String nome = partes[1];
                    String senha = partes[2];

                    // Crie o correntista sem chamar salvarCorrentistas
                    Correntista correntista = new Correntista(cpf, nome, senha);
                    this.correntistas.add(correntista); // Adicione diretamente ao repositório

                    // Carregar IDs das contas associadas
                    if (partes.length > 3) {
                        String ids = partes[3]; // IDs das contas associadas ao correntista, separados por vírgula
                        for (String idConta : ids.split(",")) {
                            Conta conta = this.localizarConta(Integer.parseInt(idConta));
                            if (conta != null) {
                                correntista.adicionarConta(conta); // Associa a conta ao correntista
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler arquivo de correntistas: " + ex.getMessage());
        }
    }

    public void salvarObjetos() {
        salvarCorrentistas();
        salvarContas();
    }

    public void salvarContas() {
        // Gravar as contas nos arquivos CSV
        try (FileWriter arquivo1 = new FileWriter(PATH_CONTAS)) {
            for (Conta conta : contas) {
                String linha = (conta instanceof ContaEspecial ? "ContaEspecial" : "Conta") + ";" +
                               conta.getId() + ";" + conta.getData() + ";" + conta.getSaldo();
                if (conta instanceof ContaEspecial) {
                    linha += ";" + ((ContaEspecial) conta).getLimite();
                }
                arquivo1.write(linha + "\n");
            }
        } catch (Exception e) {
            throw new RuntimeException("Problema na criação do arquivo de contas: " + e.getMessage());
        }
    }

    public void salvarCorrentistas() {
        // Gravar os correntistas nos arquivos CSV
        try (FileWriter arquivo2 = new FileWriter(PATH_CORRENTISTAS)) {
            for (Correntista correntista : correntistas) {
                ArrayList<String> listaIds = new ArrayList<>();
                for (Conta conta : correntista.getContas()) {
                    listaIds.add(String.valueOf(conta.getId()));  // Adiciona o ID da conta na lista
                }
                String listaId = String.join(",", listaIds);  // Converte a lista de IDs em uma única string separada por vírgula
                arquivo2.write(correntista.getCpf() + ";" + correntista.getNome() + ";" + correntista.getSenha() + ";" + listaId + "\n");
            }
        } catch (Exception e) {
            throw new RuntimeException("Problema na criação do arquivo de correntistas: " + e.getMessage());
        }
    }
}
