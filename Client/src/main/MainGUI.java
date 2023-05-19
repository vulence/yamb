package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;
import java.awt.Font;
import javax.swing.SwingConstants;

import org.w3c.dom.css.RGBColor;
import java.awt.Color;
import java.awt.event.MouseMotionAdapter;

public class MainGUI {

	private JFrame frame;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JButton baciKocke;
	private JButton kocka1;
	private JButton kocka2;
	private JButton kocka3;
	private JButton kocka4;
	private JButton kocka5;
	private JButton[] fields;
	private JButton[] kocke;
	private JLabel[] enemyFields;
	Game game;
	private JLabel finalSum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		prepareFields();
		prepareEnemyFields();
		for (int i = 0; i < 64; i++) {
			frame.getContentPane().add(getFields(fields[i], i));
			frame.getContentPane().add(enemyFields[i]);
		}
		
		frame.setBounds(100, 100, 833, 815);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(getFinalSum());
		frame.getContentPane().add(getBaciKocke());
		frame.getContentPane().add(getKocka1());
		frame.getContentPane().add(getKocka2());
		frame.getContentPane().add(getKocka3());
		frame.getContentPane().add(getKocka4());
		frame.getContentPane().add(getKocka5());
		frame.getContentPane().add(getLblNewLabel());
		frame.getContentPane().add(getLblNewLabel_1());
		
		prepareKocke();
		
		game = new Game(kocke, fields, enemyFields, baciKocke, finalSum);
		game.start();
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon("E:\\Programiranje\\Yamb\\Client\\resources\\yamb.PNG"));
			lblNewLabel.setBounds(10, 11, 314, 608);
		}
		return lblNewLabel;
	}
	private JLabel getLblNewLabel_1() {
		if (lblNewLabel_1 == null) {
			lblNewLabel_1 = new JLabel("");
			lblNewLabel_1.setIcon(new ImageIcon("E:\\Programiranje\\Yamb\\Client\\resources\\yamb.PNG"));
			lblNewLabel_1.setBounds(451, 11, 314, 608);
		}
		return lblNewLabel_1;
	}
	private JButton getKocka1() {
		if (kocka1 == null) {
			kocka1 = new JButton("");
			kocka1.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (kocka1.isEnabled() && !kocka1.getText().equals("")) kocka1.setEnabled(false);
					else kocka1.setEnabled(true);
				}
			});
			kocka1.setBounds(35, 673, 52, 47);
		}
		return kocka1;
	}
	private JButton getKocka2() {
		if (kocka2 == null) {
			kocka2 = new JButton("");
			kocka2.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (kocka2.isEnabled() && !kocka2.getText().equals("")) kocka2.setEnabled(false);
					else kocka2.setEnabled(true);
				}
			});
			kocka2.setBounds(105, 673, 52, 47);
		}
		return kocka2;
	}
	private JButton getKocka3() {
		if (kocka3 == null) {
			kocka3 = new JButton("");
			kocka3.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (kocka3.isEnabled() && !kocka3.getText().equals("")) kocka3.setEnabled(false);
					else kocka3.setEnabled(true);
				}
			});
			kocka3.setBounds(172, 673, 52, 47);
		}
		return kocka3;
	}
	private JButton getKocka4() {
		if (kocka4 == null) {
			kocka4 = new JButton("");
			kocka4.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (kocka4.isEnabled() && !kocka4.getText().equals("")) kocka4.setEnabled(false);
					else kocka4.setEnabled(true);
				}
			});
			kocka4.setBounds(235, 673, 52, 47);
		}
		return kocka4;
	}
	private JButton getKocka5() {
		if (kocka5 == null) {
			kocka5 = new JButton("");
			kocka5.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (kocka5.isEnabled() && !kocka5.getText().equals("")) kocka5.setEnabled(false);
					else kocka5.setEnabled(true);
				}
			});
			kocka5.setBounds(304, 673, 52, 47);
		}
		return kocka5;
	}
	private JButton getBaciKocke() {
		if (baciKocke == null) {
			baciKocke = new JButton("BACAJ");
			baciKocke.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					game.baciKocke(baciKocke);
				}
			});
			baciKocke.setBounds(148, 742, 89, 23);
		}
		return baciKocke;
	}
	private JButton getFields(JButton field, int i) {
		field.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if ((i >= 0 && i <= 5) || (i >= 16 && i <= 21) || (i >= 32 && i <= 37) || (i >= 48 && i <= 53)) game.poljeBroja(i);
				else if (Arrays.asList(7, 8, 23, 24, 39, 40, 55, 56).contains(i)) game.poljeMinMax(i);
				else if (Arrays.asList(10, 26, 42, 58).contains(i)) game.poljeTriling(i);
				else if (Arrays.asList(11, 27, 43, 59).contains(i)) game.poljeKenta(i);
				else if (Arrays.asList(12, 28, 44, 60).contains(i)) game.poljeFul(i);
				else if (Arrays.asList(13, 29, 45, 61).contains(i)) game.poljePoker(i);
				else game.poljeJamb(i);
			}
		});
		
		return field;
	}
	private void prepareFields() {
		if (fields == null) {
			int x = 81;
			int y = 60;
			fields = new JButton[64];
			for (int i = 0; i < 64; i++) {
				fields[i] = new JButton("fields" + String.valueOf(i));
				fields[i].setBounds(x, y, 39, 23);
				fields[i].setBorder(null);
				
				if (i == 6 || i == 9 || i == 15 || i == 22 || i == 25 || i == 31 || i == 38 || i == 41 || i == 47 || i == 54
						|| i == 57 || i == 63) {
					fields[i].setEnabled(false);
					fields[i].setText("0");
				}
				else fields[i].setText("");
				
				y += 35;
				if (i == 15 || i == 31 || i == 47) {
					x += 62;
					y = 60;
				}
			}
		}
	}
	private void prepareEnemyFields() {
		if (enemyFields == null) {
			int x = 521;
			int y = 64;
			enemyFields = new JLabel[64];
			for (int i = 0; i < 64; i++) {
				enemyFields[i] = new JLabel("enemyFields" + String.valueOf(i));
				enemyFields[i].setBounds(x, y, 46, 14);
				enemyFields[i].setFont(new Font("Tahoma", Font.PLAIN, 14));
				enemyFields[i].setHorizontalAlignment(SwingConstants.CENTER);
				enemyFields[i].setForeground(Color.RED);
				enemyFields[i].setBorder(null);
				
				if (i == 6 || i == 9 || i == 15 || i == 22 || i == 25 || i == 31 || i == 38 || i == 41 || i == 47 || i == 54
						|| i == 57 || i == 63) {
					enemyFields[i].setText("0");
				}
				else enemyFields[i].setText("");
				
				y += 35;
				if (i == 15 || i == 31 || i == 47) {
					x += 62;
					y = 64;
				}
			}
		}
	}
	private void prepareKocke() {
		kocke = new JButton[5];
		kocke[0] = kocka1;
		kocke[1] = kocka2;
		kocke[2] = kocka3;
		kocke[3] = kocka4;
		kocke[4] = kocka5;
	}

	private JLabel getFinalSum() {
		if (finalSum == null) {
			finalSum = new JLabel("Your total score is:");
			finalSum.setForeground(Color.RED);
			finalSum.setFont(new Font("Tahoma", Font.BOLD, 20));
			finalSum.setHorizontalAlignment(SwingConstants.CENTER);
			finalSum.setBounds(105, 204, 609, 169);
			finalSum.setVisible(false);
		}
		return finalSum;
	}
}
