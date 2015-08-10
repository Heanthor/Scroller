package grid;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Tool for saving custom Grid layouts to files, in a format able to be loaded on demand.
 * @author Reed
 */
public class GridRecorder {
	private Color drawColor = Color.RED;

	public static void main(String[] args) {
		try {
			new GridRecorder().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void init() throws IOException {
		JFrame f = new JFrame("Letter Recorder");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocation(50, 50);

		int gridSize, squareSize;
		Grid g = new Grid(gridSize = 10, squareSize = 50, true);
		g.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int[] loc = g.gridLocation(arg0.getPoint());
				g.toggle(loc[0], loc[1], drawColor);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		//not confusing at all
		f.getContentPane().add(g);
		JPanel p = new JPanel();

		String[] colors = {"Red", "Orange", "Yellow", "Green", "Blue", "Violet"};
		JComboBox<String> c = new JComboBox<String>(colors);
		c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				@SuppressWarnings("unchecked")
				JComboBox<String> parent = (JComboBox<String>) arg0.getSource();
				String color = (String)parent.getSelectedItem();
				System.out.println(color);
				drawColor = map(color);
			}

			private Color map(String color) {
				switch (color) {
				case "Red":
					return Color.RED;
				case "Orange":
					return new Color(0xCD950C);
				case "Yellow":
					return Color.YELLOW;
				case "Green":
					return new Color(0x55AE3A);
				case "Blue":
					return Color.BLUE;
				case "Violet":
					return new Color(0x4B0082);
				default:
					return Color.RED;
				}
			}
		});
		p.add(c);

		JTextField tf = new JTextField(10);
		JButton r = new JButton("Reset");
		r.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				g.setSize(gridSize);//Reset size to original
				g.clearGrid();
				tf.setText("");
				f.pack();//Resize window
			}
		});

		p.add(r);
		JButton b = new JButton("Save");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String letter = "letters/" + tf.getText().trim() + ".txt";

				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(letter));
					bw.write(g.coordDump());
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		JButton l = new JButton("Load");
		l.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String txt = "";
				try {
					txt = tf.getText().trim();
					if (!txt.contains(".txt")) {
						txt += ".txt";
					}

					g.setGrid(Grid.readGrid("letters/" + txt));
					f.pack(); //Resizes the window to fit the incoming grid size
				} catch (IOException e) {
					System.err.println("File " + txt + " not found");
				}
			}
		});

		p.add(l);
		p.add(b);
		p.add(tf);

		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.pack();
		f.setVisible(true);
	}
}
