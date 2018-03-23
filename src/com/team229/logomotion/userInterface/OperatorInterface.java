/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.userInterface;

import com.team229.logomotion.utils.Utils;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author PJ
 */
public class OperatorInterface {

    Joystick rightJoy;
    Joystick leftJoy;
    Joystick opCont;

/************************************
 * 			XBOX Mappings			*
 ***********************************/
private static final int XBOX360_A_BTN		= 1;
private static final int XBOX360_B_BTN		= 2;
private static final int XBOX360_X_BTN		= 3;
private static final int XBOX360_Y_BTN		= 4;

private static final int XBOX360_LB_BTN		= 5;
private static final int XBOX360_RB_BTN		= 6;


private static final int XBOX360_LEFT_STICK_Y 	= 2;
private static final int XBOX360_TRIGGER_AXIS	= 3;
private static final int XBOX360_RIGHT_STICK_Y 	= 5;

/*



/****************************************************
 * 				Cyrpress Board IO					*
 * **************************************************/

//Digital out
private static final int TUBE_INDICATOR_LEDS = 6;

//Digital In
private static final int BLUE_TUBE_BTN = 6;//5
private static final int RED_TUBE_BTN = 5;//6// //2
private static final int WHITE_TUBE_BTN	 = 2;//5//2//6

private static final int AIM_BTN = 1;
private static final int DEPLOY_BTN = 2;




private static OperatorInterface singletonInstance = null;
    private final boolean USE_FAKE_IO = false;

public static OperatorInterface getInstance()
{
	if( singletonInstance == null)
		singletonInstance = new OperatorInterface();

	return singletonInstance;
}


    DriverStation ds;

public OperatorInterface()
{
	rightJoy 	= new Joystick(1);
	leftJoy 	= new Joystick(2);


	opCont 		= new Joystick(3);

	if(USE_FAKE_IO)
            ds = DriverStation.getInstance();
	

}

public void runLightTest()
{
    //TODO
	int ctr = 0;
	boolean lightsOn = true;

	if(ctr > 100)
	{
		ctr = 0;
		lightsOn = !lightsOn;
	}
	ctr++;

	setTubeLights(lightsOn);
}

public void setTubeLights(boolean on)
{
if(USE_FAKE_IO)
	ds.setDigitalOut(TUBE_INDICATOR_LEDS, on);

}

public boolean isCircleBtnPressed()
{
if(USE_FAKE_IO)
	return !ds.getDigitalIn(WHITE_TUBE_BTN);
else
	return false;

}

public boolean isTriangleBtnPressed()
{
if(USE_FAKE_IO)
	return !ds.getDigitalIn(RED_TUBE_BTN);
else
	return false;

}

public boolean isSquareBtnPressed()
{
if(USE_FAKE_IO)
	return !ds.getDigitalIn(BLUE_TUBE_BTN);
else
	return false;

}



public double getRight_y()
{
	return rightJoy.getY();
}
public double getLeft_y()
{
	return leftJoy.getY();
}

public boolean isDamperPressed()
{
	return rightJoy.getTrigger();
}


public double getRollerInOutStick()
{
	double deadband = .1;
	double val = 0;



	if(!Utils.inInclusiveRange( opCont.getRawAxis(XBOX360_TRIGGER_AXIS),0,deadband))
		val = (opCont.getRawAxis(XBOX360_TRIGGER_AXIS));

	return val;
}


public double getElevatorStick()
{
	double deadband = .1;
	double val = 0;

	if(!Utils.inInclusiveRange(opCont.getRawAxis(XBOX360_RIGHT_STICK_Y),0,deadband))
		val = opCont.getRawAxis(XBOX360_RIGHT_STICK_Y);

	return val;
}

public double getRollerRotateStick()
{
	double deadband = .17;
	double val = 0;

	if(!Utils.inInclusiveRange(opCont.getRawAxis(XBOX360_LEFT_STICK_Y),0,deadband))
		val = opCont.getRawAxis(XBOX360_LEFT_STICK_Y);

	return val;
}



public boolean isShifterPressed()
{
	return leftJoy.getTrigger();
}

public boolean isAutoLowPegBtnPressed()
{
	return (opCont.getRawButton(XBOX360_X_BTN) || rightJoy.getRawButton(4));
}
public boolean isAutoMidPegBtnPressed()
{
	return (opCont.getRawButton(XBOX360_Y_BTN) || rightJoy.getRawButton(3));
}
public boolean isAutoHighPegBtnPressed()
{
	return (opCont.getRawButton(XBOX360_B_BTN) || rightJoy.getRawButton(5));
}
public boolean isAutoLoaderBtnPressed()
{
	return opCont.getRawButton(XBOX360_A_BTN);
}
public boolean isPegHeightModifierBtnPressed()
{
	return (opCont.getRawButton(XBOX360_RB_BTN) || leftJoy.getRawButton(4));
}

public boolean isArmBtnPressed()
{
	return ( opCont.getRawButton(XBOX360_LB_BTN) || rightJoy.getRawButton(2));
}



public void printToDash()
{
	Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();


	dash.addCluster();
	{
		//Joystick stuff
		dash.addDouble(getRight_y());
		dash.addDouble(getLeft_y());
		dash.addDouble(getElevatorStick());
		dash.addDouble(getRollerInOutStick());
		dash.addDouble(getRollerRotateStick());

		dash.addBoolean(isDamperPressed());
		dash.addBoolean(isShifterPressed());

		dash.addBoolean(isAutoLowPegBtnPressed());
		dash.addBoolean(isAutoMidPegBtnPressed());
		dash.addBoolean(isAutoHighPegBtnPressed());
		dash.addBoolean(isAutoLoaderBtnPressed());
		dash.addBoolean(isPegHeightModifierBtnPressed());

		dash.addBoolean(isArmBtnPressed());


		//Cyrpess Stuff
		dash.addBoolean(isTriangleBtnPressed());
		dash.addBoolean(isCircleBtnPressed());
		dash.addBoolean(isSquareBtnPressed());
		dash.addBoolean(isMiniArmsBtnPressed());
		dash.addBoolean(isDeployBtnPressed());
	}
    dash.finalizeCluster();
}



Joystick getDriverRightJoy()
{
	return rightJoy;
}

Joystick getDriverLeftJoy()
{
	return leftJoy;
}




public boolean isMiniArmsBtnPressed()
{
	double thresh = 2.50;

	if(ds.getAnalogIn(AIM_BTN) > thresh)
		return true;
	return false;
}

public boolean isDeployBtnPressed()
{
	double thresh = 2.50;

	if(ds.getAnalogIn(DEPLOY_BTN) > thresh)
		return true;
	return false;
}












}
