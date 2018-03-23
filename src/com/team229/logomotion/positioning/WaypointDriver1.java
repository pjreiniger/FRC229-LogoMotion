/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.positioning;

import com.team229.logomotion.utils.Utils;
import com.sun.squawk.util.MathUtils;
import com.team229.logomotion.modules.DriveTrain;
import com.team229.logomotion.utils.OurPID;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;

/**
 *
 * @author PJ
 */
public class WaypointDriver1 {

    double targetX,targetY;
    double targetAngle;//final angle only, will not correct till its at last waypoint

    DriveTrain driveTrain;
    
    double distAway;
    
    OurPID anglePID = new OurPID(.009, 0, 0);
    OurPID distPID = new OurPID(.6, 0, 0);


    public WaypointDriver1()
    {
        distAway = 1000000000;
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



            double speedTurnK = 3;//5;

            double currX = Positioning.GetXft();
            double currY = Positioning.GetYft();
            double currAngle = Positioning.GetAngle();
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


            double distControl = distPID.calcControl(distError);//distKp*distError  +  distKd*dDErr  +  distKi*sumDError;
            double angleControl = anglePID.calcControl(angleError);//*angleError  +  angleKd*dAErr  +  angleKi*sumAError;

            double leftM = distControl + angleControl + angleControl*distControl*speedTurnK;
            double rightM = distControl - angleControl- angleControl*distControl*speedTurnK;

            leftM = Utils.LimitVal(leftM, maxSpeed);
            rightM = Utils.LimitVal(rightM, maxSpeed);

            driveTrain.setLeftRight(leftM, rightM);
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
        return makeBetween180s( targetAngle -  Positioning.GetAngle() );
    }




}
