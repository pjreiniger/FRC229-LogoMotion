/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.modules;

import com.team229.logomotion.utils.PortMap;
import com.team229.logomotion.userInterface.OperatorInterface;
import com.team229.logomotion.utils.OurPID;
import com.team229.logomotion.utils.Utils;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author PJ
 */
public class Elevator {
    public static int MODIFIER_EXTRA_HEIGHT	= 8;

    public static int LOW_PEG_HEIGHT	= 30;
    public static int MID_PEG_HEIGHT	= 66;//58
    public static int HIGH_PEG_HEIGHT	= 96;//92

    public static int BOT_HEIGHT	 = 30;		//Height the


    private OperatorInterface oi;

    private SpeedController elevMot;

    private Encoder encoder;
    private DigitalInput bottomLimitSW;

    private double elevatorSpeed = 0;

    private OurPID elevatorPID = new OurPID(-.4, 0, 0);

    public Elevator() throws CANTimeoutException
    {
        this(   PortMap.ELEVATOR_MOTOR,
                PortMap.ELEVATOR_ENC_A,
                PortMap.ELEVATOR_ENC_B,
                PortMap.ELEVATOR_LSW);
    }

    public Elevator(int emot, int enc_a, int enc_b, int limsw)  throws CANTimeoutException
    {
        oi = OperatorInterface.getInstance();

        elevMot = new CANJaguar(emot);
        encoder = new Encoder(enc_a, enc_b);
        bottomLimitSW = new DigitalInput(limsw);
    }


    public void control()
    {
         double dontCareDB = .1;

        double stick = oi.getElevatorStick();
        double speed = 0.0;


        //If the stick isnt moved look at the buttons
        if(Utils.inInclusiveRange(stick, 0.0, dontCareDB))
        {
                double desHeight = calcDesHeight();

                //This means you dont have any buttons pressed
                if(desHeight != -999)
                        speed = getSpeedToHeight(desHeight);
        }
        else
        {
                speed = stick;
        }

        if(bottomLimitSW.get())
        {
                encoder.reset();
                //printf("reset e;ev\r\n ");
        }


        //printf("Final elev speed = %f\r\n", speed);
        setSpeed(speed);

    }

    public double dampDownNoStallUp(double speed)
    {
        //The bigger the number the faster it goes
         double downDamper = .5;//.4;

        //The more negative the number the more you have to push the stick to move
         double avgStallVal = -.5;

        //Moving down, damper speed
        if(speed > 0)
        {
                speed *= downDamper;
        }
        //Moving up
        else
        {
                //You want the speed to be more negative then the stall
                //So it can overcome it
                if(speed > avgStallVal)
                        speed = 0;
        }

        return speed;
    }

    public double calcDesHeight()
    {
        double height = 0.0;


        if(oi.isAutoLowPegBtnPressed())
        {
            height = LOW_PEG_HEIGHT;
        }
        else if(oi.isAutoMidPegBtnPressed())
        {
            height = MID_PEG_HEIGHT;
        }
        else if(oi.isAutoHighPegBtnPressed())
        {
            height = HIGH_PEG_HEIGHT;
        }
        else if(oi.isAutoLoaderBtnPressed())
        {
            height = BOT_HEIGHT;
        }
        //If none of the buttons are pressed dont try to go anywhere else
        else
        {
                return -999;
        }

        if(oi.isPegHeightModifierBtnPressed())
        {
                height += MODIFIER_EXTRA_HEIGHT;
        }

        //printf("Des elev hieght = %f\r\n", height);
        return height;
    }

    public double getSpeedToHeight(double height)
    {
        double speed = elevatorPID.calcControl(height - getPotInch());
        return speed;
    }


    public double getPotRaw()
    {
        return encoder.get();
    }
    public double getPotInch()
    {

        int  TOP_CLICKS	= 2600;	//encoder clicks to the upper limit
        int  TOP_HEIGHT	= 104;		//Height that the arm reaches at TOP_CLICKS


        double ticksPerInch = TOP_CLICKS/(TOP_HEIGHT-BOT_HEIGHT);

        double inches = getPotRaw()/ticksPerInch + BOT_HEIGHT;
        //printf("pot raw = %f, inch = %f\r\n", getPotRaw(), inches);


        return inches;
    }


    public void setSpeed(double speed)
    {
        if(elevatorSpeed != speed)
        {
            speed = dampDownNoStallUp(speed);
            elevatorSpeed = speed;

            System.out.println("Updated elevator speed to: " + speed);
            elevMot.set(elevatorSpeed);
        }
        //printf("Elevator final speed = %f\r\n", speed);

        //NEGATIVE VALUE FOR UP
    }

    public void stop()
    {
        setSpeed(0.0);
    }



    public void printToDash()
    {
        Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();

        dash.addCluster();
        {
                dash.addDouble(elevatorSpeed);
                dash.addDouble(getPotRaw());
                dash.addDouble(getPotInch());
                dash.addBoolean(bottomLimitSW.get());
        }
        dash.finalizeCluster();

    }



}
