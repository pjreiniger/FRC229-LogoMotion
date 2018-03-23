/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team229.logomotion;


import com.team229.logomotion.utils.PortMap;
import com.team229.logomotion.auton.AutonController;
import com.team229.logomotion.auton.AutonMaker;
import com.team229.logomotion.modules.DriveTrain;
import com.team229.logomotion.modules.Elevator;
import com.team229.logomotion.modules.LineFollower;
import com.team229.logomotion.modules.Minibot;
import com.team229.logomotion.modules.RollerClaw;
import com.team229.logomotion.userInterface.LEDDriver;
import com.team229.logomotion.userInterface.OperatorInterface;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Derivative extends SimpleRobot {


    private DriveTrain base;
    private LineFollower lineFollower;
    private Minibot minibot;

    private RollerClaw rc;
    private Elevator elev;

    private final LEDDriver leds;



    private final Compressor compressor;

    private final AutonMaker autonMaker;
    private final AutonController auton;


    private final Timer loopTimer;
    private double LOOP_WAIT_TIME = .05;


    private OperatorInterface oi;
   
    
    public Derivative()
    {
        super();

	oi = OperatorInterface.getInstance();


	lineFollower = new LineFollower();
        minibot	= new Minibot();
        leds = new LEDDriver();

        try
        {
            elev = new Elevator();
            rc = new RollerClaw();
            base = new DriveTrain();
        }
        catch(CANTimeoutException e)
        {
            e.printStackTrace();
        }

        auton = new AutonController();
        autonMaker = new AutonMaker(auton, PortMap.TIER_SW, PortMap.START_POS_SW, PortMap.ROTARY_SW);

        compressor = new Compressor(PortMap.PRESSURE_SWITCH, PortMap.COMPRESSOR_RELAY);
        compressor.start();

        loopTimer = new Timer();
        loopTimer.start();
    }
    
    
    public void autonomous() {
        autonMaker.WriteMode();


        while(isAutonomous())
        {
            auton.handle();

            //dashPack.updateDashboard();
            Timer.delay(LOOP_WAIT_TIME);
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        //Camera* cam = Camera::getInstance();

        loopTimer.reset();

        while (isOperatorControl())
        {
            base.control();
            rc	.control();
            elev.control();
            leds.control();
            minibot.control();

            oi.setTubeLights(rc.getTubeSw());


            //dashPack.updateDashboard();

            //printf("Loop took %f secodns \r\n", loopTimer.Get());
            loopTimer.reset();


            Timer.delay(LOOP_WAIT_TIME);
        }
    }

    protected void disabled()
    {
        while(isDisabled())
        {
            //dashPack.updateDashboard();
            autonMaker.WriteMode();


            Timer.delay(0.1);
        }
    }
}
