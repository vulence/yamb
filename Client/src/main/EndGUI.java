package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class EndGUI extends JFrame {

	private JPanel contentPane;
	private JLabel label1;
	private JLabel resultLabel;
	private JLabel playerSum;
	private int totalSum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EndGUI frame = new EndGUI();
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
	public EndGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 642, 528);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLabel1());
		contentPane.add(getResultLabel());
		contentPane.add(getPlayerSum());
	}
	public EndGUI(int totalSum) {
		this();
		this.totalSum = totalSum;
		setLabels();
	}
	private JLabel getLabel1() {
		if (label1 == null) {
			label1 = new JLabel("Waiting for Player 2 to finish..");
			label1.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
			label1.setHorizontalAlignment(SwingConstants.CENTER);
			label1.setBounds(154, 151, 311, 80);
		}
		return label1;
	}
	private JLabel getResultLabel() {
		if (resultLabel == null) {
			resultLabel = new JLabel("New label");
			resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
			resultLabel.setBounds(203, 263, 187, 139);
		}
		return resultLabel;
	}
	private JLabel getPlayerSum() {
		if (playerSum == null) {
			playerSum = new JLabel("Your total sum is: ");
			playerSum.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
			playerSum.setHorizontalAlignment(SwingConstants.CENTER);
			playerSum.setBounds(203, 65, 199, 24);
		}
		return playerSum;
	}
	private void setLabels() {
		playerSum.setText(playerSum.getText() + Integer.toString(totalSum));
	}
}
