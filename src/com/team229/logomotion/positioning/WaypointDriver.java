/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.positioning;

import com.team229.logomotion.utils.Utils;
import com.sun.squawk.util.MathUtils;
import com.team229.logomotion.modules.DriveTrain;

/**
 *
 * @author PJ
 */
public class WaypointDriver {

    double targetX,targetY;
    double targetAngle;//final angle only, will not correct till its at last waypoint

    DriveTrain driveTrain;

    double distAway;

    double lastAError, lastDError;
    double sumAError, sumDError;

    Positioning pos;


public WaypointDriver()
{
    lastAError = lastDError = 0;
    sumAError = sumDError = 0;
    distAway = 1000000000;

    pos = Positioning.getInstance();
    pos.start();
}

public void SetDesiredWaypoint(double x, double y)
{
	targetX = x;
	targetY = y;
}

public void SetDesiredAngle(double angle)
{
	targetAngle = angle;
}

public void SetDriveTrain(DriveTrain d)
{
	driveTrain = d;
}

public void DriveToSetpoint(double maxSpeed)
{
	maxSpeed = Utils.LimitVal(maxSpeed, 1);


	//These are the PID constants for t
	double distKp = .6;
	double distKd = 0;//-.01;
	double distKi = 0;

	double angleKp = .009;//.005;
	double angleKd = 0;//-.05;
	double angleKi = 0;

	double speedTurnK = 3;//5;

	double currX = pos.GetXft();
	double currY = pos.GetYft();
	double currAngle = pos.GetAngle();
	boolean dReverse=false;




	currAngle = makeBetween180s(currAngle);

	double angleError =  Utils.RadianToDegree(MathUtils.atan2(targetX-currX,targetY-currY)) - currAngle;
	angleError = makeBetween180s(angleError);

	//It is easier to drive backwards
	if (angleError>90)
	{
		angleError = -(180-angleError);
		dReverse=true;
	}
	if (angleError<-90)
	{
		angleError = (180+angleError);
		dReverse=true;
	}

	distAway = Math.sqrt((targetX-currX)*(targetX-currX)+(targetY-currY)*(targetY-currY));

	double distError = distAway * Math.cos(Utils.DegreeToRadian(angleError));

	if (dReverse)
		distError= -distError;

	double dAErr = angleError-lastAError;
	double dDErr = distError-lastDError;

	double distControl = distKp*distError  +  distKd*dDErr  +  distKi*sumDError;
	double angleControl = angleKp*angleError  +  angleKd*dAErr  +  angleKi*sumAError;



	double leftM = distControl + angleControl + angleControl*distControl*speedTurnK;
	double rightM = distControl - angleControl- angleControl*distControl*speedTurnK;

	//printf("Before: l=%f, r=%f", leftM, rightM);

	leftM = Utils.LimitVal(leftM, maxSpeed);
	rightM = Utils.LimitVal(rightM, maxSpeed);
	//printf("\t After: l=%f, r=%f", leftM, rightM);


	driveTrain.setLeftRight(leftM, rightM);
	//driveTrain->setLeftRight(1.0, 1.0);

	lastAError = angleError;
	lastDError = distError;
	sumAError += angleError;
	sumDError += distError;
}


public double makeBetween180s(double inAngle)
{
	while (inAngle>180)
		inAngle-=360;
	while (inAngle<=-180)
		inAngle+=360;

	return inAngle;
}





public double CurrentError()
{
	return distAway;
}

public double CurrentAngleError()
{
    return makeBetween180s( targetAngle -  pos.GetAngle() );
}




}
