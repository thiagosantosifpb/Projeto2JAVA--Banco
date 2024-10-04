package appswing;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TelaPrincipal {
    private JFrame frame;
    private JMenu mnCorrentista;
    private JMenu mnConta;
    private JMenu mnCaixa;
    private JLabel label;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaPrincipal window = new TelaPrincipal();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaPrincipal() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Banco - Sistema de Gerenciamento");
        frame.setBounds(100, 100, 450, 363);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        label = new JLabel("");
        label.setFont(new Font("Tahoma", Font.PLAIN, 26));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText("Inicializando...");
        label.setBounds(0, 0, 450, 313);

        // Caminho atualizado para a imagem
        // Caminho relativo para a imagem
        final String PATH_IMAGEM = "src/arquivos/ifpb-joao-pessoa.jpg";

        ImageIcon imagem = new ImageIcon(PATH_IMAGEM);
        if (imagem.getIconWidth() == -1) {
            System.err.println("Imagem não encontrada: " + imagem.getDescription());
        }

        label.setIcon(imagem);
        frame.getContentPane().add(label);
        frame.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        mnCorrentista = new JMenu("Correntista");
        mnCorrentista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaCorrentista(); 
            }
        });
        menuBar.add(mnCorrentista);

        mnConta = new JMenu("Conta");
        mnConta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaConta(); 
            }
        });
        menuBar.add(mnConta);

        mnCaixa = new JMenu("Caixa");
        mnCaixa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new TelaCaixa(); 
            }
        });
        menuBar.add(mnCaixa);
    }
}
