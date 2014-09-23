 public class House
   extends Hex
 {
   private static String spritePath = "Images/houseMarker.png";
   
   private int row;
   private int column;
   
   private int turnsBuilt = 0;
   
   private String state = "marker";
   
   private boolean hasSpawnedPeople = false;
   
   private Hex builder;
   
   GamePanel gamePanel;
   
   public House()
   {
     super(spritePath);
   }
   
   public void act()
   {
     if (this.state.equals("marker"))
     {
       attractPeople();
     }
     else if (this.state.equals("one"))
     {
       setSpritePath("Images/house1.png");
       this.state = "two";
     }
     else if (this.state.equals("two"))
     {
       setSpritePath("Images/house2.png");
       this.state = "built";
     }
     else if (this.state.equals("built"))
     {
       this.turnsBuilt += 1;
       
       setSpritePath("Images/houseBuilt.png");
       
       if (this.turnsBuilt == 2) 
       {
         this.builder.setCurrentTask("wander");
       }
       
       if (this.turnsBuilt > 2) 
       {
         if (!this.hasSpawnedPeople) 
         {
           spawnPeople();
         }
       }
     }
   }
   
   public void attractPeople()
   {
     this.row = getLocation().getRow();
     this.column = getLocation().getColumn();
     
     this.gamePanel = getGamePanel();
     
     Location[] adjacentLocations = getLocation().getAdjacentLocations();
     
     for (Location l : adjacentLocations) 
     {
       if (((l.getHex() instanceof People)) && (!l.getHex().getCurrentTask().equals("build")))
       {
         this.builder = l.getHex();
         this.builder.setCurrentTask("build");
         this.builder.setHasBuiltHouse(true);
         this.state = "one";
         return;
       }
     }
     
     for (int r = -6; r <= this.row + 6; r++) 
     {
       for (int c = -6; c <= this.column + 6; c++) 
       {
         if (this.gamePanel.isValidLocation(r, c))
         {
           Hex nearbyHex = this.gamePanel.getLocation(r, c).getHex();
           
           if (((nearbyHex instanceof People)) && (nearbyHex.getCurrentTask().equals("wander"))) 
           {
             nearbyHex.setCurrentTask("moveToTarget", getLocation());
           }
         }
       }
     }
   }
   
   public void spawnPeople()
   {
     Location[] adjacentLocations = getLocation().getAdjacentLocations();
     
     for (Location l : adjacentLocations) 
     {
       if (this.gamePanel.isValidLocation(l)) 
       {
         if ((l.getHex() instanceof Grass))
         {
           l.setHex(new People());
           this.hasSpawnedPeople = true;
           return;
         }
       }
     }
   }
 }
