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
		BufferedReader br = new BufferedReader(new FileReader("letters/a.txt"));
		System.out.println(br.readLine());
		JFrame f = new JFrame("Color Squares Done the Right Way");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(50, 50, 600, 600);

		int gridSize, squareSize;
		Grid g = new Grid(gridSize = 10, squareSize = 50);
		g.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int[] loc = g.gridLocation(arg0.getPoint());
				g.toggle(loc[0], loc[1]);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent arg0) {

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

		});

		//not confusing at all
		f.getContentPane().add(g);
		JPanel p = new JPanel();
		JButton r = new JButton("Reset");
		r.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				g.clearGrid();
			}
		});
		
		p.add(r);
		JButton b = new JButton("Save");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		p.add(b);
		JTextField tf = new JTextField(10);
		p.add(tf);

		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.pack();
		f.setVisible(true);
		br.close();
	}
}
