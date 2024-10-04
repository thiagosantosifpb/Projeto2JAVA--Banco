package appswing;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import regras_negocio.Fachada;

public class TelaCaixa {
    public JFrame frame;
    public TelaCaixa() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Caixa - Sistema Bancário");
        frame.setBounds(100, 100, 450, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("Operações do Caixa");
        label.setFont(new Font("Tahoma", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 450, 40);
        frame.getContentPane().add(label);

 

        JButton btnCreditar = new JButton("Creditar");
        btnCreditar.setBounds(150, 80, 150, 30);
        btnCreditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(frame, "Digite o ID da conta:");
                String valorStr = JOptionPane.showInputDialog(frame, "Digite o valor a creditar:");

                if (idStr == null || valorStr == null || idStr.isEmpty() || valorStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Os campos não podem ficar vazios", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int id = Integer.parseInt(idStr);
                    double valor = Double.parseDouble(valorStr);
                    Fachada.creditarValor(id, valor);
                    JOptionPane.showMessageDialog(frame, "R$ " + valor + " creditado com sucesso na conta ID " + id);

            
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Entrada inválida para o ID ou valor", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.getContentPane().add(btnCreditar);

        JButton btnDebitar = new JButton("Debitar");
        btnDebitar.setBounds(150, 120, 150, 30);
        btnDebitar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = JOptionPane.showInputDialog(frame, "Digite o ID da conta:");
                String cpf = JOptionPane.showInputDialog(frame, "Digite o CPF do correntista:");
                String senha = JOptionPane.showInputDialog(frame, "Digite a senha do correntista:");
                String valorStr = JOptionPane.showInputDialog(frame, "Digite o valor a debitar:");

                if (idStr == null || valorStr == null || idStr.isEmpty() || valorStr.isEmpty() || cpf == null || senha == null || cpf.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Os campos não podem ficar vazios", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int id = Integer.parseInt(idStr);
                    double valor = Double.parseDouble(valorStr);
                    Fachada.debitarValor(id, cpf, senha, valor);
                    JOptionPane.showMessageDialog(frame, "R$ " + valor + " debitado com sucesso da conta ID " + id);

               
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Entrada inválida para o ID ou valor", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.getContentPane().add(btnDebitar);

        JButton btnTransferir = new JButton("Transferir");
        btnTransferir.setBounds(150, 160, 150, 30);
        btnTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idOrigemStr = JOptionPane.showInputDialog(frame, "Digite o ID da conta de origem:");
                String idDestinoStr = JOptionPane.showInputDialog(frame, "Digite o ID da conta de destino:");
                String valorStr = JOptionPane.showInputDialog(frame, "Digite o valor a transferir:");

                if (idOrigemStr == null || idDestinoStr == null || valorStr == null || idOrigemStr.isEmpty() || idDestinoStr.isEmpty() || valorStr.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Os campos não podem ficar vazios", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int idOrigem = Integer.parseInt(idOrigemStr);
                    int idDestino = Integer.parseInt(idDestinoStr);
                    double valor = Double.parseDouble(valorStr);
                    Fachada.transferirValores(idOrigem, idDestino, valor);
                    JOptionPane.showMessageDialog(frame, "R$ " + valor + " transferido com sucesso da conta ID " + idOrigem + " para a conta ID " + idDestino);

                
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Entrada inválida para o ID ou valor", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.getContentPane().add(btnTransferir);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setBounds(150, 200, 150, 30);
        btnFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.getContentPane().add(btnFechar);

        frame.setVisible(true);
    }
}
