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
	private final int SCROLL_TICK = 1000;

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

		for (int i = 0; i < uniqueLetters.length; i++) {
			//Assume all letters are present on the same sized grid (which they should be)
			letterGrids.put(uniqueLetters[i],  Grid.readGrid("letters/" + letters[i] + ".txt"));
		}

		//This definitely isn't n^3 time, it's meant to be slow!
		for (char letter: letters) { //debug line
			Color[][] grid = letterGrids.get(letter);
			//Color[][] grid = letterGrids.get("a"); //debug line

			//Debugging grid
			if (str.equals("debug")) {
				grid = Grid.readGrid("letters/debug.txt");
				debug = true;
			}

			int steps = grid.length; //Width of grid

			//Shift grid over
			//The x coord is the only thing being changed here
			//e.g. grid[x][y]
			for (int shiftAmount = steps - 1; shiftAmount >= 0; shiftAmount--) {
				Color[][] shiftedGrid = Grid.makeBlankGrid(steps);

				//Shift all grid elements over by shiftAmount, unless they go out of bounds
				for (int x = 0; x < steps; x++) {
					for (int y = 0; y < steps; y++) {
						if (!(x + shiftAmount > steps - 1)) {
							shiftedGrid[x + shiftAmount][y] = grid[x][y];
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
		} //debug line

		System.out.println();
	}
}
