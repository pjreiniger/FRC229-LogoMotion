/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.userInterface;

/**
 *
 * @author PJ
 */
public class ToggleButton {

    boolean lastState = false;
    boolean outputHigh;
    boolean stateChanged = false;

    public ToggleButton(boolean startHigh)
    {
        outputHigh = startHigh;
    }

    public void update(boolean thisState)
    {
        if(thisState != lastState)
        {
            outputHigh = !outputHigh;
            lastState = thisState;
            stateChanged = true;
        }
        else
            stateChanged = false;
    }

    public boolean isHigh()
    {
        return outputHigh;
    }
    public boolean didStateChange()
    {
        return stateChanged;
    }
}
