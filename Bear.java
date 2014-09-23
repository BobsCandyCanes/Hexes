import java.util.ArrayList;

public class Bear extends Hex
{
	private static String spritePath = "Images/bear.png";

	public Bear()
	{
		super(spritePath);
	}

	public void act()
	{
		Location tempLocation = getLocation();

		Location moveLocation = chooseMoveLocation();
		
		if (moveLocation != tempLocation)
		{
			moveLocation.setHex(this);

			tempLocation.setHex(new Grass());
		}
		
		eat();
	}

	public Location chooseMoveLocation()
	{
		Location thisLocation = getLocation();

		Location[] adjacentLocations = thisLocation.getAdjacentLocations();

		ArrayList<Location> possibleLocations = new ArrayList<Location>();
		
		for (Location l : adjacentLocations)
		{
			if (((l.getHex() instanceof Grass)) || ((l.getHex() instanceof Trees)))
			{
				possibleLocations.add(l);
			}
		}
		
		if (possibleLocations.size() == 0)
		{
			return thisLocation;
		}
		
		int random = (int) (Math.random() * possibleLocations.size());

		return (Location) possibleLocations.get(random);
	}

	public void eat()
	{
		Location[] adjacentLocations = getLocation().getAdjacentLocations();

		for (Location l : adjacentLocations)
		{
			if ((l.getHex() instanceof People))
			{
				l.setHex(new Grass());
			}
		}
	}
}
