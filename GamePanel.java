import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, MouseListener,
Runnable
{
	private static final long serialVersionUID = 1L;

	private Graphics g;

	private int panelWidthInPixels;
	private int panelHeightInPixels;

	private int hexWidthInPixels = 36;
	private int hexHeightInPixels = 33;

	private int numberOfRows;
	private int numberOfColumns;

	private int mouseX;
	private int mouseY;

	private int stepNumber = 0;

	private int timerNumber = 0;
	private static int gameSpeed = 15; // The lower the number, the faster the speed

	private Image highlightRing;
	private Image selectRing;
	
	private boolean isPaused = true;

	static Timer timer;

	Location[][] locations;

	public GamePanel(int width, int height)
	{
		panelWidthInPixels = width;
		panelHeightInPixels = height;

		numberOfColumns = (panelWidthInPixels / (hexWidthInPixels - 11) - 1);
		numberOfRows = (panelHeightInPixels / (hexHeightInPixels - 1));

		locations = new Location[numberOfRows][numberOfColumns];

		resetWorld();

		setPreferredSize(new Dimension(panelWidthInPixels, panelHeightInPixels));

		setFocusable(true);

		addMouseListener(this);
	}

	public void mousePressed(MouseEvent arg0)
	{
		if(isPaused)
		{
			updateMousePosition();

			for (Location[] rows : this.locations)
			{
				for (Location loc : rows)
				{
					if (loc.mouseIsOver(mouseX, mouseY))
					{
						g.drawImage(selectRing, loc.getXPixel(), loc.getYPixel(), null);

						if (isPaused)
						{
							loc.changeHexType();
							checkOverlays();
						}
					}
				}
			}
		}
	}

	public void mouseEntered(MouseEvent arg0)
	{
	}

	public void mouseExited(MouseEvent arg0)
	{
	}

	public void mouseClicked(MouseEvent arg0)
	{
		if(!isPaused)
		{
			updateMousePosition();

			boolean done = false;

			for (Location[] rows : locations)
			{
				for (Location loc : rows)
				{
					if (!done)
					{
						if (loc.mouseIsOver(mouseX, mouseY))
						{
							g.drawImage(selectRing, loc.getXPixel(), loc.getYPixel(), null);

							if (loc.getHex() instanceof Grass)
							{
								loc.setHex(new RallyPoint());
							} 
							else if (loc.getHex() instanceof RallyPoint)
							{
								loc.setHex(new Grass());
							}

							done = true;
						}
					}
				}
			}
		}
	}

	public void mouseReleased(MouseEvent arg0)
	{
	}

	public void actionPerformed(ActionEvent e)
	{
		if (!isPaused)
		{
			timerNumber++;

			if (timerNumber == gameSpeed)
			{
				timerNumber = 0;

				worker.run();
			}
		}

		repaint();
	}

	public void run()
	{
		initialize();

		timer = new Timer(5, this);
		timer.setInitialDelay(0);
		timer.start();

		worker.start();
	}

	public void initialize()
	{
		importSprites();

		this.g = getGraphics();

		paint(this.g);
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		boolean done = false;

		updateMousePosition();

		for (Location[] rows : this.locations)
		{
			for (Location loc : rows)
			{
				g.drawImage(loc.getImage(), loc.getXPixel(), loc.getYPixel(), null);

				for (Image overlay : loc.getOverlays())
				{
					g.drawImage(overlay, loc.getXPixel(), loc.getYPixel(), null);
				}

				for (Image border : loc.getBorders())
				{
					g.drawImage(border, loc.getXPixel(), loc.getYPixel(), null);
				}

				if (!done)
				{
					if (loc.mouseIsOver(mouseX, mouseY))
					{
						g.drawImage(highlightRing, loc.getXPixel(), loc.getYPixel(), null);

						done = true;
					}
				}
			}
		}
	}

	public void checkOverlays()
	{
		for (int row = 0; row < numberOfRows; row++)
		{
			for (int col = 0; col < numberOfColumns; col++)
			{
				locations[row][col].getHex().checkOverlays();
			}
		}
	}

	public void checkBorders()
	{
		for (int row = 0; row < numberOfRows; row++)
		{
			for (int col = 0; col < numberOfColumns; col++)
			{
				if (!locations[row][col].getFaction().equals("None"))
				{
					locations[row][col].checkBorders();
				}
			}
		}
	}

	public void resetWorld()
	{
		stepNumber = 0;

		for (int row = 0; row < numberOfRows; row++)
		{
			for (int col = 0; col < numberOfColumns; col++)
			{
				int hexXCoordinate = col * (hexWidthInPixels - 11);
				int hexYCoordinate = row * (hexHeightInPixels - 1);

				if (col % 2 == 1)
				{
					hexYCoordinate += 16;
				}

				locations[row][col] = new Location(hexXCoordinate, hexYCoordinate, this);

				locations[row][col].setRow(row);
				locations[row][col].setColumn(col);
			}
		}

		checkOverlays();
	}

	Thread worker = new Thread() 
	{
		public void run()
		{
			step();
		}
	};

	public void step()
	{
		stepNumber++;

		System.out.println("Step number: " + stepNumber);

		for (Location[] row : locations)
		{
			for (Location col : row)
			{
				col.getHex().setHasActed(false);
			}
		}

		for (Location[] row : locations)
		{
			for (Location col : row)
			{
				if (!col.getHex().hasActed())
				{
					col.getHex().setHasActed(true);
					col.act();
				}
			}
		}
	}
	
	public boolean isPaused()
	{
		return isPaused;
	}

	public void togglePause()
	{
		if (isPaused)
		{
			isPaused = false;
			System.out.println("Unpausing");
		} 
		else
		{
			isPaused = true;
			System.out.println("Pausing");
		}
	}

	public int getPanelWidthInPixels()
	{
		return panelWidthInPixels;
	}

	public int getPanelHeightInPixels()
	{
		return panelHeightInPixels;
	}

	public int getNumberOfRows()
	{
		return numberOfColumns;
	}

	public int getNumberOfColumns()
	{
		return numberOfRows;
	}

	public Location[][] getLocations()
	{
		return locations;
	}

	public Location getLocation(int r, int c)
	{
		if (isValidLocation(r, c))
		{
			return this.locations[r][c];
		}

		return null;
	}

	public boolean isValidLocation(int r, int c)
	{
		if ((r >= 0) && (c >= 0) && (r < numberOfRows) && (c < numberOfColumns))
		{
			return locations[r][c] != null;
		}

		return false;
	}

	public boolean isValidLocation(Location loc)
	{
		for (int r = 0; r < numberOfRows; r++)
		{
			for (int c = 0; c < numberOfColumns; c++)
			{
				if (locations[r][c] == loc)
				{
					return true;
				}
			}
		}

		return false;
	}

	public void updateMousePosition()
	{
		Point mousePos = getMousePosition();

		if (mousePos != null)
		{
			mouseX = mousePos.x;
			mouseY = mousePos.y;
		}
	}

	public void setGameSpeed(int i)
	{
		gameSpeed = i;
	}

	public void importSprites()
	{
		try
		{
			highlightRing = ImageIO.read(new File("Images/Overlays/highlightRing.png"));
			selectRing = ImageIO.read(new File("Images/Overlays/selectRing.png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
