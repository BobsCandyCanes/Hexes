import java.awt.Image;
import java.util.ArrayList;

public class Location
{
	private int xPixel = 0;
	private int yPixel = 0;

	private int Column = 0;
	private int Row = 0;

	private int hexWidth = 36;
	private int hexHeight = 33;

	private int centerX;
	private int centerY;

	private GamePanel gamePanel;

	private Hex hex;

	public Location() 
	{

	}

	public Location(int x, int y, GamePanel gp)
	{
		this.gamePanel = gp;

		this.xPixel = x;
		this.yPixel = y;

		this.centerX = (this.xPixel + this.hexWidth / 2);
		this.centerY = (this.yPixel + this.hexHeight / 2);

		this.hex = new Grass();
	}

	public boolean mouseIsOver(int mouseX, int mouseY)
	{
		if (Math.abs(mouseX - this.centerX) <= 14)
		{
			if (Math.abs(mouseY - this.centerY) <= 14) 
			{
				return true;
			}
		}

		return false;
	}

	public void act()
	{
		this.hex.act();
	}

	public GamePanel getGamePanel()
	{
		return this.gamePanel;
	}

	public int getXPixel()
	{
		return this.xPixel;
	}

	public int getYPixel()
	{
		return this.yPixel;
	}

	public int getColumn()
	{
		return this.Column;
	}

	public void setColumn(int Column)
	{
		this.Column = Column;
	}

	public int getRow()
	{
		return this.Row;
	}

	public void setRow(int Row)
	{
		this.Row = Row;
	}

	public Hex getHex()
	{
		return this.hex;
	}

	public void setHex(Hex h)
	{
		this.hex = h;
		this.hex.setLocation(this);
	}

	public void changeHexType()
	{
		switch ((Hexes.getHexType()))
		{

		case "People": 
			this.setHex(new People());
			break;
		case "Bear": 
			this.setHex(new Bear());
			break;
		case "Lave": 
			this.setHex(new Lava());
			break;
		case "Grass": 
			this.setHex(new Grass());
			break;
		case "House": 
			this.setHex(new House());
			break;
		case "Trees": 
			this.setHex(new Trees());
			break;
		case "Water": 
			this.setHex(new Water());
			break;
		case "RallyPoint": 
			this.setHex(new RallyPoint());
			break;

		default:
			break;
		}

		this.gamePanel.checkOverlays();
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

		int count = 0;
		for (int c = this.Column - 1; c <= this.Column + 1; c++) 
		{
			for (int r = this.Row - 1; r <= this.Row + 1; r++) 
			{
				if (this.gamePanel.isValidLocation(r, c)) 
				{
					if (c == this.Column - 1)
					{
						if (this.Column % 2 == 0)
						{
							if (r != this.Row + 1)
							{
								tempLocations.add(this.gamePanel.getLocation(r, c));
								count++;
							}
						}
						else if (r != this.Row - 1)
						{
							tempLocations.add(this.gamePanel.getLocation(r, c));
							count++;
						}
					}
					else if (c == this.Column)
					{
						if (r != this.Row)
						{
							tempLocations.add(this.gamePanel.getLocation(r, c));
							count++;
						}
					}
					else if (c == this.Column + 1) 
					{
						if (this.Column % 2 == 0)
						{
							if (r != this.Row + 1)
							{
								tempLocations.add(this.gamePanel.getLocation(r, c));
								count++;
							}
						}
						else if (r != this.Row - 1)
						{
							tempLocations.add(this.gamePanel.getLocation(r, c));
							count++;
						}
					}
				}
			}
		}
		Location[] adjacentLocations = new Location[count];

		for (int i = 0; i < tempLocations.size(); i++) 
		{
			adjacentLocations[i] = ((Location)tempLocations.get(i));
		}
		return adjacentLocations;
	}
}
