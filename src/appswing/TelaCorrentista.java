package appswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Correntista;
import regras_negocio.Fachada;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;

public class TelaCorrentista {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public TelaCorrentista() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gerenciamento de Correntistas");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"CPF", "Nome", "Senha", "IDs das Contas"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        JButton btnAdicionar = new JButton("Adicionar Correntista");
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = JOptionPane.showInputDialog(frame, "Digite o CPF do correntista:");
                String nome = JOptionPane.showInputDialog(frame, "Digite o nome do correntista:");
                String senha = JOptionPane.showInputDialog(frame, "Digite a senha do correntista:");

                if (cpf != null && nome != null && senha != null && !cpf.trim().isEmpty() && !nome.trim().isEmpty() && !senha.trim().isEmpty()) {
                    try {
                        Fachada.criarCorrentista(cpf, nome, senha);
                        carregarCorrentistasNaTabela(); // Atualiza a tabela com os correntistas
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnAdicionar);

        JButton btnMostrarContas = new JButton("Mostrar Contas");
        btnMostrarContas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String cpf = (String) model.getValueAt(selectedRow, 0);
                    Correntista correntista = Fachada.listarCorrentistas().stream()
                            .filter(c -> c.getCpf().equals(cpf))
                            .findFirst()
                            .orElse(null);
                    if (correntista != null) {
                        // Converte a lista de IDs para uma lista de Strings
                        String idsContas = correntista.getIdsContas().stream()
                                .map(String::valueOf) // Converte cada ID para String
                                .collect(Collectors.joining(", ")); // Junta os IDs em uma única String

                        JOptionPane.showMessageDialog(frame, "Contas do correntista CPF " + cpf + ": " + idsContas);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Correntista não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecione um correntista.");
                }
            }
        });
        panel.add(btnMostrarContas);

        JButton btnApagarConta = new JButton("Apagar Conta");
        btnApagarConta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String cpf = (String) model.getValueAt(selectedRow, 0);
                    Correntista correntista = Fachada.listarCorrentistas().stream()
                            .filter(c -> c.getCpf().equals(cpf))
                            .findFirst()
                            .orElse(null);

                    if (correntista != null) {
                        // Converte a lista de IDs em um array para mostrar no diálogo
                        String[] ids = correntista.getIdsContas().stream()
                                .map(String::valueOf)
                                .toArray(String[]::new);

                        // Exibe um diálogo para escolher a conta a ser apagada
                        String idStr = (String) JOptionPane.showInputDialog(frame, "Selecione o ID da conta a ser apagada:", "Apagar Conta", JOptionPane.QUESTION_MESSAGE, null, ids, ids.length > 0 ? ids[0] : null);

                        if (idStr != null) {
                            try {
                                int id = Integer.parseInt(idStr.trim());
                                Fachada.apagarConta(id); // Chama o método para apagar a conta
                                carregarCorrentistasNaTabela(); // Atualiza a tabela de correntistas
                                JOptionPane.showMessageDialog(frame, "Conta apagada com sucesso.");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Correntista não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecione um correntista.");
                }
            }
        });
        panel.add(btnApagarConta);

        carregarCorrentistasNaTabela(); // Carrega correntistas ao inicializar a tela

        frame.setVisible(true);
    }

    private void carregarCorrentistasNaTabela() {
        model.setRowCount(0); // Limpa a tabela antes de adicionar novos dados
        for (Correntista c : Fachada.listarCorrentistas()) { 
            String idsContas = c.getIdsContas().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", ")); // Junta os IDs em uma única String
            model.addRow(new Object[]{c.getCpf(), c.getNome(), c.getSenha(), idsContas});
        }
    }
}
