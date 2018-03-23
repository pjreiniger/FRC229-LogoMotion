/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.utils;

/**
 *
 * @author PJ
 */
public class OurPID {

    double lastError;
    double sumError;

    double setpoint;

    double kP, kI, kD;

    double tolerance;

    private static final double kDefaultTolerance = .5;

    public OurPID() {
        this(0,0,0,kDefaultTolerance);
    }
    public OurPID(double p, double i, double d)
    {
        this(p,i,d,kDefaultTolerance);
    }
    public OurPID(double p, double i, double d, double tol)
    {
        kP = p;
        kI = i;
        kD = d;
        tolerance = tol;

        lastError = 0;
        sumError = 0;
    }

    public void setSetpoint(double desSetpoint)
    {
        setpoint = desSetpoint;
    }

    public double calcControl(double error)
    {
	double derivError = error-lastError;

        double controlVal = kP*error + kD * derivError + kI * sumError;


	lastError = error;
	sumError += error;

        return controlVal;
    }

    


}
