package com.team229.logomotion.modules;



import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Dashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import com.team229.logomotion.utils.PortMap;
import com.team229.logomotion.userInterface.OperatorInterface;
import com.team229.logomotion.userInterface.ToggleButton;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class DriveTrain {
    SpeedController leftMotor_a;
    SpeedController leftMotor_b;
    SpeedController rightMotor_a;
    SpeedController rightMotor_b;

    DoubleSolenoid shifterSol;

    RobotDrive base;


    OperatorInterface oi;

    boolean highGear = false;

    double leftSpeed = 0;
    double rightSpeed = 0;

    ToggleButton shifterBtn = new ToggleButton(false);

    public DriveTrain() throws CANTimeoutException
    {
        this(
                    PortMap.LEFT_DRIVE_MOT_A,
                    PortMap.LEFT_DRIVE_MOT_B,
                    PortMap.RIGHT_DRIVE_MOT_A,
                    PortMap.RIGHT_DRIVE_MOT_B,
                    PortMap.SHIFTER_SOL_A,
                    PortMap.SHIFTER_SOL_B);
    }


    public DriveTrain(	int lefta,
                                                    int leftb,
                                                    int righta,
                                                    int rightb,
                                                    int shifterIn,
                                                    int shifterOut) throws CANTimeoutException
    {
        oi = OperatorInterface.getInstance();
        leftMotor_a     = new CANJaguar(lefta);
        leftMotor_b     = new CANJaguar(leftb);
        rightMotor_a    = new CANJaguar(righta);
        rightMotor_b    = new CANJaguar(rightb);

        shifterSol = new DoubleSolenoid(shifterIn, shifterOut);

        highGear = false;

        base = new RobotDrive(leftMotor_a, leftMotor_b, rightMotor_a, rightMotor_b);

    }

    public void control()
    {
        double DAMPING_FACTOR = .3f;

        double rightSpeed = 0;
        double leftSpeed = 0;

        controlShifter();
        if(oi.isDamperPressed())
        {
            leftSpeed = oi.getLeft_y()*DAMPING_FACTOR;
            rightSpeed = oi.getRight_y()*DAMPING_FACTOR;
        }
        else
        {
            leftSpeed = oi.getLeft_y();
            rightSpeed = oi.getRight_y();
        }

        setLeftRight(leftSpeed, rightSpeed);
    }

    /**This must be called to activly engage the shifter in either gear.
     * If this is not called they might drift into neutral or something
     */
    public void runShifter()
    {
        System.out.println("Shifter state changing");
        if(highGear)
        {
            shiftHighGear();
        }
        else
        {
            shiftLowGear();
        }
    }

    public void controlShifter()
    {
        shifterBtn.update(oi.isShifterPressed());
        if(shifterBtn.didStateChange())
        {
            highGear = shifterBtn.isHigh();
            //This does the actual engaging of the gears and such
            runShifter();
        }


    }

    public void setHighGear(boolean yes)
    {
        highGear = yes;
    }

    public void shiftHighGear()
    {
        shifterSol.set(DoubleSolenoid.Value.kReverse);
    }
    public void shiftLowGear()
    {
        shifterSol.set(DoubleSolenoid.Value.kForward);
    }



    public void setLeftRight(double newLeft, double newRight)
    {
        if(newLeft != leftSpeed || newRight != newRight)
        {
            leftSpeed = newLeft;
            rightSpeed = newRight;
            base.setLeftRightMotorOutputs(rightSpeed, leftSpeed);
        }
    }

    public void stop()
    {
        setLeftRight(0.0, 0.0);
    }

    public double getRightSpeed()
    {
        return rightSpeed;
    }

    public double getLeftSpeed()
    {
        return leftSpeed;
    }




    public void printToDash()
    {
        Dashboard dash = DriverStation.getInstance().getDashboardPackerLow();

        dash.addCluster();
        {
            dash.addDouble(getRightSpeed());
            dash.addDouble(getLeftSpeed());

            dash.addBoolean(oi.isDamperPressed());
            dash.addBoolean(highGear);

        }
        dash.finalizeCluster();

    }




}
