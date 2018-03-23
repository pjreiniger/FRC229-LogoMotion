/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.utils.Utils;
import com.team229.logomotion.positioning.WaypointDriver;
import com.team229.logomotion.modules.Elevator;

/**
 *
 * @author PJ
 */
public class MoveElevator implements AutonCommand
{
    private double height;
    private double maxSpeed;
    private double db;


    private Elevator elev;
    
    public MoveElevator(double height, double maxSpeed, double db){
        this.height = height;
        this.db = db;
        this.maxSpeed = maxSpeed;
    }
    public void setup() {
    }

    public boolean execute()
    {
        boolean finished = false;
	double error = height - elev.getPotInch();

	if(height < Elevator.BOT_HEIGHT)
		height = Elevator.BOT_HEIGHT;

	if(Utils.inInclusiveRange(error, 0, db))
	{
		finished = true;
	}
	else
	{
		double speed = elev.getSpeedToHeight(height);
		speed = Utils.LimitVal(speed, maxSpeed);

		elev.setSpeed(speed);
	}

	return finished;
    }

    public void finish() {
        elev.setSpeed(0.0);
    }

}
