import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Water
extends Hex
{
	private static String spritePath = "Images/water1.png";
	
	private int numSteps = 0;
	
	private Image sandTop;
	private Image sandTopRight;
	private Image sandBottomRight;
	private Image sandBottom;
	private Image sandBottomLeft;
	private Image sandTopLeft;

	public Water()
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

			spritePath = "Images/water1.png";
		}
		else
		{
			spritePath = "Images/water2.png";
		}
		
		setSpritePath(spritePath);
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
				if (!(l.getHex() instanceof Water)) 
				{
					if (l == north) 
					{
						addOverlay(this.sandTop);
					} 
					else if (l == south) 
					{
						addOverlay(this.sandBottom);
					} 
					else if (l == southeast) 
					{
						addOverlay(this.sandBottomRight);
					} 
					else if (l == southwest) 
					{
						addOverlay(this.sandBottomLeft);
					} 
					else if (l == northeast) 
					{
						addOverlay(this.sandTopRight);
					} 
					else if (l == northwest) 
					{
						addOverlay(this.sandTopLeft);
					}
				}
			}
		}
	}

	public void importOverlays()
	{
		try
		{
			this.sandTop = ImageIO.read(new File("Images/Overlays/sandTop.png"));
			this.sandTopRight = ImageIO.read(new File("Images/Overlays/sandTopRight.png"));
			this.sandBottomRight = ImageIO.read(new File("Images/Overlays/sandBottomRight.png"));
			this.sandBottom = ImageIO.read(new File("Images/Overlays/sandBottom.png"));
			this.sandBottomLeft = ImageIO.read(new File("Images/Overlays/sandBottomLeft.png"));
			this.sandTopLeft = ImageIO.read(new File("Images/Overlays/sandTopLeft.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
