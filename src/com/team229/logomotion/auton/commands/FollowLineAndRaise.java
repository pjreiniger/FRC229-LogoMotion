/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.auton.commands;

import com.team229.logomotion.auton.AutonController;

/**
 *
 * @author PJ
 */
public class FollowLineAndRaise extends DoubleAutonCommand
{
    public FollowLineAndRaise(double startX, double startY, double desDist, double height){

        AutonCommand c1 = new FollowLineForDist(startX, startY, desDist);
        AutonCommand c2 = new MoveElevator(height, AutonController.AVG_ELEV_SPEED, AutonController.AVG_ELEV_DB);

        setCommands(c1, c2);
    }
}
