package com.team229.logomotion.auton;

import com.team229.logomotion.auton.commands.AutonCommand;
import com.team229.logomotion.positioning.WaypointDriver;
import com.team229.logomotion.auton.commands.DoNothing;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import java.util.Vector;

public class AutonController {
    public static final double AVG_ELEV_SPEED    = 0;
    public static final double AVG_ELEV_DB       = 0;
    public static final double AVG_DRIVE_SPEED   = 0;
    public static final double AVG_DIST_DB       = 0;

    public static final double AVG_TURN_SPEED   = 0;
    public static final double AVG_ANGLE_DB     = 0;

    private WaypointDriver wpDriver;

    private Vector cmdList;
    private AutonCommand curCmd;

    private Timer t;
    private Timer alwaysSpitTimer;

    public AutonController()
    {
    	
    }

    public void handle()
    {
        boolean finished = curCmd.execute();
        if(finished == true)
	{
		//Reset the timer after each step
		t.reset();

		// This command is done, go get the next one
		if(cmdList.size() > 0 )
		{
                    //Finish up this guy
                    curCmd.finish();

                    //Get the next one
                    curCmd = (AutonCommand) cmdList.firstElement();
                    cmdList.removeElement(curCmd);

                    //Setup the nextone
                    curCmd.setup();
		}
		else
			curCmd = new DoNothing();

	}
    }

    void reset() {
        cmdList.removeAllElements();
    }

    void addCommand(AutonCommand cmd) {
        cmdList.addElement(cmd);
    }


}
