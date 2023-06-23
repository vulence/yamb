package main;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends Thread {
	private JButton[] kocke;
	private JButton[] fields;
	private JButton baciKocke;
	private JLabel[] enemyFields;
	private JPanel endPanel;
	private JLabel finalSum, opponentFinalSum, winText;
	private int counter;
	private static final int PORT = 4444;
	private Socket socket;
	private ServerSocket serverSocket;
	private OutputStream out;
	private InputStream in;
	private PrintStream ps;
	private BufferedReader kb;
	private AtomicBoolean yourTurn, najava, end;
	private int najavaPolje;
	
	public Game(JButton[] kocke, JButton[] fields, JLabel[] enemyFields, JButton baciKocke, JPanel endPanel, JLabel finalSum, JLabel opponentFinalSum, JLabel winText) {
		this.counter = 0;
		this.kocke = kocke;
		this.fields = fields;
		this.enemyFields = enemyFields;
		this.endPanel = endPanel;
		this.finalSum = finalSum;
		this.opponentFinalSum = opponentFinalSum;
		this.winText = winText;
		this.baciKocke = baciKocke;
		this.yourTurn = new AtomicBoolean(false);
		this.najava = new AtomicBoolean(false);
		this.end = new AtomicBoolean(false);
		this.najavaPolje = -1;
		setup();
	}
	
	public void setup() {
		for (int i = 1; i < 16; i++) fields[i].setEnabled(false);
		for (int i = 32; i < 46; i++) fields[i].setEnabled(false);
	}
	
	public void baciKocke(JButton baciKocke) {
		if (najava.get()) disableFields();
			
		disableNajava();
		counter++;
			
		Random rand = new Random();
		for (int i = 0; i < 5; i++) if (kocke[i].isEnabled()) kocke[i].setText(Integer.toString(rand.nextInt(6) + 1));
			
		if (counter == 3) {
			disableKocke();
		}
	}
	
	public void clearAllFields() {
		for (int i = 0; i < fields.length; i++) fields[i].setText("");
		for (int i = 0; i < enemyFields.length; i++) enemyFields[i].setText("");
	}
	
	public int[] getKocke() {
		int[] sveKocke = new int[7];
		sveKocke[1] = sveKocke[2] = sveKocke[3] = sveKocke[4] = sveKocke[5] = sveKocke[6] = 0;
		for (int i = 0; i < kocke.length; i++) sveKocke[Integer.parseInt(kocke[i].getText())]++;
		
		
		return sveKocke;
	}
	
	public void poljeBroja(int i) {
		if (counter == 0) {
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int brojKocke = i;
		int[] sveKocke = getKocke();
		if (i >= 0 && i <= 5) brojKocke++;
		else if (i >= 16 && i <= 21) brojKocke -= 15;
		else if (i >= 32 && i <= 37) brojKocke -= 31;
		else brojKocke -= 47;
		fields[i].setText(Integer.toString(sveKocke[brojKocke]*brojKocke));
		sendData(i, sveKocke[brojKocke]*brojKocke, updateSum(i));
	}
	
	public void poljeMinMax(int i) {
		if (counter == 0) { 
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int sum = 0;
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) sum += sveKocke[j]*j;
		fields[i].setText(Integer.toString(sum));
		sendData(i, sum, updateSum(i));
	}
	
	public void poljeTriling(int i) {
		if (counter == 0) {
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int sum = 0;
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] >= 3) sum = 3*j + 20;
		}
		fields[i].setText(Integer.toString(sum));
		sendData(i, sum, updateSum(i));
	}
	
	public void poljeKenta(int i) {
		if (counter == 0) {
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int sum = 0;
		int br = 0;
		int[] sveKocke = getKocke();
		
		for (int j = 1; j < sveKocke.length; j++) {
			if (br == 5) break;
			sum += sveKocke[j]*j;
			br++;
			if (sveKocke[j] == 0) br = 0;
		}
		
		if (br == 5) {
			if (counter == 1) sum += 66;
			else if (counter == 2) sum += 56;
			else sum += 46;
			
			fields[i].setText(Integer.toString(sum));
			sendData(i, sum, updateSum(i));
		}
		else {
			fields[i].setText("0");
			sendData(i, 0, updateSum(i));
		}
	}
	
	public void poljeFul(int i) {
		if (counter == 0) {
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int[] sveKocke = getKocke();
		int dva = -1, tri = -1;
		
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 2) dva = j;
			if (sveKocke[j] == 3) tri = j;
		}
		
		if (dva == -1 || tri == -1) {
			fields[i].setText("0");
			sendData(i, 0, updateSum(i));
		}
		else {
			fields[i].setText(Integer.toString(sveKocke[dva]*dva+sveKocke[tri]*tri+30));
			sendData(i, sveKocke[dva]*dva+sveKocke[tri]*tri+30, updateSum(i));
		}
	}
	
	public void poljePoker(int i) {
		if (counter == 0) {
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 4) {
				fields[i].setText(Integer.toString(sveKocke[j]*j+40));
				sendData(i,sveKocke[j]*j+40, updateSum(i));
				return;
			}
		}
		
		fields[i].setText("0");
		sendData(i, 0, updateSum(i));
	}
	
	public void poljeJamb(int i) {
		if (counter == 0) {
			proveriNajavu(i);
			return;
		}
		
		if (!fields[i].isEnabled()) return;
		
		int[] sveKocke = getKocke();
		for (int j = 1; j < sveKocke.length; j++) {
			if (sveKocke[j] == 5) {
				fields[i].setText(Integer.toString(sveKocke[j]*j+50));
				sendData(i, sveKocke[j]*j+50, updateSum(i));
				return;
			}
		}
		
		fields[i].setText("0");
		sendData(i, 0, updateSum(i));
	}
	
	public void upisiNajavu() {
		fields[najavaPolje].setBorder(fields[1].getBorder());
		
		if (najavaPolje >= 48 && najavaPolje <= 53) poljeBroja(najavaPolje);
		else if (najavaPolje >= 54 && najavaPolje <= 56) poljeMinMax(najavaPolje);
		else if (najavaPolje == 58) poljeTriling(najavaPolje);
		else if (najavaPolje == 59) poljeKenta(najavaPolje);
		else if (najavaPolje == 60) poljeFul(najavaPolje);
		else if (najavaPolje == 61) poljePoker(najavaPolje);
		else poljeJamb(najavaPolje);
	}
	
	public void proveriNajavu(int i) {
		if (!yourTurn.get() || i < 48) return;
		
		if (najava.get()) {
			fields[najavaPolje].setBorder(fields[1].getBorder());
			najava.set(false);
		}
		
		if (i != najavaPolje) {
			najava.set(true);
			fields[i].setBorder(BorderFactory.createLineBorder(Color.RED, 4));
			najavaPolje = i;
		}
		else najavaPolje = -1;
	}
	
	public int[] updateSum(int i) {
		int sum = 0;
		int[] res = {-1, -1};
		
		if (i < 6) {
			for (int j = 0; j < 6; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			sum += sum >= 60 ? 30 : 0;
			
			fields[6].setText(Integer.toString(sum));
			res[0] = 6;
			res[1] = sum;
		}
		else if (i < 9) {
			for (int j =  7; j < 9; j++) {
				if (fields[j].getText().equals("")) return res;
			}
			
			sum = Integer.parseInt(fields[7].getText()) - Integer.parseInt(fields[8].getText());
			
			fields[9].setText(Integer.toString(sum));
			res[0] = 9;
			res[1] = sum;
		}
		else if (i < 15) {
			for (int j = 10; j < 15; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[15].setText(Integer.toString(sum));
			res[0] = 15;
			res[1] = sum;
		}
		else if (i < 22) {
			for (int j = 16; j < 22; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[22].setText(Integer.toString(sum));
			res[0] = 22;
			res[1] = sum;
		}
		else if (i < 25) {
			for (int j = 23; j < 25; j++) {
				if (fields[j].getText().equals("")) return res;
			}
			
			sum = Integer.parseInt(fields[23].getText()) - Integer.parseInt(fields[24].getText());
			
			fields[25].setText(Integer.toString(sum));
			res[0] = 25;
			res[1] = sum;
		}
		else if (i < 31) {
			for (int j = 26; j < 31; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[31].setText(Integer.toString(sum));
			res[0] = 31;
			res[1] = sum;
		}
		else if (i < 38) {
			for (int j = 32; j < 38; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[38].setText(Integer.toString(sum));
			res[0] = 38;
			res[1] = sum;
		}
		else if (i < 41) {
			for (int j = 39; j < 41; j++) {
				if (fields[j].getText().equals("")) return res;
			}
			
			sum = Integer.parseInt(fields[39].getText()) - Integer.parseInt(fields[40].getText());
			
			fields[41].setText(Integer.toString(sum));
			res[0] = 41;
			res[1] = sum;
		}
		else if (i < 47) {
			for (int j = 42; j < 47; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[47].setText(Integer.toString(sum));
			res[0] = 47;
			res[1] = sum;
		}
		else if (i < 54) {
			for (int j = 48; j < 54; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[54].setText(Integer.toString(sum));
			res[0] = 54;
			res[1] = sum;
		}
		else if (i < 57) {
			for (int j = 55; j < 57; j++) {
				if (fields[j].getText().equals("")) return res;
			}
			
			sum = Integer.parseInt(fields[55].getText()) - Integer.parseInt(fields[56].getText());
			
			fields[57].setText(Integer.toString(sum));
			res[0] = 57;
			res[1] = sum;
		}
		else {
			for (int j = 58; j < 63; j++) {
				if (fields[j].getText().equals("")) return res;
				
				sum += Integer.parseInt(fields[j].getText());
			}
			
			fields[63].setText(Integer.toString(sum));
			res[0] = 63;
			res[1] = sum;
		}
		
		return res;
	}
	
	private int getTotalSum() {
		int totalSum = 0;
		for (int i = 6; i < fields.length; i += 16) totalSum += Integer.parseInt(fields[i].getText());
		for (int i = 9; i < fields.length; i += 16) totalSum += Integer.parseInt(fields[i].getText());
		for (int i = 15; i < fields.length; i += 16) totalSum += Integer.parseInt(fields[i].getText());
		
		return totalSum;
	}
	
	private int getTotalEnemySum() {
		int totalSum = 0;
		for (int i = 6; i < enemyFields.length; i += 16) totalSum += Integer.parseInt(enemyFields[i].getText());
		for (int i = 9; i < enemyFields.length; i += 16) totalSum += Integer.parseInt(enemyFields[i].getText());
		for (int i = 15; i < enemyFields.length; i += 16) totalSum += Integer.parseInt(enemyFields[i].getText());
		
		return totalSum;
	}
	
	private void printEnd() {
		int yourSum = getTotalSum();
		int opponentSum = getTotalEnemySum();
		
		finalSum.setText("Your total score is: " + Integer.toString(yourSum));
		opponentFinalSum.setText("Your opponent's total score is: " + Integer.toString(opponentSum));
		
		if (yourSum > opponentSum) {
			winText.setForeground(Color.GREEN);
			winText.setText("YOU WIN!");
		}
		else if (yourSum < opponentSum) {
			winText.setForeground(Color.RED);
			winText.setText("YOU LOSE!");
		}
		else {
			winText.setForeground(Color.BLUE);
			winText.setText("It's a tie!");
		}
		
		endPanel.setVisible(true);
		while (true) {}
	}
	
	private void printWaiting() {
		finalSum.setText("");
		winText.setText("");
		opponentFinalSum.setText("Waiting for another player to join..");
		
		endPanel.setVisible(true);
	}
	
	// tries to connect to an existing client
	private boolean connect() {
		try {
			socket = new Socket("localhost", PORT);
			yourTurn.set(false);
			disableKocke();
			disableFields();
			clearKocke();
		} catch (IOException e) {
			e.printStackTrace();
			return false; // if doesn't succeed, returns false to start a server
		}
		
		return true;
	}
	
	private void startServer() {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void listener() {
		try {
			socket = serverSocket.accept();
			yourTurn.set(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void init() {
		if (!connect()) {
			startServer();
			
			printWaiting();
			
			listener();
		}
		
		endPanel.setVisible(false);
		
		try {
			in = socket.getInputStream();
			kb = new BufferedReader(new InputStreamReader(in));
			
			out = socket.getOutputStream();
			ps = new PrintStream(out, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		counter = 0;
	}
	
	private void sendData(int i, int val, int[] sum) {
		ps.println(i); // index polja koje se update
		ps.println(val); // vrednost koja treba da se nalazi polju
		ps.println(sum[0]); // indeks polja sume koje treba da se update
		ps.println(sum[1]); // vrednost polja sume
		
		yourTurn.set(false);
		
		clearAll();
	}
	
	private void clearAll() {
		counter = 0;
		if (najavaPolje != -1) fields[najavaPolje].setBorder(fields[1].getBorder());
		najavaPolje = -1;
		yourTurn.set(false);
		najava.set(false);
		disableKocke();
		disableFields();
		clearKocke();
	}
	
	public void checkEnd() {
		if (!yourTurn.get()) return;
		
		//if (!fields[0].isEnabled()) end.set(true);
			
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].isEnabled()) return;
		}
		
		end.set(true);
	}
	
	public boolean startNewGame() {
		if (yourTurn.get()) {
			clearAllFields();
			sendData(-2, 0, new int[] {0, 0});
			return true;
		}
		
		return false;
	}
	
	private void enableButtons() {
		baciKocke.setEnabled(true);
		
		for (int i = 0; i < 14; i++) {
			if (i == 0 && fields[i].getText().equals("")) {
				fields[i].setEnabled(true);
				break;
			}
			
			if (!fields[i].getText().equals("") && fields[i + 1].getText().equals("")) {
				fields[i + 1].setEnabled(true);
				break;
			}
		}
		
		for (int i = 16; i < 32; i++) {
			if (fields[i].getText().equals("")) fields[i].setEnabled(true);
		}
		
		for (int i = 46; i > 32; i--) {
			if (i == 46 && fields[i].getText().equals("")) {
				fields[i].setEnabled(true);
				break;
			}
			
			if (!fields[i].getText().equals("") && fields[i - 1].getText().equals("")) {
				fields[i - 1].setEnabled(true);
				break;
			}
		}
		
		for (int i = 48; i < fields.length; i++) {
			if (fields[i].getText().equals("")) fields[i].setEnabled(true);
		}
		
		for (int i = 0; i < kocke.length; i++) kocke[i].setEnabled(true);
	}
	
	private void disableFields() {
		for (int i = 0; i < fields.length; i++) if (i != najavaPolje) fields[i].setEnabled(false);
	}
	
	private void disableKocke() {
		baciKocke.setEnabled(false);
		for (int i = 0; i < kocke.length; i++) kocke[i].setEnabled(false);
	}
	
	private void clearKocke() {
		for (int i = 0; i < kocke.length; i++) kocke[i].setText("");
	}
	
	private void disableNajava() {
		for (int i = 48; i < 64; i++) if (i != najavaPolje) fields[i].setEnabled(false);
	}
	
	private void tick() {		
		String buf, value, sumValue;
		int index, sumIndex;
		
		if (!end.get()) {
			if (!yourTurn.get()) {
				try {
					buf = kb.readLine();
					index = Integer.parseInt(buf);
					value = kb.readLine();
					
					
					buf = kb.readLine();
					sumIndex = Integer.parseInt(buf);
					sumValue = kb.readLine();
					
					if (index == -2) {	// checks for new game
						clearAllFields();
					}
					else if (index != -1) {	// checks for end
						enemyFields[index].setText(value);
						if (sumIndex != -1) enemyFields[sumIndex].setText(sumValue);	// checks if sum field needs updating
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				yourTurn.set(true);
				enableButtons();
			}
			else {
				checkEnd();
				if (end.get()) {
					sendData(-1, -1, new int[] {-1, -1});
				}
			}
		}
		else {
			printEnd();
		}
	}
	
	public void run() {
		init();
		while (true) tick();
	}
}