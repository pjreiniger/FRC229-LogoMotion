/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.auton.AutonController;
import com.team229.logomotion.modules.Elevator;
import com.team229.logomotion.modules.RollerClaw;

/**
 *
 * @author PJ
 */
public class DriveAndMoveElevator extends DoubleAutonCommand
{
    public DriveAndMoveElevator(double x, double y, double height){
        
	double driveSpeed = AutonController.AVG_DRIVE_SPEED * .90;	//Scale it down just a tad to be safe
	double driveDB = AutonController.AVG_DIST_DB;

	double elevSpeed = AutonController.AVG_ELEV_SPEED * .8;
	double elevDB	= AutonController.AVG_ELEV_DB;

        AutonCommand c1 = new DriveToPosition(x, y, driveSpeed, driveDB);
        AutonCommand c2 = new MoveElevator(height, elevSpeed, elevDB);
        
        setCommands(c1, c2);
    }
}
