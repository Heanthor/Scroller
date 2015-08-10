package grid;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JComponent;

/**
 * A grid of squares, represented by a Color[][].
 * The background color is white.
 * @author Reed
 */
public class Grid extends JComponent {
	private static final long serialVersionUID = 2892865424401791072L;
	private static final Color BACKGROUND = Color.WHITE;
	private boolean lines = true; //Draw grid lines
	private Color[][] squares;
	private int size = 10;
	private int width = 50;
	private int height = 50;

	//Colors that look better than defaults
	public static final Color ORANGE = new Color(0xCD950C);
	public static final Color GREEN = new Color(0x55AE3A);
	public static final Color VIOLET = new Color(0x4B0082);

	/**
	 * Creates a new grid with 10 rows and cols, with square pixel sizes of 50.
	 */
	public Grid() {
		init();
	}

	/**
	 * Creates a new grid with set rows and cols, and a set square pixel size.
	 * @param size - The number of rows and cols.
	 * @param squarePixelSize - The width and height of each cell.
	 */
	public Grid(int size, int squarePixelSize, boolean lines) {
		this.size = size;
		this.width = squarePixelSize;
		this.height = squarePixelSize;
		this.lines = lines;

		init();
	}

	/**
	 * Creates a grid with set size, and separate width and height.
	 * @param size - The number of rows and cols.
	 * @param width - The width of each cell.
	 * @param height - The height of each cell.
	 */
	public Grid(int size, int width, int height, boolean lines) {
		this.size = size;
		this.width = width;
		this.height = height;
		this.lines = lines;

		init();
	}

	/**
	 * Sets the current grid to be equal to the parameter. The current width
	 * and height of the Grid are conserved.
	 * @param arr - The grid to set.
	 */
	public Grid(Color[][] arr, boolean lines) {
		int size = arr.length;
		this.lines = lines;

		squares = arr;
		this.setPreferredSize(new Dimension
				(width * size, height * size));
	}

	/**
	 * Common setup used by all constructors.
	 */
	private void init() {
		squares = new Color[size][size];
		this.setPreferredSize(new Dimension
				(width * size, height * size));
		squares = makeBlankGrid(size);

		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentHidden(ComponentEvent arg0) {
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
			}

			@Override
			public void componentResized(ComponentEvent arg0) {				
				int gridHeight = arg0.getComponent().getHeight();
				int gridWidth = arg0.getComponent().getWidth();

				height = gridHeight / size;
				width = gridWidth / size;
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
			}
		});
	}

	public void paint(Graphics g) {
		super.paint(g);

		//Squares
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				g.setColor(squares[i][j]);
				g.fillRect(i * width, j * height,
						width, height);
			}
		}

		if (lines) {
			//Lines
			for (int i = 0; i < size; i++) {
				g.setColor(Color.BLACK);
				if (i != 0) {
					//vertical
					g.drawLine(i * width, 0, i * width, height * size);
				}
				if (i != 0) {
					//horizontal
					g.drawLine(0, i * height, width * size, i * height);
				}
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
	 * Draws a rectangle at specified coordinates, in red.
	 * @param x - x coord
	 * @param y - y coord
	 * @return the current object
	 */
	public Grid drawRect(int x, int y) {
		squares[x][y] = Color.RED;
		this.repaint();

		return this;
	}

	/**
	 * Fills this grid with a given color, making every cell this color.
	 * @param color - Color to fill with
	 * @return the current object
	 */
	public Grid fill(Color color) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				squares[i][j] = color;
			}
		}
		this.repaint();

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

	/**
	 * Clears the grid, using white as a default color. 
	 * @return the current object
	 */
	public Grid clearGrid() {
		clearGrid(Grid.BACKGROUND);
		return this;
	}

	/**
	 * Toggles the color located at point x, y.
	 *  Paints specified color, or white if otherwise.
	 * @param x - x coordinate
	 * @param y - y coordinate
	 * @param color - Color to draw with
	 */
	public void toggle(int x, int y, Color color) {
		if (squares[x][y] == Grid.BACKGROUND) {
			drawRect(x, y, color);
		} else {
			drawRect(x, y, Grid.BACKGROUND);
		}
	}

	/**
	 * Translates a MouseEvent point into the corresponding coordinate on this Grid
	 * @param p The Point the mouse was clicked on
	 * @return [x, y] coordinate of the clicked square
	 */
	public int[] gridLocation(Point p) {
		int[] container = new int[2];
		container[0] = (p.x / width);
		container[1] = (p.y / height);

		/* System.out.println(p + " into [" + container[0] + ", " +
				container[1] + "]."); */

		return container;
	}

	/**
	 * Save the current coordinates to a string of the form
	 * "size:{grid size}|{(x1,y1)*{color}/(x2,y2)...}"
	 * @return The string generated by the current grid
	 */
	public String coordDump() {
		String toReturn = "size:" + this.size + "|";

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (squares[i][j] != Grid.BACKGROUND) {
					toReturn += "(" + i + "," + j + ")*" + squares[i][j].getRGB() + "/";
				}
			}
		}

		return toReturn;
	}

	/**
	 * Set this grid use the supplied Color[][] to display its colors.
	 * Sets the size of this grid to the size of the incoming array.
	 * @param in - Array to set to
	 */
	public void setGrid(Color[][] in) {
		squares = in;
		this.size = in.length;

		this.setPreferredSize(new Dimension
				(width * size, height * size));
		this.repaint();
	}

	/**
	 * Set the size of the grid. Also sets preferred window size.
	 * Clears the contents of the grid in the process.
	 * @param size - Size to set to
	 */
	public void setSize(int size) {
		this.size = size;
		squares = makeBlankGrid(size);
		this.setPreferredSize(new Dimension
				(width * size, height * size));
	}

	public Color getBackgroundColor() {
		return Grid.BACKGROUND;
	}

	/**
	 * Creates a "blank" grid, e.g. all squares are White.
	 * @param size The size of the grid to create
	 * @return The blank grid.
	 */
	public static Color[][] makeBlankGrid(int size) {
		Color[][] toReturn = new Color[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				toReturn[i][j] = Grid.BACKGROUND;
			}
		}

		return toReturn;
	}

	/**
	 * Parse the grid layout saved by coordDump() into a Color array that represents the grid.
	 * @param s The parsed string
	 * @return A Color array to be used to set a Grid.
	 */
	public static Color[][] parseCoordDump(String s) {
		int size = Integer.parseInt(s.substring(s.indexOf(":") + 1, s.indexOf("|")));
		Color[][] toReturn = new Color[size][size];

		//initialize
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				toReturn[i][j] = Grid.BACKGROUND;
			}
		}

		do {
			try {
				String point = s.substring(s.indexOf("(") + 1, s.indexOf(")")); //x,y
				int x = Integer.parseInt(point.substring(0, point.indexOf(",")));
				int y = Integer.parseInt(point.substring(point.indexOf(",") + 1));
				s = s.substring(s.indexOf(")"));
				String color = s.substring(s.indexOf("*") + 1, s.indexOf("/"));
				toReturn[x][y] = new Color(Integer.parseInt(color));

				s = s.substring(s.indexOf(")") + 1);
			} catch (StringIndexOutOfBoundsException e) {
				//Empty grid (or space character)
				break;
			}
		} while(s.length() > 0);

		return toReturn;
	}

	/**
	 * Reads a saved grid stored in a file.
	 * Example filename: "letters/a.txt"
	 * @param filename The filename of the grid
	 * @return The grid found in the file
	 * @throws IOException If file is not found
	 */
	public static Color[][] readGrid(String filename) throws IOException {		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String dmp = br.readLine();

		br.close();

		return Grid.parseCoordDump(dmp);
	}

	/**
	 * Prints a grid in a more readable format.
	 * Format: "(x, y):{color}" where color is either W (white) or R (red)
	 * @param grid The grid to read
	 * @return The string representation
	 */
	public static String readGrid(Color[][] grid) {
		String toReturn = "";
		int size = grid.length;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				toReturn += "(" + i + ", " + j + "):" +
						(grid[i][j] == Color.RED ? "R" : "W") + " ";
			}
		}

		return toReturn;
	}

	/**
	 * Immediately repaints the Grid.
	 */
	public void forceRepaint() {
		this.paintImmediately(new Rectangle(size * width, size * height));
	}

	public String toString() {
		return Grid.readGrid(squares);
	}
}
