/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.modules;

import com.team229.logomotion.utils.PortMap;
import com.team229.logomotion.userInterface.OperatorInterface;
import com.team229.logomotion.userInterface.ToggleButton;
import com.team229.logomotion.utils.Utils;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author PJ
 */
public class RollerClaw {
    private SpeedController topMotor;
    private SpeedController botMotor;

    private DoubleSolenoid armSolenoid;

    private OperatorInterface oi;

    private DigitalInput tubLimSw;

    private boolean armUp;

    ToggleButton armBtn = new ToggleButton(true);

    private double topRollerSpeed = 0;
    private double botRollerSpeed = 0;


    public RollerClaw() throws CANTimeoutException
    {
        this(PortMap.TOP_ROLLER_MOT,
                    PortMap.BOTTOM_ROLLER_MOT,
                    PortMap.TUBE_LSW,
                    PortMap.ARM_SOL_IN,
                    PortMap.ARM_SOL_OUT);
       // ;
    }

public RollerClaw(int topMot, int botMot, int tubeLS, int solIn, int solOut) throws CANTimeoutException
{
	oi = OperatorInterface.getInstance();

	topMotor = new CANJaguar(topMot);
	botMotor = new CANJaguar(botMot);

	armSolenoid = new DoubleSolenoid(solIn,  solOut);

	tubLimSw = new DigitalInput(tubeLS);

	armUp = false;
}


//The in/out has precedence.  Check to see what that is doing first.
//If that is stagnant, then look at the rotate value.
public void control()
{
//#define ROLLER_DEADBAND	.1

	//static int ctr = 0;

	double scaleDownFactor = 1;//1.5;	//Bigger
	double inOutMinVal = .1;

	double valFromInOut = oi.getRollerInOutStick()/scaleDownFactor;
	double valFromRotate = -oi.getRollerRotateStick()/scaleDownFactor;

	double topSpeed = 0;
	double botSpeed = 0;


	//If the in/out is not worth caring about
	if(Utils.inInclusiveRange(valFromInOut,0, inOutMinVal))
	{
		//Then rotate the tube
		topSpeed = -valFromRotate;
		botSpeed = valFromRotate;
	}
	//This means you are trying to suck in/spit out
	else
	{
		topSpeed = botSpeed = -valFromInOut;
	}

	//printf("roller.control top speed = %f, bot = %f\r\n", topSpeed, botSpeed);
	//printf("roller.control inout = %f, updown = %f\r\n", valFromInOut, valFromRotate);

        setTopBotSpeed(topSpeed, botSpeed);

	toggleArm();
}

private void setTopBotSpeed(double newTopSpeed, double newBotSpeed)
{
    if(newTopSpeed != topRollerSpeed || newBotSpeed != botRollerSpeed)
    {
        topRollerSpeed = newTopSpeed;
        botRollerSpeed = newBotSpeed;

	topMotor.set(topRollerSpeed);
	botMotor.set(botRollerSpeed);
    }
}

public void toggleArm()
{
    //TODO
    /*
	boolean wasReleased = true;


	if(oi.isArmBtnPressed())
	{
		if(wasReleased)
		{
			armUp = !armUp;
			wasReleased = false;
		}
	}
	else
		wasReleased = true;

	controlArm();
     * */

     armBtn.update(oi.isArmBtnPressed());

     if(armBtn.didStateChange())
     {
         armUp = armBtn.isHigh();
         controlArm();
     }

}

public void controlArm()
{
    System.out.println("Arm state chagned, updating");
    if(armUp)
    {
        moveArmUp();
    }
    else
    {
        moveArmDown();
    }
}

public void setArmPosition(boolean up)
{
    if(up != armUp)
    {
        armUp = up;
        controlArm();
    }
}

public void moveArmUp()
{
    //printf("moving arm up\r\n");
    armSolenoid.set(DoubleSolenoid.Value.kForward);
}

public void moveArmDown()
{
    //printf("moving arm down\r\n");
    armSolenoid.set(DoubleSolenoid.Value.kReverse);
}


public void stop()
{
    topMotor.set(0.0);
    botMotor.set(0.0);
}


public void suckIn()
{
	double speed = -.5;

    topMotor.set(speed);
    botMotor.set(speed);
}


public void spitOut()
{
	 double speed = .7;
	 double diff = .1;

    topMotor.set(speed+diff);
    botMotor.set(speed-diff);
}


public boolean getTubeSw()
{
	return !tubLimSw.get();
}
public void printToDash()
{
	Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();

	dash.addCluster();
	{
		dash.addDouble(topMotor.get());
		dash.addDouble(botMotor.get());

		dash.addBoolean(getTubeSw());
		dash.addBoolean(armUp);
	}
    dash.finalizeCluster();
}






}
