/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.positioning.Positioning;
import com.team229.logomotion.utils.Utils;
import com.team229.logomotion.positioning.WaypointDriver;

/**
 *
 * @author PJ
 */
public class TurnToPoint implements AutonCommand
{
    private double x;
    private double y;
    private double maxSpeed;
    private double maxError;

    private WaypointDriver wpDriver;

    private double angle;

    public TurnToPoint(double x, double y, double maxSpeed, double maxError){
        this.x = x;
        this.y = y;
        this.maxSpeed = maxSpeed;
        this.maxError = maxError;
    }
    public void setup() {
        calcAngle();
    }

    private double calcAngle()
    {
        double curX = Positioning.GetXft();
        double curY = Positioning.GetYft();

        double dx = x - curX;
        double dy = y - curY;

        angle = Math.tan(x/y);
        return angle;
    }

    public boolean execute() {

        boolean finished = false;

        calcAngle();

	wpDriver.SetDesiredAngle(angle);
	double error = wpDriver.CurrentAngleError();

	wpDriver.DriveToSetpoint(maxSpeed);

	if(Utils.inInclusiveRange(error, 0, maxError))
	{
            finished = true;
	}

	return finished;
    }

    public void finish() {
    }


}
