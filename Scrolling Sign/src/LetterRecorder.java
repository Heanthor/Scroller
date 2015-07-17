import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LetterRecorder {
	public static void main(String[] args) {
		try {
			new LetterRecorder().init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		JFrame f = new JFrame("Letter Recorder");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(50, 50, 600, 600);

		int gridSize, squareSize;
		Grid g = new Grid(gridSize = 11, squareSize = 50);
		g.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int[] loc = g.gridLocation(arg0.getPoint());
				g.toggle(loc[0], loc[1]);
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

					BufferedReader br = new BufferedReader(new FileReader("letters/" + txt));
					String dmp = br.readLine();
					g.setGrid(g.parseCoordDump(dmp));
					f.pack(); //Resizes the window to fit the incoming grid size
					
					br.close();
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
