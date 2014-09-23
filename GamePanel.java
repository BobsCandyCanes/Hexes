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

public class GamePanel extends JPanel implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;
	
	private Graphics g;
	
	private int width;
	private int height;
	
	private int hexWidth = 36;
	private int hexHeight = 33;
	
	private int numRows;
	private int numColumns;
	
	private int mouseX;
	private int mouseY;
	
	private int timerNumber = 0;
	private int stepNumber = 0;
	
	private static int speed = 50;
	
	private Image highlightRing;
	private Image selectRing;
	
	private boolean mousePressed = false;
	private boolean sandboxMode = true;
	private boolean isPaused = true;
	
	static Timer mouseTimer;
	
	Location[][] locations;
	
	MouseListener mouseAdapter = new MouseListener() {
		public void mouseClicked(MouseEvent arg0)
		{
		}

		public void mouseEntered(MouseEvent arg0)
		{
		}

		public void mouseExited(MouseEvent arg0)
		{
		}

		public void mousePressed(MouseEvent arg0)
		{
			GamePanel.this.mousePressed = true;
		}

		public void mouseReleased(MouseEvent arg0)
		{
			GamePanel.this.mousePressed = false;
		}
	};

	public GamePanel(int x, int y)
	{
		this.width = x;
		this.height = y;

		this.numColumns = (this.width / (this.hexWidth - 11) - 1);
		this.numRows = (this.height / (this.hexHeight - 1));

		this.locations = new Location[this.numRows][this.numColumns];

		resetWorld();

		setPreferredSize(new Dimension(x, y));

		setFocusable(true);

		addMouseListener(this.mouseAdapter);
	}

	public void actionPerformed(ActionEvent e)
	{
		Point mousePos = getMousePosition();
		
		if (mousePos != null)
		{
			this.mouseX = mousePos.x;
			this.mouseY = mousePos.y;
		}
		
		if (!this.isPaused)
		{
			this.timerNumber += 1;
			
			if (this.timerNumber == speed)
			{
				this.timerNumber = 0;
				step();
			}
		}
		
		update();
		repaint();
	}

	public void run()
	{
		initialize();

		mouseTimer = new Timer(15, this);
		mouseTimer.setInitialDelay(0);
		mouseTimer.start();
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

		boolean isHighlighted = false;
		boolean isSelected = false;
		
		for (Location[] rows : this.locations)
		{
			for (Location l : rows)
			{
				if (!done)
				{
					if (l.mouseIsOver(this.mouseX, this.mouseY))
					{
						isHighlighted = true;
						
						if (this.mousePressed)
						{
							isSelected = true;
							
							if (this.sandboxMode)
							{
								l.changeHexType();
							}
						}
						
						done = true;
					}
				}
				
				g.drawImage(l.getImage(), l.getXPixel(), l.getYPixel(), null);
				
				for (Image overlay : l.getOverlays())
				{
					g.drawImage(overlay, l.getXPixel(), l.getYPixel(), null);
				}
				
				if (isHighlighted)
				{
					g.drawImage(this.highlightRing, l.getXPixel(), l.getYPixel(), null);
				}
				
				if (isSelected)
				{
					g.drawImage(this.selectRing, l.getXPixel(), l.getYPixel(),null);
				}
				
				isHighlighted = false;
				isSelected = false;
			}
		}
	}

	public void checkOverlays()
	{
		for (int r = 0; r < this.numRows; r++)
		{
			for (int c = 0; c < this.numColumns; c++)
			{
				this.locations[r][c].getHex().checkOverlays();
			}
		}
	}

	public void update()
	{
	}

	public void resetWorld()
	{
		this.stepNumber = 0;
		
		for (int r = 0; r < this.numRows; r++)
		{
			for (int c = 0; c < this.numColumns; c++)
			{
				int hexX = c * (this.hexWidth - 11);
				int hexY = r * (this.hexHeight - 1);
				
				if (c % 2 == 1)
				{
					hexY += 16;
				}
				
				this.locations[r][c] = new Location(hexX, hexY, this);

				this.locations[r][c].setRow(r);
				this.locations[r][c].setColumn(c);
			}
		}
	}

	public void step()
	{
		this.stepNumber += 1;
		System.out.println("Step number: " + this.stepNumber);
		
		for (Location[] r : this.locations)
		{
			for (Location c : r)
			{
				c.getHex().setHasActed(false);
			}
		}
		
		for (Location[] r : this.locations)
		{
			for (Location c : r)
			{
				if (!c.getHex().hasActed())
				{
					c.getHex().setHasActed(true);
					c.act();
				}
			}
		}
	}

	public boolean isSandboxMode()
	{
		return this.sandboxMode;
	}

	public void toggleSandboxMode()
	{
		if (this.sandboxMode)
		{
			this.sandboxMode = false;
		} 
		else
		{
			this.sandboxMode = true;
		}
	}

	public void togglePause()
	{
		if (this.isPaused)
		{
			this.isPaused = false;
			System.out.println("Unpausing");
		}
		else
		{
			this.isPaused = true;
			System.out.println("Pausing");
		}
	}

	public int getWidth()
	{
		return this.width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public int getNumRows()
	{
		return this.numColumns;
	}

	public int getNumColumns()
	{
		return this.numRows;
	}

	public Location[][] getLocations()
	{
		return this.locations;
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
		if ((r >= 0) && (c >= 0) && (r < this.numRows) && (c < this.numColumns))
		{
			return this.locations[r][c] != null;
		}
		
		return false;
	}

	public boolean isValidLocation(Location l)
	{
		for (int r = 0; r < this.numRows; r++)
		{
			for (int c = 0; c < this.numColumns; c++)
			{
				if (this.locations[r][c] == l)
				{
					return true;
				}
			}
		}
		
		return false;
	}

	public void setSpeed(int i)
	{
		speed = i;
	}

	public void importSprites()
	{
		try
		{
			this.highlightRing = ImageIO.read(new File("Images/Overlays/highlightRing.png"));
			this.selectRing = ImageIO.read(new File("Images/Overlays/selectRing.png"));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
