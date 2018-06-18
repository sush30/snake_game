package game;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainClass {

	public static void main(String[] args) {
		JFrame frame=new JFrame("Snake");
		frame.setContentPane(new GamePanel());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(GamePanel.width,GamePanel.height));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
