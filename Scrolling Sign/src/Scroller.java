import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Scroller {
	private int scrollTick = 224; //time between each scroll (ms) changed by slider
	private boolean working = false; //Prevent multiple scrolls at once
	private boolean loop = false; //Loop button

	public static void main(String[] args) {
		new Scroller().init();
	}

	@SuppressWarnings("unused")
	private void init() {
		JFrame f = new JFrame("Scroller");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(50, 50);

		int gridSize, squareSize;
		Grid g = new Grid(gridSize = 10, squareSize = 50);

		JPanel p = new JPanel();
		JSlider sli = new JSlider(1, 90);
		sli.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				scrollTick = (100 - sli.getValue()) * 4;
			}
		});

		JTextField tf = new JTextField(10);
		JButton s = new JButton("Scroll");
		JCheckBox b = new JCheckBox("Loop");
		s.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (loop) {
					//Button has "stop" text here, stop the loop
					loop = false;
					working = false;
					s.setText("Scroll");
					b.setSelected(false);
				} else {
					if (!tf.getText().equals("") && !working) {
						//Asynchronous since repaints and sleeps are done in a loop
						String text = tf.getText().trim();
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									if (b.isSelected()) { //Loop
										s.setText("Stop");
										loop = true;
										working = true;
										loop(g, text);
									} else {
										working = true;
										s.setText("Scrolling...");
										scroll(g, text);
										working = false;
										s.setText("Scroll");
									}
								} catch (IOException e) {
									tf.setText("File not found");
									working = false;
								}
							}
						}).start();
						tf.setText("");
					}
				}
			}
		});

		p.add(sli);
		p.add(s);
		p.add(tf);
		p.add(b);

		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.getContentPane().add(g);
		f.pack();
		f.setVisible(true);
	}

	private void scroll(Grid g, String str) throws IOException {
		char[] letters = str.toCharArray();

		//Get unique letters to minimize loading from files
		ArrayList<Character> al = new ArrayList<Character>();
		for (char letter: letters) {
			al.add(letter);
		}

		Set<Character> t = new LinkedHashSet<Character>(al);
		Character[] uniqueLetters = new Character[t.size()];
		t.toArray(uniqueLetters);

		//Grids that represent the letters to be printed
		HashMap<Character, Color[][]> letterGrids = new HashMap<Character, Color[][]>();

		int size = -1;
		for (int i = 0; i < uniqueLetters.length; i++) {
			String tmpCh = "" + uniqueLetters[i];

			//Special letter cases
			if (tmpCh.equals(" ")) {
				tmpCh = "spc"; //space filename
			}
			if (tmpCh.equals("?")) {
				tmpCh = "qm";
			}
			if (tmpCh.equals("|")) {
				tmpCh = "flag";
			}

			Color [][] tmp = Grid.readGrid("letters/" + tmpCh + ".txt");

			//Assume all letters are present on the same sized grid (which they should be)
			if (size == -1) {
				size = tmp.length;
			}

			letterGrids.put(uniqueLetters[i], tmp);
		}

		Color[][] strGrid = new Color[size * letters.length][size];

		//Create grid of all letters in sequence
		int longGridSize = 0; //pos in long array
		for (char letter: letters) {
			Color[][] tmp = letterGrids.get(letter); //less calls to get
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					strGrid[longGridSize][j] = tmp[i][j];
				}
				longGridSize++;
			}
		}

		/* Shift grid over
		 * 	The x coord is the only thing being changed here
		 * 	e.g. grid[x][y]
		 * 	Each iteration waits for SCROLL_TICK before shifting again 
		 */
		for (int shiftAmount = -size - 1; shiftAmount <= strGrid.length; shiftAmount++) {
			Color[][] shiftedGrid = Grid.makeBlankGrid(size);
			//Shift long grid over by shiftAmount and print what overlaps with shiftedGrid
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (!(x + shiftAmount < 0 || x + shiftAmount > strGrid.length - 1)) {
						shiftedGrid[x][y] = strGrid[x + shiftAmount][y];
					} //else out of bounds, not printed
				}
			}

			g.setGrid(shiftedGrid);

			try {
				Thread.sleep(scrollTick);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println();
	}

	private void loop(Grid g, String s) {
		while (loop) {
			try {
				scroll(g, s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
