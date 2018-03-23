/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.utils.Utils;
import com.team229.logomotion.positioning.WaypointDriver;

/**
 *
 * @author PJ
 */
public class TurnToAngle implements AutonCommand
{
    private double angle;
    private double maxSpeed;
    private double maxError;

    private WaypointDriver wpDriver;
    
    public TurnToAngle(double angle, double maxSpeed, double maxError){
        this.angle = angle;
        this.maxSpeed = maxSpeed;
        this.maxError = maxError;
    }
    public void setup() {
    }

    public boolean execute() {
        
        boolean finished = false;

	wpDriver.SetDesiredAngle(angle);
	double error = wpDriver.CurrentAngleError();

	wpDriver.DriveToSetpoint(maxSpeed);

	//printf("error = %f, me = %f speed = %f\r\n", error, maxError, maxSpeed);
	//if(wpDriver->CurrentAngleError()<maxError && wpDriver->CurrentAngleError()>-maxError)
	if(Utils.inInclusiveRange(error, 0, maxError))
	{
		finished = true;
	}

	return finished;
    }

    public void finish() {
    }

}
