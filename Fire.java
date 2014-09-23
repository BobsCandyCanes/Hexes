 public class Fire extends Hex
 {
   private static String spritePath = "Images/fire.png";
   private int turnsAlive = 0;
   
   public Fire()
   {
     super(spritePath);
   }
   
   public void act()
   {
     this.turnsAlive += 1;
     
     if (this.turnsAlive == 2) 
     {
       getLocation().setHex(new Grass());
     }
   }
 }


