package main;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HowToPlayGUI extends JDialog {
	private JTextArea ruleText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HowToPlayGUI dialog = new HowToPlayGUI();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public HowToPlayGUI() {
		setBounds(100, 100, 580, 416);
		getContentPane().setLayout(null);
		getContentPane().add(getRuleText());

	}
	private JTextArea getRuleText() {
		if (ruleText == null) {
			ruleText = new JTextArea();
			ruleText.setText("Yamb is a simple game where the goal is to get a bigger total sum than your opponent. Each column is filled in respect to certain rules, whereas the sum of each row is calculated differently.");
			ruleText.setLineWrap(true);
			ruleText.setWrapStyleWord(true);
			ruleText.setOpaque(false);
			ruleText.setEditable(false);
			ruleText.setBounds(10, 11, 544, 355);
		}
		return ruleText;
	}
}
