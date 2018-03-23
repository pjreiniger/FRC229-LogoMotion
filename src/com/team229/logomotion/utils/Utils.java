/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.utils;

/**
 *
 * @author PJ
 */
public class Utils {
    public static double MotorValLimit(double in)
    {
            //return (in>1.0?1.0:(in<-1.0?-1.0:in));
            return LimitVal(in, -1.0, 1.0);
    }

    public static double LimitVal(double in, double limit)
    {
            return LimitVal(in, -limit, limit);
    }
    public static double LimitVal(double in, double min, double max)
    {
            double output = in;

            if(in > max)
            {
                    output = max;
            }
            if(in < min)
            {
                    output = min;
            }

            return output;
    }


    public static double DegreeToRadian(double degrees)
    {
            return degrees*Math.PI/180.0;
    }


    public static double RadianToDegree(double rads)
    {
            return rads*180.0/Math.PI;
    }



    /**
     * Returns if the current value is withing the deadband of the desired value
     * i.e. -deadband < cur < deadband
     */
    public static boolean inInclusiveRange(double cur, double des, double deadband)
    {
            if( ((cur+deadband) > des) && ((cur-deadband)<des) )
                    return true;
            return false;
    }






}
