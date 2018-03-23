/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.auton.AutonController;
import com.team229.logomotion.modules.RollerClaw;

/**
 *
 * @author PJ
 */
public class ScoreTube implements AutonCommand
{
    AutonCommand moveElev;
    RollerClaw rc;

    public ScoreTube(double lowerToHeight){
        moveElev = new MoveElevator(
                lowerToHeight,
                AutonController.AVG_ELEV_SPEED,
                AutonController.AVG_ELEV_DB);
        
    }
    public void setup() {
        moveElev.setup();
    }

    public boolean execute() {
        boolean finished = false;


	rc.spitOut();
	finished = moveElev.execute();

	return finished;
    }

    public void finish() {
        moveElev.finish();
    }

}
