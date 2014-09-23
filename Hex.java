import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public abstract class Hex
{
	private String spritePath = "Images/hex.png";
	
	private String currentTask;
	
	private Image sprite;
	
	private ArrayList<Image> overlays = new ArrayList<Image>();
	
	private boolean hasActed = false;
	
	private Location location;

	public Hex()
	{
		importSprites();
	}

	public Hex(String str1)
	{
		this.spritePath = str1;

		importSprites();
	}

	public void importSprites()
	{
		try
		{
			this.sprite = ImageIO.read(new File(this.spritePath));
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void act()
	{
	}

	public void addOverlay(Image i)
	{
		this.overlays.add(i);
	}

	public void clearOverlays()
	{
		this.overlays = new ArrayList<Image>();
	}

	public void setCurrentTask(String s)
	{
	}

	public void setCurrentTask(String s, Location l)
	{
	}

	public void setHasBuiltHouse(boolean b)
	{
	}

	public String getCurrentTask()
	{
		System.out.println("ERROR!");

		return this.currentTask;
	}

	public ArrayList<Image> getOverlays()
	{
		return this.overlays;
	}

	public void checkOverlays()
	{
	}

	public boolean hasActed()
	{
		return this.hasActed;
	}

	public void setHasActed(boolean b)
	{
		this.hasActed = b;
	}

	public void setSpritePath(String str1)
	{
		this.spritePath = str1;

		importSprites();
	}

	public void setSprite(Image i)
	{
		this.sprite = i;
	}

	public void setLocation(Location l)
	{
		this.location = l;
	}

	public Location getLocation()
	{
		return this.location;
	}

	public GamePanel getGamePanel()
	{
		return this.location.getGamePanel();
	}

	public Image getImage()
	{
		return this.sprite;
	}
}
