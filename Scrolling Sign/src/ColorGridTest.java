
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

/**
 * The actual way to draw a grid of colorful squares >.>
 * @author Reed
 */
public class ColorGridTest {
	public static void main(String[] args) {
		new ColorGridTest().init();
	}

	private void init() {
		JFrame f = new JFrame("Color Squares Done the Right Way");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setBounds(50, 50, 600, 600);

		int gridSize, squareSize;

		Grid g = new Grid(gridSize = 10, squareSize = 50);
		//g.drawRect(5, 3, Color.BLUE).drawRect(4, 3, Color.RED);

		drawRandomRectangles(g, gridSize);

		//not confusing at all
		f.getContentPane().add(g);
		JPanel p = new JPanel();
		JButton b = new JButton("Refresh");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				drawRandomRectangles(g, gridSize);
			}
		});

		p.add(b);

		f.getContentPane().add(p, BorderLayout.SOUTH);
		f.pack();
		f.setVisible(true);
	}

	public void drawRandomRectangles(Grid g, int gridSize) {
		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				//this is where function pointers would be useful
				//try 255 for nice blue grid
				//or 0xFFFFFF for all colors
				g.drawRect(i, j, new Color(new Random().nextInt(0xFFFFFF)));
			}
		}
	}
}
