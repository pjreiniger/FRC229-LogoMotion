/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton;

import com.team229.logomotion.positioning.Positioning;
import com.team229.logomotion.auton.commands.DoNothing;
import com.team229.logomotion.auton.commands.DriveAndMoveElevator;
import com.team229.logomotion.auton.commands.FinishedCommand;
import com.team229.logomotion.auton.commands.FollowLineAndRaise;
import com.team229.logomotion.auton.commands.MoveElevator;
import com.team229.logomotion.auton.commands.ScoreTube;
import com.team229.logomotion.auton.commands.TurnToAngle;
import com.team229.logomotion.auton.commands.WaitCommand;
import com.team229.logomotion.modules.Elevator;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author PJ
 */
public class AutonMaker {

private static final double TUBE_SCORE_END_DIST =  16.9;//17.1


	double startX;
	double startY;
	

	AnalogChannel pegTierSw;
	AnalogChannel startPosSw;
	AnalogChannel rotarySw;

	AutonController autonController;


public AutonMaker(AutonController ac, int tierSw, int startSw, int rotSw)
{
	autonController = ac;
	pegTierSw = new AnalogChannel(tierSw);
	startPosSw = new AnalogChannel(startSw);
	rotarySw = new AnalogChannel(rotSw);
}


public int getAutonMode()
{
	int line = getStartLine();
	int pegH = getPegHeightNum();
	int rotSw= getRotarySwitch();

	//3 lines x 3 peg heights x 6 modes,
	int mode;
	mode = 18*(line)+6*(pegH)+rotSw;

	return mode;

}

public int getStartLine()
{
	int val = (int)startPosSw.getValue();
	int out = (val+50) / 340;

	//printf("Start line: voltage = %f, value = %i, out = %i\r\n", startPosSw.getVoltage(), startPosSw.getValue(), out);
	return out;
}

public int getPegHeightNum()
{
	int val = (int)pegTierSw.getValue();
	int out = (val+50) / 340;

	//printf("Peg tier: voltage = %f, value = %i, out = %i\r\n", pegTierSw.getVoltage(), pegTierSw.getValue(), out);
	return out;
}
public int getRotarySwitch()
{
	int val = (int)rotarySw.getValue() + 50;
	int out = val/200 +1;

	//printf("RotarySw: voltage = %f, value = %i, out = %i\r\n", rotarySw.getVoltage(), rotarySw.getValue(), out);
	return out;
}



public void WriteMode()
{
	//Reset the controller
	autonController.reset();

	//This is the method that actually creates the deque
	createMode();

}

public void createMode()
{

}




public void followLineScore(double height)
{
	double startX = 0.0;
	double startY = 0.0;

	 double startAngle = 0;
	 double lineFollowDist = TUBE_SCORE_END_DIST - 2;
	 double endY = TUBE_SCORE_END_DIST;


	Positioning.SetPosition(startX, startY, startAngle);

	double dropHeight = 2;
        
        autonController.addCommand(new FollowLineAndRaise(startX, startY, lineFollowDist, height));
        //autonController.addCommand(new
        autonController.addCommand(new TurnToAngle(0, AutonController.AVG_TURN_SPEED, AutonController.AVG_ANGLE_DB));
        autonController.addCommand(new DriveAndMoveElevator(startX, startY, height));
        autonController.addCommand(new MoveElevator(height, AutonController.AVG_ELEV_SPEED, AutonController.AVG_ELEV_DB));
        autonController.addCommand(new WaitCommand(.5));
        autonController.addCommand(new ScoreTube(height - dropHeight));
        autonController.addCommand(new DriveAndMoveElevator(startX, startY, Elevator.BOT_HEIGHT));
        autonController.addCommand(new FinishedCommand());



}

double getPegHeight(boolean goHigher)
{

	int tier = getPegHeightNum();

	double height = 0.0;

	switch(tier)
	{
	case 0:
		height = Elevator.LOW_PEG_HEIGHT;
		break;
	case 1:
		height = Elevator.MID_PEG_HEIGHT;
		break;
	case 2:
		height = Elevator.HIGH_PEG_HEIGHT;
		break;
	default:
		return Elevator.LOW_PEG_HEIGHT;
	}

	if(goHigher)
	{
		height += Elevator.MODIFIER_EXTRA_HEIGHT;
		//printf("adding extra height");
	}


	return height;
}


public void printToDash()
{
	Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();

	dash.addCluster();
	{
		dash.addDouble(getPegHeightNum());
		dash.addDouble(getStartLine());
		dash.addDouble(getRotarySwitch());
		dash.addDouble(getAutonMode());
	}
    dash.finalizeCluster();
}

}
