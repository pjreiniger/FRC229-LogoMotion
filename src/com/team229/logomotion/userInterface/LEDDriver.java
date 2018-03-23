/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.userInterface;

import com.team229.logomotion.utils.PortMap;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author PJ
 */
public class LEDDriver {
    private Relay redLEDs;
    private Relay whiteLEDs;
    private Relay blueLEDs;
    
    private Timer t;
    private OperatorInterface oi;

 

    public LEDDriver()
    {
        this(PortMap.RED_LEDS,
                PortMap.WHITE_LEDS,
                PortMap.BLUE_LEDS);
    }
    public LEDDriver(int red, int white, int blue)
    {
        oi = OperatorInterface.getInstance();
        redLEDs     = new Relay(red);
        whiteLEDs   = new Relay(white);
        blueLEDs    = new Relay(blue);

        t = new Timer();
        t.start();
    }


    public void control()
    {
        selectLights(oi.isTriangleBtnPressed(), oi.isCircleBtnPressed(), oi.isSquareBtnPressed());
    }

    public void TurnAllOn()
    {
        redLEDs.set(Relay.Value.kOn);
        whiteLEDs.set(Relay.Value.kOn);
        blueLEDs.set(Relay.Value.kOn);
    }

    public void TurnAllOff()
    {
        redLEDs.set(Relay.Value.kOff);
        whiteLEDs.set(Relay.Value.kOff);
        blueLEDs.set(Relay.Value.kOff);
    }

    private void selectLights(boolean red, boolean white, boolean blue)
    {

        //Red Triangle
        if(red)
        {
            redLEDs.set(Relay.Value.kOn);
        }
        else
            redLEDs.set(Relay.Value.kOff);

        //White Circle
        if(white){

            whiteLEDs.set(Relay.Value.kOn);
        }
        else
            whiteLEDs.set(Relay.Value.kOff);


        //Blue Square
        if(blue)
        {
            blueLEDs.set(Relay.Value.kOn);
        }
        else
            blueLEDs.set(Relay.Value.kOff);
    }

    int flashCtr = 0;
    
    private static final int RED_ON     = 0;
    private static final int WHITE_ON   = 1;
    private static final int BLUE_ON    = 2;
    private static final int ALL_ON     = 3;
    private static final int ALL_OFF    = 4;

    private static final double SINGLE_LIGHT_TIME = .3; //seconds
    private static final double FLASH_TIME = .2;

    private int lightShowState = RED_ON;
    
    public void lightShow()
    {
        switch(lightShowState)
        {
        case RED_ON:
            selectLights(true, false, false);
            if(t.get() > SINGLE_LIGHT_TIME)
            {
                t.reset();
                lightShowState = WHITE_ON;
            }
            break;
        case WHITE_ON:
            selectLights(false, true, false);
            if(t.get() > SINGLE_LIGHT_TIME)
            {
                t.reset();
                lightShowState = BLUE_ON;
            }
            break;
        case BLUE_ON:
            selectLights(false, false, true);
            if(t.get() > SINGLE_LIGHT_TIME)
            {
                t.reset();
                lightShowState = ALL_ON;
            }
            break;
        case ALL_OFF:
            selectLights(false, false, false);
            if(t.get() > FLASH_TIME)
            {
                t.reset();
                lightShowState = ALL_ON;
            }
            break;

        case ALL_ON:
            selectLights(true, true, true);
            if(t.get() > FLASH_TIME)
            {
                t.reset();
                flashCtr ++;
                if(flashCtr > 6)
                {
                    flashCtr = 0;
                    lightShowState = RED_ON;
                }
                else
                    lightShowState = ALL_OFF;
            }
            break;
        }
    }


}
