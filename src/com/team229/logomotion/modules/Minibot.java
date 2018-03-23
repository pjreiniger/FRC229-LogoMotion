/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.modules;

import com.team229.logomotion.utils.PortMap;
import com.team229.logomotion.userInterface.OperatorInterface;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author PJ
 */
public class Minibot {
    DoubleSolenoid armSol;
    DoubleSolenoid deploySol;


    DigitalInput limSw;

    OperatorInterface oi;


    boolean armDown = false;
    boolean miniOut = false;

    public Minibot()
    {
        this(
                PortMap.MINIBOT_DEPLOY_SOL_A,
                PortMap.MINIBOT_DEPLOY_SOL_B,
                PortMap.MINIBOT_EXTEND_SOL_A,
                PortMap.MINIBOT_EXTENT_SOL_B,
                PortMap.MINIBOT_LIMIT_SWITCH);
    }

    public Minibot(int as1, int as2, int dep1, int dep2, int lsw)
    {
        armSol = new DoubleSolenoid(as1, as2);
        deploySol = new DoubleSolenoid(dep1, dep2);

        limSw = new DigitalInput(lsw);



        oi = OperatorInterface.getInstance();
    }

    public void control()
    {
        controlServos();
        controlPnuematic();
    }

    public void controlServos()
    {
        boolean wasReleased = true;

        if(oi.isMiniArmsBtnPressed())
        {
            //printf("btn pressed\r\n");
            if(wasReleased)
            {
                armDown = !armDown;

                wasReleased = false;
            }
        }
        else
            wasReleased = true;

        if(armDown)
        {
            flingAimArms();
        }
        else
        {
            holdAimArms();
        }
    }

    public void controlPnuematic()
    {
        boolean wasReleased = true;
        if(oi.isDeployBtnPressed())
        {
            //printf("btn pressed\r\n");
            if(wasReleased)
            {
                miniOut = !miniOut;

                wasReleased = false;
            }
        }
        else
            wasReleased = true;


        if(miniOut)
        {
            deployMiniBot();
        }
        else
        {
            holdMiniBot();
        }
    }

    public void deployMiniBot()
    {
        deploySol.set(DoubleSolenoid.Value.kForward);
    }

    public void holdMiniBot()
    {
        deploySol.set(DoubleSolenoid.Value.kReverse);
    }


    public void flingAimArms()
    {
        armSol.set(DoubleSolenoid.Value.kReverse);
    }



    public void holdAimArms()
    {
        armSol.set(DoubleSolenoid.Value.kForward);
    }


    public void printToDash()
    {
        Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();

        dash.addCluster();
        {
            //dash.addboolean (rightSW.Get());
            dash.addBoolean (armDown);
            dash.addBoolean (limSw.get());
        }
        dash.finalizeCluster();
    }





}
