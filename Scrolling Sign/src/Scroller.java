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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Scroller {
	private boolean debug = false;
	private final int SCROLL_TICK = 200;

	public static void main(String[] args) {
		new Scroller().init();
	}

	private void init() {
		JFrame f = new JFrame("Scroller");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(50, 50);

		int gridSize, squareSize;
		Grid g = new Grid(gridSize = 10, squareSize = 50);

		JPanel p = new JPanel();
		JTextField tf = new JTextField(10);
		JButton s = new JButton("Scroll");
		s.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!tf.getText().equals("")) {
					//Asynchronous since repaints and sleeps are done in a loop
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								scroll(g, tf.getText().trim());
							} catch (IOException e) {
								tf.setText("File not found");
							}
						}
					}).start();
				}
				tf.setText("");
			}
		});

		p.add(s);
		p.add(tf);

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
			Color [][] tmp = Grid.readGrid("letters/" + letters[i] + ".txt");

			//Assume all letters are present on the same sized grid (which they should be)
			if (size == -1) {
				size = tmp.length;
			}

			letterGrids.put(uniqueLetters[i], tmp);
		}

		//Color[][] grid = letterGrids.get("a"); //temp
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
		//Color[][] grid = letterGrids.get("a"); //debug line

		//Debugging grid
		if (str.equals("debug")) {
			//grid = Grid.readGrid("letters/debug.txt");
			debug = true;
		}

		/* Shift grid over
		 *The x coord is the only thing being changed here
		 *e.g. grid[x][y]
		 *Each iteration waits for SCROLL_TICK before shifting again 
		 */
		for (int shiftAmount = longGridSize - 1; shiftAmount >= 0; shiftAmount--) {
			Color[][] shiftedGrid = Grid.makeBlankGrid(size);
			//Shift all grid elements over by shiftAmount, unless they go out of bounds
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					if (!(x + shiftAmount > strGrid.length - 1)) {
						shiftedGrid[x][y] = strGrid[x + shiftAmount][y];
					} //else out of bounds, not printed
				}
			}

			g.setGrid(shiftedGrid);

			if (debug) {
				System.out.println(Grid.readGrid(shiftedGrid));
			}

			try {
				Thread.sleep(SCROLL_TICK);
				if (debug) {
					System.out.println("sleep");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println();
	}
}

