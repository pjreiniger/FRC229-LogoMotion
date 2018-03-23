/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.positioning.Positioning;
import com.team229.logomotion.utils.Utils;
import com.team229.logomotion.modules.DriveTrain;
import com.team229.logomotion.modules.LineFollower;

/**
 *
 * @author PJ
 */
public class FollowLineForDist implements AutonCommand
{
    private DriveTrain base;
    private LineFollower lineFollower;

    private double startX;
    private double startY;
    private double desDist;
    
    public FollowLineForDist(double startX, double startY, double desDist){
        this.startX = startX;
        this.startY = startY;
        this.desDist = desDist;
    }
    public void setup() {
    }

    public boolean execute() {
        	double maxSpeed = .65;//.6;
	double speed = .5;//.44;
	double db = .1;

	double curDist = getDistance(startX, startY);

	boolean finished = false;

	if(Utils.inInclusiveRange(curDist, desDist, db))
	{
		base.stop();
		finished = true;
	}
	else
	{
            double steerGain = lineFollower.getSteerSpeed();

            double leftSpeed = speed + steerGain;
            double rightSpeed = speed - steerGain;

            leftSpeed = Utils.LimitVal(leftSpeed, maxSpeed);
            rightSpeed = Utils.LimitVal(rightSpeed, maxSpeed);


            base.setLeftRight(leftSpeed, rightSpeed);

            //printf("Right=%f,\tleft=%f, \tTurn Gain = %f, dist = %f\r\n", rightSpeed, leftSpeed, steerGain, curDist);
	}

	return finished;
    }

    public void finish() {
    }
    
    private double getDistance(double sx, double sy)
    {
	double dist = 0;
	double cx = Positioning.GetXft();
	double cy = Positioning.GetYft();

	dist = Math.sqrt( (sx-cx)*(sx-cx) + (sy-cy)* (sy-cy) );


	return dist;
    }

}
