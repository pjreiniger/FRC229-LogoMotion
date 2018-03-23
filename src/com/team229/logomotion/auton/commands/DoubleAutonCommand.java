/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

public class DoubleAutonCommand implements AutonCommand {

    private AutonCommand c1 = null;
    private AutonCommand c2 = null;
    
    public DoubleAutonCommand()
    {
    }

    public void setCommands(AutonCommand c1, AutonCommand c2)
    {
        this.c1 = c1;
        this.c2 = c2;
    }
    public void setup() {
        c1.setup();
        c2.setup();
    }

    public boolean execute() {
        c2.execute();
        return c1.execute();
    }

    public void finish() {
        c1.finish();
        c2.finish();
    }

}
