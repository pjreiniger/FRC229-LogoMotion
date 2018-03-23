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
public class DriveToPosition implements AutonCommand
{
    private double x;
    private double y;
    private double maxSpeed;
    private double maxError;

    private WaypointDriver wpDriver;
    
    public DriveToPosition(double x, double y, double maxSpeed, double maxError){
        this.x = x;
        this.y = y;
        this.maxSpeed = maxSpeed;
        this.maxError = maxError;
    }
    public void setup() {
	wpDriver.SetDesiredWaypoint(x,y);
    }

    public boolean execute() {

	boolean finished = false;

	wpDriver.DriveToSetpoint(maxSpeed);

	double error = wpDriver.CurrentError();

	if(Utils.inInclusiveRange(error, 0, maxError))
	{
		finished = true;
		//printf("cury = %f, curx = %f, desY = %f, desX = %f \r\n", GetYft(), GetXft(), y, x);
	}

	return finished;
    }

    public void finish() {
        
    }

}
