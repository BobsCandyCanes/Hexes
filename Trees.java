import java.util.ArrayList;

public class Trees extends Hex
{
	private static String spritePath = "Images/treesYoung.png";
	private static String spritePath2 = "Images/trees.png";
	private static String spritePath3 = "Images/treesOld.png";
	
	private final int SPREAD_RATE = 15;	//The higher the rate, the slower the spread
	
	private int age = 0;

	public Trees()
	{
		super(spritePath);
	}

	public void act()
	{
		this.age += 1;
		
		if (this.age % 3 == 0)
		{
			spread();
		}
		if (this.age == 10)
		{
			setSpritePath(spritePath2);
		}
		if (this.age == 20)
		{
			setSpritePath(spritePath3);
		}
	}


	public void spread()
	{
		Location[] adjacentLocations = getLocation().getAdjacentLocations();

		Location moveLocation = chooseMoveLocation(adjacentLocations);

		if (moveLocation != getLocation())
		{
			int random = (int) (Math.random() * SPREAD_RATE);

			if (random == 1)
			{
				moveLocation.setHex(new Trees());
			}
		}
	}

	public Location chooseMoveLocation(Location[] a)
	{
		ArrayList<Location> possibleLocations = new ArrayList<Location>();
		
		for (Location l : a)
		{
			if ((l.getHex() instanceof Grass))
			{
				possibleLocations.add(l);
			}
		}
		
		if (possibleLocations.size() == 0)
		{
			return getLocation();
		}
		
		int random = (int) (Math.random() * possibleLocations.size());

		return (Location) possibleLocations.get(random);
	}

}
