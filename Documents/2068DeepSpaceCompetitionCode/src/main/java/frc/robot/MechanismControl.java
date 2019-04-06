package frc.robot;

import edu.wpi.first.wpilibj.*;
public class MechanismControl
{
    private CargoIntake cargoIntake;
    private PneumaticsControl hatchIntake;
    private Lift lift;
    private XboxController controller;
    private static boolean hasHatch = false;
    public MechanismControl(CargoIntake cargoIntake, PneumaticsControl hatchIntake, Lift lift, XboxController controller)
    {
        this.cargoIntake = cargoIntake;
        this.hatchIntake = hatchIntake;
        this.lift = lift;
        this.controller = controller;
    }
    public void baseControl()
    {
        //System.out.println("Started Mechanism Control");
        cargoIntake.baseControl();
        //System.out.println("Finished base control");
        lift.totalLiftControl();
        
        //System.out.println("Finished lift control");
        if(controller.getBButtonPressed() && lift.getCurrentPosition() != 0 )
      {
        if(hasHatch)
        {
          hatchIntake.outakeHatch();
          hasHatch = false;
          
          //mechanismController.setRumble(RumbleType.kRightRumble, 1);
        }
        else
        {
          hatchIntake.intakeHatch();
          hasHatch = true;
          /*lift.setCurrentPosition(lift.getCurrentPosition() + 1);
            lift.setTargetMet(false);
            while(!lift.getTargetMet())
            {
                lift.steppingLiftControl();
            }
            */
           // hatchIntake.pistonControl(false);
          
        }
    }
        
        //System.out.println("Got a little further");
        //hatchIntake.independentControl();
        if(controller.getPOV() == 0)
        {
            hatchIntake.pistonControl(true);
        }
        else if(controller.getPOV() == 180 )
        {
            hatchIntake.pistonControl(false);
        }

      
      //System.out.println("Printing positions");
      displayPositions();
      

    }
    public void displayPositions()
    {
        hatchIntake.displayPositions();
        cargoIntake.displayPositions();
        lift.displayValues();
       
    }
    public static boolean getHasHatch()
    {
        return(hasHatch);
    }
}