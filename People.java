import java.util.ArrayList;

public class People
extends Hex
{
	private static String spritePath = "Images/people.png";

	private Location currentTarget;
	private Location currentLocation;
	private Location moveLocation;
	private Location previousLocation = new Location();
	private Location secondPreviousLocation = new Location();

	private boolean hasBuiltHouse = false;

	private String currentTask = "wander";

	private int turnsSpentOnTask = 0;

	private int age = 0;

	public People()
	{
		super(spritePath);

		this.previousLocation = getLocation();
	}

	public void act()
	{
		if (this.currentTask.equals("wander"))
		{
			if ((!this.hasBuiltHouse) && (this.age > 5)) 
			{
				buildHouse();
			} 
			else 
			{
				wander();
			}
		}
		else if (this.currentTask.equals("moveToTarget")) 
		{
			moveToTarget(this.currentTarget);
		} 
		else if (this.currentTask.equals("build")) 
		{
			build();
		} 
		else if (this.currentTask.equals("idle")) 
		{
			idle();
		}
		this.age += 1;
	}

	public void setCurrentTask(String s)
	{
		this.currentTask = s;
	}

	public void setCurrentTask(String s, Location l)
	{
		if (s.equals("moveToTarget"))
		{
			this.currentTask = s;
			this.currentTarget = l;
		}
	}

	public String getCurrentTask()
	{
		return this.currentTask;
	}

	public void wander()
	{
		this.currentTask = "wander";

		this.currentLocation = getLocation();

		this.moveLocation = chooseMoveLocation();

		moveTo(this.moveLocation);
	}

	public void idle() {}

	public void build() {}

	public void buildHouse()
	{
		GamePanel gamePanel = getGamePanel();

		Location[] adjacentLocations = getLocation().getAdjacentLocations();
		for (Location l : adjacentLocations) 
		{
			if (gamePanel.isValidLocation(l)) 
			{
				if ((l.getHex() instanceof Grass))
				{
					boolean isSuitableSite = true;

					Location[] adjacentLocationsHouse = l.getAdjacentLocations();

					for (Location i : adjacentLocationsHouse) 
					{
						if (gamePanel.isValidLocation(i)) 
						{
							if ((i.getHex() instanceof House)) 
							{
								isSuitableSite = false;
							}
						}
					}
					if (isSuitableSite)
					{
						l.setHex(new House());
						this.hasBuiltHouse = true;
						return;
					}
				}
			}
		}
		wander();
	}

	public Location chooseMoveLocation()
	{
		Location thisLocation = getLocation();

		Location[] adjacentLocations = thisLocation.getAdjacentLocations();

		ArrayList<Location> possibleLocations = new ArrayList<Location>();

		for (Location l : adjacentLocations) 
		{
			if ((l.getHex() instanceof Grass)) 
			{
				possibleLocations.add(l);
			}
		}

		if (possibleLocations.size() == 0)
		{
			return thisLocation;
		}

		int random = (int)(Math.random() * possibleLocations.size());

		return (Location)possibleLocations.get(random);
	}

	public void moveToTarget(Location target)
	{
		this.turnsSpentOnTask += 1;

		if (this.turnsSpentOnTask > 9)
		{
			this.turnsSpentOnTask = 0;
			setCurrentTask("wander");
		}

		int thisRow = getLocation().getRow();
		int thisCol = getLocation().getColumn();

		int rowDiff = thisRow - target.getRow();
		int colDiff = thisCol - target.getColumn();

		GamePanel gamePanel = getGamePanel();

		this.currentLocation = getLocation();

		this.moveLocation = this.currentLocation;

		Location[] targetLocations = target.getAdjacentLocations();

		for (Location l : targetLocations) 
		{
			if (l == getLocation())
			{
				setCurrentTask("wander");
				return;
			}
		}

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
		
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		
		if (colDiff == 0)
		{
			if (rowDiff > 0)
			{
				possibleMoves.add(north);
				possibleMoves.add(northwest);
				possibleMoves.add(northeast);
				possibleMoves.add(southwest);
				possibleMoves.add(southeast);
				possibleMoves.add(south);
			}
			else
			{
				possibleMoves.add(south);
				possibleMoves.add(southeast);
				possibleMoves.add(southwest);
				possibleMoves.add(northeast);
				possibleMoves.add(northwest);
				possibleMoves.add(north);
			}
		}
		else if (rowDiff == 0)
		{
			if (colDiff > 0)
			{
				possibleMoves.add(south);
				possibleMoves.add(north);
				possibleMoves.add(southeast);
				possibleMoves.add(northeast);
				if (thisCol % 2 == 0)
				{
					possibleMoves.add(0, northwest);
					possibleMoves.add(0, southwest);
				}
				else
				{
					possibleMoves.add(0, southwest);
					possibleMoves.add(northwest);
				}
			}
			else
			{
				possibleMoves.add(south);
				possibleMoves.add(north);
				possibleMoves.add(southwest);
				possibleMoves.add(northwest);
				if (thisCol % 2 == 0)
				{
					possibleMoves.add(0, northeast);
					possibleMoves.add(0, southeast);
				}
				else
				{
					possibleMoves.add(0, southeast);
					possibleMoves.add(0, northeast);
				}
			}
		}
		else if ((rowDiff > 0) && (colDiff > 0))
		{
			possibleMoves.add(northwest);
			possibleMoves.add(north);
			possibleMoves.add(southwest);
			possibleMoves.add(northeast);
			possibleMoves.add(south);
			possibleMoves.add(southeast);
		}
		else if ((rowDiff > 0) && (colDiff < 0))
		{
			possibleMoves.add(northeast);
			possibleMoves.add(north);
			possibleMoves.add(southeast);
			possibleMoves.add(northwest);
			possibleMoves.add(south);
			possibleMoves.add(southwest);
		}
		else if ((rowDiff < 0) && (colDiff > 0))
		{
			possibleMoves.add(southwest);
			possibleMoves.add(south);
			possibleMoves.add(northwest);
			possibleMoves.add(southeast);
			possibleMoves.add(north);
			possibleMoves.add(northeast);
		}
		else if ((rowDiff < 0) && (colDiff < 0))
		{
			possibleMoves.add(southeast);
			possibleMoves.add(south);
			possibleMoves.add(northeast);
			possibleMoves.add(southwest);
			possibleMoves.add(north);
			possibleMoves.add(northwest);
		}
		for (Location l : possibleMoves) 
		{
			if (gamePanel.isValidLocation(l)) 
			{
				if ((l.getHex() instanceof Grass)) 
				{
					if ((l != this.previousLocation) && (l != this.secondPreviousLocation))
					{
						this.moveLocation = l;
						break;
					}
				}
			}
		}
		
		this.secondPreviousLocation = this.previousLocation;
		this.previousLocation = getLocation();

		moveTo(this.moveLocation);
	}

	public void moveTo(Location moveLocation)
	{
		this.currentLocation = getLocation();
		if ((moveLocation != this.currentLocation) && ((moveLocation.getHex() instanceof Grass)))
		{
			moveLocation.setHex(this);

			this.currentLocation.setHex(new Grass());
		}
	}

	public void setHasBuiltHouse(boolean b)
	{
		this.hasBuiltHouse = b;
	}
}

