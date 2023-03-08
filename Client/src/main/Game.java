package main;

import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;

public class Game {
	private JButton[] kocke;
	private JButton[] fields;
	private int potez;
	
	public Game(JButton[] kocke, JButton[] fields) {
		this.kocke = kocke;
		this.fields = fields;
		setup();
	}
	
	public void setup() {
		for (int i = 1; i < 16; i++) fields[i].setEnabled(false);
		for (int i = 32; i < 46; i++) fields[i].setEnabled(false);
	}
	
	public void baciKocke() {
		Random rand = new Random();
		for (int i = 0; i < 5; i++) if (kocke[i].isEnabled()) kocke[i].setText(Integer.toString(rand.nextInt(6) + 1));
	}
	
	public int[] getKocke() {
		int[] sveKocke = new int[7];
		sveKocke[1] = sveKocke[2] = sveKocke[3] = sveKocke[4] = sveKocke[5] = sveKocke[6] = 0;
		for (int i = 0; i < kocke.length; i++) sveKocke[Integer.parseInt(kocke[i].getText())]++;
		
		return sveKocke;
	}
	
	public void poljeBroja(int i) {
		int brojKocke = i;
		int[] sveKocke = getKocke();
		if (i >= 0 && i <= 5) brojKocke++;
		else if (i >= 16 && i <= 21) brojKocke -= 15;
		else if (i >= 32 && i <= 37) brojKocke -= 31;
		else brojKocke -= 48;
		fields[i].setText(Integer.toString(sveKocke[brojKocke]*brojKocke));
		//updatesum(i);
	}
	
	public void poljeMinMax(int i) {
		int sum = 0;
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) sum += sveKocke[j]*j;
		fields[i].setText(Integer.toString(sum));
		//updateSum(i);
	}
	
	public void poljeTriling(int i) {
		int sum = 0;
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 3) sum = sveKocke[j]*j + 20;
		}
		fields[i].setText(Integer.toString(sum));
		//updateSum(i);
	}
	
	public void poljeKenta(int i) {
		int sum = 0;
		int br = 0;
		int[] sveKocke = getKocke();
		
		for (int j = 1; j < sveKocke.length; j++) {
			if (br == 5) break;
			sum += sveKocke[j]*j;
			br++;
			if (sveKocke[j] == 0) br = 0;
		}
		
		if (br == 5) fields[i].setText(Integer.toString(sum));
		else fields[i].setText("0");
		//updateSum(i);
	}
	
	public void poljeFul(int i) {
		int[] sveKocke = getKocke();
		int dva = -1, tri = -1;
		
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 2) dva = j;
			if (sveKocke[j] == 3) tri = j;
		}
		
		if (dva == -1 || tri == -1) fields[i].setText("0");
		else fields[i].setText(Integer.toString(sveKocke[dva]*dva+sveKocke[tri]*tri+30));
	}
	
	public void poljePoker(int i) {
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 4) {
				fields[i].setText(Integer.toString(sveKocke[j]*j+40));
				return;
			}
		}
		
		fields[i].setText("0");
	}
	
	public void poljeJamb(int i) {
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 5) {
				fields[i].setText(Integer.toString(sveKocke[j]*j+50));
				return;
			}
		}
		
		fields[i].setText("0");
	}
	
	public void updateSum(int i) {
		
	}
}
