import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.File;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.IOException;


public class Graffiti2 implements ClipboardOwner
{
   private String fileName;
   private Point colorButton;
   private Point colorBox;
   private Point topLeft;
   private Point bottomRight;
   private Robot robot;
   private BufferedImage image;
   
   public Graffiti2(String fileName) throws java.awt.AWTException
   {
      robot = new Robot();
      robot.setAutoDelay(100);
/*      robot.mouseMove(0,0);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      System.out.println("\n" + MouseInfo.getPointerInfo().getDevice());
      System.out.println(MouseInfo.getPointerInfo().getLocation() + "\n");*/
      this.fileName = fileName;
      this.colorButton = new Point(410, 485);
      this.colorBox = new Point(462, 393);
      this.topLeft = new Point(301, 176);
      this.bottomRight = new Point(873, 438);
   }
   
   public void eat()
   {
      try
      {
         File in = new File(fileName);
         BufferedImage buffer = ImageIO.read(in);
         this.image = buffer;
      }
      catch(java.io.IOException e)
      {
         System.out.println("EXCEPTION CAUGHT: " + e);
      }
   }
   
   public void run()
   {
      String currentColor = null;
      String oldColor = null;
      
      int rows = 172; //264;
      int cols = 575;
      
      for(int row = 171; row < rows; row += 1)
      {
         for(int col = 0; col < cols - 1; col += 1)
         {
            /* Get colors */
            int colorcode = image.getRGB(col, row);
            int red = (colorcode & 0x00ff0000) >> 16;
            int green = (colorcode & 0x0000ff00) >> 8;
            int blue = colorcode & 0x000000ff;
            Color color = new Color(red, green, blue);
            Color next  = new Color(red, green, blue);
            
            int offset = 0;
            
            /* Get hex string */
            currentColor = "#" + Integer.toHexString(colorcode).substring(2);
            
            if(!(currentColor.equals("#0c101d")))
            {
               if(!(currentColor.equals(oldColor)))
               {
                  /* Change color */
                  robot.mouseMove((int)colorButton.getX(), (int)colorButton.getY());
                  robot.mousePress(InputEvent.BUTTON1_MASK);
                  robot.mouseRelease(InputEvent.BUTTON1_MASK);
                  robot.mouseMove((int)colorBox.getX(), (int)colorBox.getY());
                  // Double-click
                  robot.mousePress(InputEvent.BUTTON1_MASK);
                  robot.mouseRelease(InputEvent.BUTTON1_MASK);
                  robot.mousePress(InputEvent.BUTTON1_MASK);
                  robot.mouseRelease(InputEvent.BUTTON1_MASK);
                  // Paste
                  this.setClipboardContents(currentColor);
                  robot.keyPress(KeyEvent.VK_CONTROL);
                  robot.keyPress(KeyEvent.VK_V);
                  robot.keyRelease(KeyEvent.VK_V);
                  robot.keyRelease(KeyEvent.VK_CONTROL);
                  robot.keyPress(KeyEvent.VK_ENTER);
                  robot.keyRelease(KeyEvent.VK_ENTER);
               
                  oldColor = currentColor;
               }

               robot.mouseMove((int)topLeft.getX() + col, (int)topLeft.getY() + row);
               // These are repeated becaues sometimes Flash doesn't notice
               robot.mousePress(InputEvent.BUTTON1_MASK);
               robot.mousePress(InputEvent.BUTTON1_MASK);
               robot.mousePress(InputEvent.BUTTON1_MASK);
               robot.mousePress(InputEvent.BUTTON1_MASK);
               
               while(color.equals(next) && ((cols - col - offset) > 0))
               {
                  colorcode = image.getRGB(col + offset, row);
                  red = (colorcode & 0x00ff0000) >> 16;
                  green = (colorcode & 0x0000ff00) >> 8;
                  blue = colorcode & 0x000000ff;
                  next  = new Color(red, green, blue);
               
                  offset++;
               }
            
               col += offset - 1;
               robot.mouseMove((int)topLeft.getX() + col, (int)topLeft.getY() + row);
               // These are repeated becaues sometimes Flash doesn't notice
               robot.mouseRelease(InputEvent.BUTTON1_MASK);
               robot.mouseRelease(InputEvent.BUTTON1_MASK);
               robot.mouseRelease(InputEvent.BUTTON1_MASK);
               robot.mouseRelease(InputEvent.BUTTON1_MASK);
               
               col--;
               System.out.println(col);
            }
         }
      }
   }
   
   /**
      * Empty implementation of the ClipboardOwner interface.
      */
      public void lostOwnership( Clipboard aClipboard, Transferable aContents)
      { }

     /**
     * Place a String on the clipboard, and make this class the
     * owner of the Clipboard's contents.
     */
     public void setClipboardContents( String aString )
     {
       StringSelection stringSelection = new StringSelection( aString );
       Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
       clipboard.setContents( stringSelection, this );
     }

     /**
     * Get the String residing on the clipboard.
     *
     * @return any text found on the Clipboard; if none found, return an
     * empty String.
     */
     public String getClipboardContents()
     {
       String result = "";
       Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
       //odd: the Object param of getContents is not currently used
       Transferable contents = clipboard.getContents(null);
       boolean hasTransferableText =
         (contents != null) &&
         contents.isDataFlavorSupported(DataFlavor.stringFlavor);
       if (hasTransferableText)
       {
         try
         {
           result = (String)contents.getTransferData(DataFlavor.stringFlavor);
         }
         catch (UnsupportedFlavorException ex)
         {
           // unlikely
           System.out.println(ex);
           ex.printStackTrace();
         }
         catch (IOException ex)
         {
           System.out.println(ex);
           ex.printStackTrace();
         }
       }
       return result;
     }
   
   public static void main(String[] args)
   {
      if(args.length > 0)
      {
         try
         {
            Graffiti2 app = new Graffiti2(args[0]);
            app.eat();
            app.run();
         }
         catch(java.awt.AWTException e)
         {
            System.out.println("EXCEPTION CAUGHT: " + e);
         }
      }
   }
}
