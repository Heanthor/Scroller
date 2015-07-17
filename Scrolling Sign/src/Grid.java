import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Grid extends JComponent {
	private static final long serialVersionUID = 2892865424401791072L;
	private Color[][] squares;
	private int size = 10;
	private int squarePixelSize = 50;

	public Grid() {
		squares = new Color[size][size];
		this.setPreferredSize(new Dimension
				(squarePixelSize * size, squarePixelSize * size));

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = Color.BLACK;
			}
		}
	}

	public Grid(int size, int squarePixelSize) {
		this.size = size;
		this.squarePixelSize = squarePixelSize;

		squares = new Color[size][size];
		this.setPreferredSize(new Dimension
				(squarePixelSize * size, squarePixelSize * size));

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = Color.BLACK;
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				g.setColor(squares[i][j]);
				g.fillRect(i * squarePixelSize, j * squarePixelSize,
						squarePixelSize, squarePixelSize);
			}
		}
	}

	/**
	 * Draws a rectangle at specified coordinates.
	 * @param x - x coord
	 * @param y - y coord
	 * @param color - Color of rectangle to be drawn
	 * @return the current object
	 */
	public Grid drawRect(int x, int y, Color color) {
		squares[x][y] = color;
		this.repaint();

		return this;
	}

	/**
	 * Clears the grid, using white as a default color. 
	 * @return the current object
	 */
	public Grid clearGrid() {
		clearGrid(Color.WHITE);
		return this;
	}

	/**
	 * Clears the grid.
	 * @param bgColor - Color to set the grid to.
	 * @return the current object
	 */
	public Grid clearGrid(Color bgColor) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = bgColor;
			}
		}
		
		this.repaint();
		
		return this;
	}
}
