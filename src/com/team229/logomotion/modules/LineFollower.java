
package com.team229.logomotion.modules;

import com.team229.logomotion.utils.PortMap;
import com.team229.logomotion.userInterface.OperatorInterface;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;


public class LineFollower {
    DriveTrain base;
    OperatorInterface oi;

    DigitalInput leftLineSensor;			// digital inputs for line tracking sensors
    DigitalInput middleLineSensor;
    DigitalInput rightLineSensor;

    Timer onLineTimer;
    Timer offLineTimer;

    public LineFollower()
    {
        this(
            PortMap.RIGHT_LINE_SENSOR,
            PortMap.MIDDLE_LINE_SENSOR,
            PortMap.LEFT_LINE_SENSOR);
    }
    public LineFollower(int right, int mid, int left)
    {
        rightLineSensor = new DigitalInput(right);
        middleLineSensor = new DigitalInput(mid);
        leftLineSensor = new DigitalInput(left);

        onLineTimer = new Timer();
        onLineTimer.start();

        offLineTimer = new Timer();
        offLineTimer.start();
    }

    public void printToDash()
    {
        Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();

        dash.addCluster();
        {
            dash.addBoolean(isLeftTripped());
            dash.addBoolean(isMiddleTripped());
            dash.addBoolean(isRightTripped());
        }
        dash.finalizeCluster();
    }

    public boolean isRightTripped()
    {
        return rightLineSensor.get();
    }
    public boolean isMiddleTripped()
    {
        return middleLineSensor.get();
    }
    public boolean isLeftTripped()
    {
        return leftLineSensor.get();
    }

    public double getSteerSpeed()
    {
        double steeringGain = .2;

        int lastBinVal = 0;
        double lastGain = 0.0;
        int curBinVal;


        int rightVal 	= isRightTripped() 	? 1 : 0;
        int middleVal 	= isMiddleTripped() ? 1 : 0;
        int leftVal 	= isLeftTripped() 	? 1 : 0;

        curBinVal = leftVal*4 + middleVal*2 + rightVal;


        double diffSpeed;


        switch (curBinVal)
        {
        case 0:					//Nothing seen, do what you did last
            diffSpeed = lastGain;
            break;
        case 1:					//Right tripped, turn
            diffSpeed = steeringGain;
            break;
        case 2:					//Center tripped, dont turn
            diffSpeed = 0;
            break;
        case 4:					//Left tripped, turn
            diffSpeed = -steeringGain;
            break;
        default:
            diffSpeed = lastGain;
        }


        //printf("binval=%i, diff = %f\r\n", curBinVal, diffSpeed);


        //Only change the laast value if you have a new non-zero one
        if(curBinVal != 0)
        {
            //printf("curBinal=%i\r\n", curBinVal);
            lastBinVal = curBinVal;
        }

        lastGain = diffSpeed;

        return diffSpeed;
    }
}
