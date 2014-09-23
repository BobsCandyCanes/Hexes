import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Lava extends Hex
{
	private static String spritePath = "Images/lava1.png";

	private int numSteps = 0;

	private Image obsidianTop;
	private Image obsidianTopRight;
	private Image obsidianBottomRight;
	private Image obsidianBottom;
	private Image obsidianBottomLeft;
	private Image obsidianTopLeft;

	public Lava()
	{
		super(spritePath);

		importOverlays();
	}

	public void act()
	{
		this.numSteps += 1;

		if (this.numSteps == 2)
		{
			this.numSteps = 0;

			spritePath = "Images/lava2.png";
		}
		else
		{
			spritePath = "Images/lava1.png";
		}

		setSpritePath(spritePath);

		burnStuff();
	}

	public void burnStuff()
	{
		Location[] adjacentLocations = getLocation().getAdjacentLocations();

		for (Location l : adjacentLocations) 
		{
			if ((l.getHex() instanceof Trees)) 
			{
				l.setHex(new Fire());
			} 
			else if ((l.getHex() instanceof House)) 
			{
				l.setHex(new Fire());
			}
		}
	}

	public void checkOverlays()
	{
		clearOverlays();

		GamePanel gamePanel = getGamePanel();

		int thisRow = getLocation().getRow();
		int thisCol = getLocation().getColumn();

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
				if (!(l.getHex() instanceof Lava)) 
				{
					if (l == north) 
					{
						addOverlay(this.obsidianTop);
					} 
					else if (l == south) 
					{
						addOverlay(this.obsidianBottom);
					} 
					else if (l == southeast) 
					{
						addOverlay(this.obsidianBottomRight);
					} 
					else if (l == southwest) 
					{
						addOverlay(this.obsidianBottomLeft);
					} 
					else if (l == northeast) 
					{
						addOverlay(this.obsidianTopRight);
					} 
					else if (l == northwest) 
					{
						addOverlay(this.obsidianTopLeft);
					}
				}
			}
		}
	}

	public void importOverlays()
	{
		try
		{
			this.obsidianTop = ImageIO.read(new File("Images/Overlays/obsidianTop.png"));
			this.obsidianTopRight = ImageIO.read(new File("Images/Overlays/obsidianTopRight.png"));
			this.obsidianBottomRight = ImageIO.read(new File("Images/Overlays/obsidianBottomRight.png"));
			this.obsidianBottom = ImageIO.read(new File("Images/Overlays/obsidianBottom.png"));
			this.obsidianBottomLeft = ImageIO.read(new File("Images/Overlays/obsidianBottomLeft.png"));
			this.obsidianTopLeft = ImageIO.read(new File("Images/Overlays/obsidianTopLeft.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}

