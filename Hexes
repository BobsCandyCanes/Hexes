 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.image.BufferedImage;
 import java.io.BufferedWriter;
 import java.io.File;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.ArrayList;
 import javax.imageio.ImageIO;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.JMenu;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import javax.swing.JPanel;
 import javax.swing.JSlider;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 
 public class Hexes
 {
   static JFrame mainWindow = new JFrame("Hexes");
   
   private static String worldFilePath = "src/world.txt";
   
   static JPanel leftPanel = new JPanel();
   static JPanel rightPanel = new JPanel();
   static JPanel topPanel = new JPanel();
   static JPanel bottomPanel = new JPanel();
   
   static JMenuBar menuBar = new JMenuBar();
   static JMenu fileMenu = new JMenu("File");
   
   static JMenuItem resetWorldButton = new JMenuItem("Reset world");
   static JMenuItem saveWorldButton = new JMenuItem("Save world");
   static JMenuItem loadWorldButton = new JMenuItem("Load world");
   
   static JSlider speedSlider;
   
   static final int SPEED_MIN = 40;
   static final int SPEED_MAX = 100;
   static final int SPEED_INIT = 50;
   
   private static int simulationSpeed = 50;
   
   static JButton pauseButton = new JButton("Unpause");
   static JButton grassButton;
   static JButton waterButton;
   static JButton treesButton;
   static JButton peopleButton;
   static JButton bearButton;
   static JButton lavaButton;
   static JButton rallyPointButton;
   static JButton houseButton;
   
   static ImageIcon grassIcon;
   static ImageIcon waterIcon;
   static ImageIcon treesIcon;
   static ImageIcon peopleIcon;
   static ImageIcon bearIcon;
   static ImageIcon lavaIcon;
   static ImageIcon rallyPointIcon;
   static ImageIcon houseIcon;
   
   static BorderLayout mainLayout = new BorderLayout();
   
   private static int gamePanelWidth = 1000;
   private static int gamePanelHeight = 500;
   
   private static int width = 1000;
   private static int height = 800;
   
   private static int leftMargin = width / 5;
   private static int rightMargin = width / 12;
   private static int topMargin = 50;
   private static int bottomMargin = 50;
   
   private static String hexType = "Grass";
   
   private static ArrayList<JButton> buttons = new ArrayList<JButton>();
   
   static GamePanel gamePanel = new GamePanel(gamePanelWidth, gamePanelHeight);
   
   public static void main(String[] args)
   {
     System.out.println("Starting game");
     
     initializeGame();
     
     gamePanel.run();
   }
   
   static ActionListener toggleSandboxMode = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.gamePanel.toggleSandboxMode();
       Hexes.gamePanel.togglePause();
       
       if (Hexes.gamePanel.isSandboxMode())
       {
         Hexes.pauseButton.setText("Unpause");
         
         for (JButton b : Hexes.buttons) 
         {
           b.setForeground(Color.BLACK);
         }
       }
       else
       {
         Hexes.pauseButton.setText("Pause");
         
         for (JButton b : Hexes.buttons) 
         {
           b.setForeground(Color.GRAY);
         }
       }
     }
   };
   
   static ActionListener setHexTypeGrass = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "Grass";
     }
   };
   
   static ActionListener setHexTypeLava = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "Lava";
     }
   };
   
   static ActionListener setHexTypeBear = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "Bear";
     }
   };
   
   static ActionListener setHexTypeWater = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "Water";
     }
   };
   
   static ActionListener setHexTypeTrees = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "Trees";
     }
   };
   
   static ActionListener setHexTypePeople = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "People";
     }
   };
   
   static ActionListener setHexTypeRallyPoint = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "RallyPoint";
     }
   };
   
   static ActionListener setHexTypeHouse = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.hexType = "House";
     }
   };
   
   static ActionListener resetWorldAction = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.gamePanel.resetWorld();
     }
   };
   
   static ActionListener saveWorldAction = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       Hexes.saveWorld(Hexes.worldFilePath);
     }
   };
   
   static ActionListener loadWorldAction = new ActionListener()
   {
     public void actionPerformed(ActionEvent e)
     {
       System.out.println("Loading the world");
     }
   };
   
   static ChangeListener speedChanged = new ChangeListener()
   {
     public void stateChanged(ChangeEvent e)
     {
       JSlider source = (JSlider)e.getSource();
       
       if (!source.getValueIsAdjusting())
       {
         Hexes.simulationSpeed = 100 - source.getValue();
         
         if (Hexes.simulationSpeed == 0) 
         {
           Hexes.simulationSpeed += 1;
         }
         
         System.out.println(Hexes.simulationSpeed);
         
         Hexes.gamePanel.setSpeed(Hexes.simulationSpeed);
       }
     }
   };
   
   public static void initializeGame()
   {
     importIcons();
     
     leftPanel.setPreferredSize(new Dimension(leftMargin, 100));
     rightPanel.setPreferredSize(new Dimension(rightMargin, 100));
     topPanel.setPreferredSize(new Dimension(500, topMargin));
     bottomPanel.setPreferredSize(new Dimension(500, bottomMargin));
     
     pauseButton.addActionListener(toggleSandboxMode);
     
     grassButton.addActionListener(setHexTypeGrass);
     waterButton.addActionListener(setHexTypeWater);
     treesButton.addActionListener(setHexTypeTrees);
     peopleButton.addActionListener(setHexTypePeople);
     bearButton.addActionListener(setHexTypeBear);
     lavaButton.addActionListener(setHexTypeLava);
     rallyPointButton.addActionListener(setHexTypeRallyPoint);
     houseButton.addActionListener(setHexTypeHouse);
     
     speedSlider = new JSlider(1, 40, 100, 50);
     
     speedSlider.setMajorTickSpacing(10);
     speedSlider.setMinorTickSpacing(5);
     speedSlider.setPaintTicks(true);
     
     speedSlider.addChangeListener(speedChanged);
     
     mainWindow.setLayout(mainLayout);
     mainWindow.setSize(width, height);
     mainWindow.setLocationRelativeTo(null);
     mainWindow.setDefaultCloseOperation(3);
     
     mainWindow.add(gamePanel, "Center");
     mainWindow.add(topPanel, "First");
     mainWindow.add(bottomPanel, "Last");
     mainWindow.add(leftPanel, "Before");
     mainWindow.add(rightPanel, "After");
     
     resetWorldButton.addActionListener(resetWorldAction);
     saveWorldButton.addActionListener(saveWorldAction);
     loadWorldButton.addActionListener(loadWorldAction);
     
     menuBar.setBackground(Color.LIGHT_GRAY);
     
     fileMenu.add(resetWorldButton);
     fileMenu.add(saveWorldButton);
     fileMenu.add(loadWorldButton);
     
     topPanel.add(pauseButton);
     
     for (JButton b : buttons) 
     {
       leftPanel.add(b);
     }
     
     menuBar.add(fileMenu);
     mainWindow.setJMenuBar(menuBar);
     
     mainWindow.pack();
     mainWindow.setLocationRelativeTo(null);
     mainWindow.setVisible(true);
   }
   
   public static void importIcons()
   {
     try
     {
       BufferedImage grassSprite = ImageIO.read(new File("Images/grass.png"));
       grassIcon = new ImageIcon(grassSprite);
       
       BufferedImage waterSprite = ImageIO.read(new File("Images/water1.png"));
       waterIcon = new ImageIcon(waterSprite);
       
       BufferedImage treesSprite = ImageIO.read(new File("Images/trees.png"));
       treesIcon = new ImageIcon(treesSprite);
       
       BufferedImage peopleSprite = ImageIO.read(new File("Images/people.png"));
       peopleIcon = new ImageIcon(peopleSprite);
       
       BufferedImage bearSprite = ImageIO.read(new File("Images/bear.png"));
       bearIcon = new ImageIcon(bearSprite);
       
       BufferedImage lavaSprite = ImageIO.read(new File("Images/lava1.png"));
       lavaIcon = new ImageIcon(lavaSprite);
       
       BufferedImage rallyPointSprite = ImageIO.read(new File("Images/rallyPoint.png"));
       rallyPointIcon = new ImageIcon(rallyPointSprite);
       
       BufferedImage houseSprite = ImageIO.read(new File("Images/houseBuilt.png"));
       houseIcon = new ImageIcon(houseSprite);
     }
     catch (IOException e)
     {
       e.printStackTrace();
     }
     
     grassButton = new JButton("Grass ", grassIcon);
     waterButton = new JButton("Water ", waterIcon);
     treesButton = new JButton("Trees ", treesIcon);
     peopleButton = new JButton("People", peopleIcon);
     bearButton = new JButton("Bear    ", bearIcon);
     lavaButton = new JButton("Lava    ", lavaIcon);
     rallyPointButton = new JButton("Rally Point", rallyPointIcon);
     houseButton = new JButton("House", houseIcon);
     
     buttons.add(grassButton);
     buttons.add(waterButton);
     buttons.add(treesButton);
     buttons.add(peopleButton);
     buttons.add(bearButton);
     buttons.add(lavaButton);
     buttons.add(rallyPointButton);
     buttons.add(houseButton);
   }
   
   public static String getHexType()
   {
     return hexType;
   }
   
   public static int getGamePanelWidth()
   {
     return gamePanelWidth;
   }
   
   public static int getGamePanelHeight()
   {
     return gamePanelHeight;
   }
   
   public static void saveWorld(String filePath)
   {
     System.out.println("Saving the world");
     
     try
     {
       BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
       
       int numRows = gamePanel.getNumRows();
       int numColumns = gamePanel.getNumColumns();
       
       int width = gamePanel.getWidth();
       int height = gamePanel.getHeight();
       
       out.write(width + "\n");
       out.write(height + "\n");
       out.write(numRows + "\n");
       out.write(numColumns + "\n");
       
       for (int r = 0; r < numRows; r++) 
       {
         for (int c = 0; c < numColumns; c++)
         {
           Hex h = gamePanel.getLocations()[c][r].getHex();
           
           if ((h instanceof Grass))
           {
             out.write("G");
           } 
           else if ((h instanceof Water)) 
           {
             out.write("W");
           } 
           else if ((h instanceof Trees)) 
           {
             out.write("T");
           } 
           else 
           {
             out.write("H");
           }
           
           if ((r != 0) || (c != 0)) 
           {
             if (c == numColumns - 1) 
             {
               out.write("\n");
             }
           }
         }
       }
       out.close();
       
       System.out.println("World saved");
     }
     catch (IOException localIOException) {}
   }
   
   public static void loadWorld(String filePath)
   {
     System.out.println("Loading world... lol jk");
   }
 }
