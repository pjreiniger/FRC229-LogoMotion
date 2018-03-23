/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.positioning;

import com.team229.logomotion.utils.Utils;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author PJ
 */
public class Positioning implements Runnable{

    private static Encoder rightEncoder;
    private static Encoder leftEncoder;


    private static final double ticksPerFootLeft	= 1770;
    private static final double ticksPerFootRight	= 1790;


    private AnalogChannel angularRate;

    private final static Positioning me = new Positioning();
    Thread updateThread;


    private static double x,y;
    private static double lastRdist, lastLdist;

    private static double angle;
    private static double initAngle;

    private static double CurrentAngularRate;

    private static double wheelDiff = 0;

    private Positioning()
    {
        
    }

    public static Positioning getInstance()
    {
        return me;
    }

    public void start()
    {
        if(updateThread == null)
            updateThread = new Thread(this);

        rightEncoder.start();
        leftEncoder.start();
        
        updateThread.start();
    }
    public void stop()
    {
        rightEncoder.stop();
        leftEncoder.stop();
        
        updateThread = null;
    }
    
    public void run() {

	//const double a = .3;
	double timePerLoop = 0.001;


	int loops = 0;
	//double lastARate = angularRate.GetValue();

	while (true)
	{
            if (rightEncoder==null||leftEncoder==null)
            {
                    continue;
            }
            double rightDist, leftDist;
            double dRight, dLeft;			//Difference between this and last
            double dAvg;					//The average between left and right

            rightDist = rightEncoder.getRaw()/ticksPerFootRight;
            leftDist  = leftEncoder.getRaw()/ticksPerFootLeft;

            dRight = rightDist - lastRdist;
            dLeft = leftDist - lastLdist;

            dAvg = (dRight + dLeft)/2;


            angle = initAngle + (double)(leftDist - rightDist + wheelDiff)/(double)(3.14159*22.0/12.0)*(180.0);

            double radAng = Utils.DegreeToRadian(angle);

            double cosA = Math.cos(radAng);
            double sinA = Math.sin(radAng);

            double gDx = sinA * dAvg;
            double gDy = cosA * dAvg;


            x+=gDx;
            y+=gDy;

            lastRdist = rightDist;
            lastLdist = leftDist;



            if (loops%3000==0)
            {
            }
            loops++;
	}
    }


    public static double GetXft()
    {
        return x;
    }

    public static double GetYft()
    {
        return y;
    }




    public static double GetRightFt()
    {
        return rightEncoder.getRaw()/ticksPerFootRight;
    }

    public static double GetLeftFt()
    {
        return leftEncoder.getRaw()/ticksPerFootLeft;
    }



    public static double GetRightRaw()
    {
        return rightEncoder.getRaw();
    }

    public static double GetLeftRaw()
    {
        return leftEncoder.getRaw();
    }

    public static double GetAngle()
    {
        double a = angle;

        while(a > 360) 		a-=360;
        while(a < 0)		a+=360;

        return a;
    }


    public static void SetPosition(double startX, double startY, double startAngle) {
    }





}
