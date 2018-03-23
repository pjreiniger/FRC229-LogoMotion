/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

/**
 *
 * @author PJ
 */
public class DoNothing implements AutonCommand
{
    public DoNothing(){
        
    }
    public void setup() {
    }

    public boolean execute() {
        return true;
    }

    public void finish() {
    }

}
