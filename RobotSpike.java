import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;

public class RobotSpike
{
   public RobotSpike() throws java.awt.AWTException
   {
      Robot robot = new Robot();
      robot.mouseMove(0,0);
      robot.mousePress(InputEvent.BUTTON1_MASK);
      System.out.println("\n" + MouseInfo.getPointerInfo().getDevice());
      System.out.println(MouseInfo.getPointerInfo().getLocation() + "\n");
   }
   
   public static void main(String[] args)
   {
      try
      {
         RobotSpike app = new RobotSpike();
      }
      catch(java.awt.AWTException e)
      {
         System.out.println("EXCEPTION CAUGHT: " + e);
      }
   }
}
