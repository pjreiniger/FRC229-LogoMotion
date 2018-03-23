/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author PJ
 */
public class WaitCommand implements AutonCommand
{
    private Timer timer;
    private double waitTime;

    public WaitCommand(double waitTime){
        timer = new Timer();
        this.waitTime = waitTime;
    }
    public void setup() {
        timer.start();
    }

    public boolean execute() {
        if(timer.get() >= waitTime)
            return true;
        return false;
    }

    public void finish() {
        timer = null;
    }

}
