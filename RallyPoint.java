public class RallyPoint extends Hex
{
	private static String spritePath = "Images/rallyPoint.png";
	private int row;
	private int column;
	GamePanel gamePanel;

	public RallyPoint()
	{
		super(spritePath);
	}

	public void act()
	{
		this.row = getLocation().getRow();
		this.column = getLocation().getColumn();

		this.gamePanel = getGamePanel();
		
		for (int r = -8; r <= this.row + 8; r++)
		{
			for (int c = -8; c <= this.column + 8; c++)
			{
				if (this.gamePanel.isValidLocation(r, c))
				{
					if ((this.gamePanel.getLocation(r, c).getHex() instanceof People))
					{
						this.gamePanel.getLocation(r, c).getHex().setCurrentTask("moveToTarget", getLocation());
					}
				}
			}
		}
	}
}
