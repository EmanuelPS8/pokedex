package view;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class InitialFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InitialFrame frame = new InitialFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InitialFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 860, 507);
		contentPane = new JPanel( ) {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img = Toolkit.getDefaultToolkit().getImage(InitialFrame.class.getResource("/images/680756.jpg"));
				g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
			
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("GAME START");
		btnStart.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		btnStart.setContentAreaFilled(false);   // remove a área de fundo
		btnStart.setBorderPainted(false);       // remove borda
		btnStart.setOpaque(false);              // garante transparência real
		btnStart.setForeground(Color.WHITE); 

		btnStart.setBounds(369, 270, 120, 23);
		contentPane.add(btnStart);
		
		btnStart.addActionListener(e -> {
		    new MainFrame().setVisible(true);
		    dispose(); // fecha a janela atual
		});


	}

}
