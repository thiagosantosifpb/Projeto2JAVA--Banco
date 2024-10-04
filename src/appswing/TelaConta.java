package appswing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Conta;
import modelo.ContaEspecial;
import regras_negocio.Fachada;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaConta {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public TelaConta() {
        initialize();
        carregarContas();  // Chama o método para carregar as contas salvas
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gerenciamento de Conta");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Tipo", "Saldo"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);

        JButton btnCriarSimples = new JButton("Criar Conta Simples");
        btnCriarSimples.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = JOptionPane.showInputDialog(frame, "Digite o CPF do correntista:");
                try {
                    int id = Fachada.criarContaSimples(cpf);
                    model.addRow(new Object[]{id, "Simples", 0.0});
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnCriarSimples);

        JButton btnCriarEspecial = new JButton("Criar Conta Especial");
        btnCriarEspecial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cpf = JOptionPane.showInputDialog(frame, "Digite o CPF do correntista:");
                String limiteStr = JOptionPane.showInputDialog(frame, "Digite o limite da conta especial:");
                if (limiteStr != null && !limiteStr.trim().isEmpty()) {
                    try {
                        double limite = Double.parseDouble(limiteStr);
                        int id = Fachada.criarContaEspecial(cpf, limite);
                        model.addRow(new Object[]{id, "Especial", 0.0});
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(frame, "O limite deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        panel.add(btnCriarEspecial);

        JButton btnAdicionarCotitular = new JButton("Adicionar Cotitular");
        btnAdicionarCotitular.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) model.getValueAt(selectedRow, 0);
                    String cpf = JOptionPane.showInputDialog(frame, "Digite o CPF do cotitular:");
                    try {
                        Fachada.inserirCorrentistaConta(id, cpf);
                        JOptionPane.showMessageDialog(frame, "Cotitular adicionado com sucesso.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecione uma conta para adicionar cotitular.");
                }
            }
        });
        panel.add(btnAdicionarCotitular);

        JButton btnApagar = new JButton("Apagar Conta");
        btnApagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) model.getValueAt(selectedRow, 0);
                    try {
                        Fachada.apagarConta(id);
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(frame, "Conta apagada com sucesso.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Selecione uma conta.");
                }
            }
        });
        panel.add(btnApagar);

        JButton btnAtualizar = new JButton("Atualizar Saldos");
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    atualizarSaldos();
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(frame, e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnAtualizar);

        frame.setVisible(true);
    }


    // Método para atualizar os saldos na tabela
    public void atualizarSaldos() throws Exception {
        for (int i = 0; i < model.getRowCount(); i++) {
            int id = (int) model.getValueAt(i, 0);
            double saldo = Fachada.consultarSaldo(id);
            model.setValueAt(saldo, i, 2);
        }
    }

    // Método para carregar as contas salvas no repositório
    private void carregarContas() {
        try {
            for (Conta conta : Fachada.listarContas()) {
                String tipo = (conta instanceof ContaEspecial) ? "Especial" : "Simples";
                model.addRow(new Object[]{conta.getId(), tipo, conta.getSaldo()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Erro ao carregar as contas", JOptionPane.ERROR_MESSAGE);
        }
    }
}
