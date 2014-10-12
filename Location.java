import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Location
{
	private int xPixel = 0;
	private int yPixel = 0;

	private int Column = 0;
	private int Row = 0;

	private int hexWidthInPixels = 36;
	private int hexHeightInPixels = 33;

	private int centerXPixel;
	private int centerYPixel;

	private String faction = "None";

	private Image blueBorderTop;
	private Image blueBorderTopRight;
	private Image blueBorderBottomRight;
	private Image blueBorderBottom;
	private Image blueBorderBottomLeft;
	private Image blueBorderTopLeft;

	private ArrayList<Image> borders = new ArrayList<Image>();

	private GamePanel gamePanel;

	private Hex hex;

	public Location()
	{
		this.importBorders();
	}

	public Location(int x, int y, GamePanel gp)
	{
		gamePanel = gp;

		xPixel = x;
		yPixel = y;

		centerXPixel = (xPixel + this.hexWidthInPixels / 2);
		centerYPixel = (yPixel + this.hexHeightInPixels / 2);

		hex = new Grass();
		hex.setLocation(this);

		importBorders();
	}

	public boolean mouseIsOver(int mouseX, int mouseY)
	{
		if (Math.abs(mouseX - centerXPixel) <= 14)	//If the mouse is within 14 pixels of the center
		{
			if (Math.abs(mouseY - centerYPixel) <= 14)
			{
				return true;
			}
		}

		return false;
	}

	public void act()
	{
		hex.act();
	}

	public GamePanel getGamePanel()
	{
		return gamePanel;
	}

	public int getXPixel()
	{
		return xPixel;
	}

	public int getYPixel()
	{
		return yPixel;
	}

	public int getColumn()
	{
		return Column;
	}

	public void setColumn(int newColumn)
	{
		Column = newColumn;
	}

	public int getRow()
	{
		return Row;
	}

	public void setRow(int newRow)
	{
		Row = newRow;
	}

	public void setFaction(String str)
	{
		faction = str;

		gamePanel.checkBorders();
	}

	public String getFaction()
	{
		return faction;
	}

	public ArrayList<Image> getBorders()
	{
		return borders;
	}

	public void addBorder(Image i)
	{
		borders.add(i);
	}

	public void clearBorders()
	{
		borders = new ArrayList<Image>();
	}

	public Hex getHex()
	{
		return hex;
	}

	public void setHex(Hex h)
	{
		hex = h;
		hex.setLocation(this);
	}

	public void changeHexType()
	{
		Class<? extends Hex> hexClass = Hexes.getHexType().getClass();

		try
		{
			Constructor<? extends Hex> constructor = hexClass.getConstructor();	
			setHex(constructor.newInstance());
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	public Image getImage()
	{
		return this.hex.getImage();
	}

	public ArrayList<Image> getOverlays()
	{
		return this.hex.getOverlays();
	}

	public Location[] getAdjacentLocations()
	{
		ArrayList<Location> tempLocations = new ArrayList<Location>();

		int numberOfAdjacentLocations = 0;
		
		for (int c = Column - 1; c <= Column + 1; c++)
		{
			for (int r = Row - 1; r <= Row + 1; r++)
			{
				if (gamePanel.isValidLocation(r, c))
				{
					if (c == Column - 1)
					{
						if (Column % 2 == 0)
						{
							if (r != Row + 1)
							{
								tempLocations.add(gamePanel.getLocation(r, c));
								numberOfAdjacentLocations++;
							}
						}
						else if (r != Row - 1)
						{
							tempLocations.add(this.gamePanel.getLocation(r, c));
							numberOfAdjacentLocations++;
						}
					}
					else if (c == Column)
					{
						if (r != Row)
						{
							tempLocations.add(gamePanel.getLocation(r, c));
							numberOfAdjacentLocations++;
						}
					}
					else if (c == Column + 1)
					{
						if (Column % 2 == 0)
						{
							if (r != Row + 1)
							{
								tempLocations.add(gamePanel.getLocation(r, c));
								numberOfAdjacentLocations++;
							}
						}
						else if (r != Row - 1)
						{
							tempLocations.add(gamePanel.getLocation(r, c));
							numberOfAdjacentLocations++;
						}
					}
				}
			}
		}
		
		Location[] adjacentLocations = new Location[numberOfAdjacentLocations];

		for (int i = 0; i < tempLocations.size(); i++)
		{
			adjacentLocations[i] = ((Location) tempLocations.get(i));
		}
		return adjacentLocations;
	}

	public void checkBorders()
	{
		clearBorders();

		int thisRow = getRow();
		int thisCol = getColumn();

		GamePanel gamePanel = this.getGamePanel();

		Location north = gamePanel.getLocation(thisRow - 1, thisCol);
		Location south = gamePanel.getLocation(thisRow + 1, thisCol);
		Location northwest;
		Location northeast;
		Location southeast;
		Location southwest;

		if (thisCol % 2 == 0)
		{
			northeast = gamePanel.getLocation(thisRow - 1, thisCol + 1);
			southeast = gamePanel.getLocation(thisRow, thisCol + 1);
			southwest = gamePanel.getLocation(thisRow, thisCol - 1);
			northwest = gamePanel.getLocation(thisRow - 1, thisCol - 1);
		}
		else
		{
			northeast = gamePanel.getLocation(thisRow, thisCol + 1);
			southeast = gamePanel.getLocation(thisRow + 1, thisCol + 1);
			southwest = gamePanel.getLocation(thisRow + 1, thisCol - 1);
			northwest = gamePanel.getLocation(thisRow, thisCol - 1);
		}

		Location[] adjacentLocations = new Location[6];

		adjacentLocations[0] = north;
		adjacentLocations[1] = northeast;
		adjacentLocations[2] = southeast;
		adjacentLocations[3] = south;
		adjacentLocations[4] = southwest;
		adjacentLocations[5] = northwest;

		for (Location l : adjacentLocations)
		{
			if (gamePanel.isValidLocation(l))
			{
				if (!(l.getFaction().equals(this.getFaction())))
				{
					if (l == north)
					{
						addBorder(this.blueBorderTop);
					}
					else if (l == south)
					{
						addBorder(this.blueBorderBottom);
					}
					else if (l == southeast)
					{
						addBorder(this.blueBorderBottomRight);
					}
					else if (l == southwest)
					{
						addBorder(this.blueBorderBottomLeft);
					}
					else if (l == northeast)
					{
						addBorder(this.blueBorderTopRight);
					}
					else if (l == northwest)
					{
						addBorder(this.blueBorderTopLeft);
					}
				}
			}
		}
	}

	public void importBorders()
	{
		try
		{
			this.blueBorderTop = ImageIO.read(new File(
					"Images/Overlays/blueBorderTop.png"));
			this.blueBorderTopRight = ImageIO.read(new File(
					"Images/Overlays/blueBorderTopRight.png"));
			this.blueBorderBottomRight = ImageIO.read(new File(
					"Images/Overlays/blueBorderBottomRight.png"));
			this.blueBorderBottom = ImageIO.read(new File(
					"Images/Overlays/blueBorderBottom.png"));
			this.blueBorderBottomLeft = ImageIO.read(new File(
					"Images/Overlays/blueBorderBottomLeft.png"));
			this.blueBorderTopLeft = ImageIO.read(new File(
					"Images/Overlays/blueBorderTopLeft.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
