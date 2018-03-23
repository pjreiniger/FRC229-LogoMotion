/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

/**
 *
 * @author PJ
 */
public interface AutonCommand {
    public void setup();
    public boolean execute();
    public void finish();
}
