/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team229.logomotion.utils;

/**
 *
 * @author PJ
 */
public class PortMap {

    public static final boolean USE_FILE = true;


    public static final int ARM_SOL_OUT;
    public static final int ARM_SOL_IN;

    //CAN - Motors
    public static final int LEFT_DRIVE_MOT_A;
    public static final int LEFT_DRIVE_MOT_B;
    public static final int RIGHT_DRIVE_MOT_A;
    public static final int RIGHT_DRIVE_MOT_B;
    public static final int ELEVATOR_MOTOR;
    public static final int TOP_ROLLER_MOT;
    public static final int BOTTOM_ROLLER_MOT;

    //Digital IO
    public static final int RIGHT_LINE_SENSOR;
    public static final int MIDDLE_LINE_SENSOR;
    public static final int LEFT_LINE_SENSOR;


    public static final int MINIBOT_DEPLOY_SOL_A;
    public static final int MINIBOT_DEPLOY_SOL_B;
    public static final int MINIBOT_EXTEND_SOL_A;
    public static final int MINIBOT_EXTENT_SOL_B;

    public static final int MINIBOT_LIMIT_SWITCH;

    public static final int ELEVATOR_ENC_A;
    public static final int ELEVATOR_ENC_B;
    public static final int ELEVATOR_LSW;
    public static final int TUBE_LSW;

    public static final int SHIFTER_SOL_A;
    public static final int SHIFTER_SOL_B;

    public static final int RED_LEDS;
    public static final int WHITE_LEDS;
    public static final int BLUE_LEDS;

    public static final int PRESSURE_SWITCH;
    public static final int COMPRESSOR_RELAY;

    public static final int TIER_SW;
    public static final int START_POS_SW;
    public static final int ROTARY_SW;

     static
    {
        if(USE_FILE)
        {
            LEFT_DRIVE_MOT_A     = FileConstantReader.readPort("LEFT_DRIVE_MOT_A");
            LEFT_DRIVE_MOT_B     = FileConstantReader.readPort("LEFT_DRIVE_MOT_B");
            RIGHT_DRIVE_MOT_A    = FileConstantReader.readPort("RIGHT_DRIVE_MOT_A");
            RIGHT_DRIVE_MOT_B    = FileConstantReader.readPort("RIGHT_DRIVE_MOT_B");

            ELEVATOR_MOTOR       = FileConstantReader.readPort("ELEVATOR_MOTOR");
            TOP_ROLLER_MOT       = FileConstantReader.readPort("TOP_ROLLER_MOT");
            BOTTOM_ROLLER_MOT    = FileConstantReader.readPort("BOTTOM_ROLLER_MOT");

            RIGHT_LINE_SENSOR    = FileConstantReader.readPort("RIGHT_LINE_SENSOR");
            MIDDLE_LINE_SENSOR   = FileConstantReader.readPort("MIDDLE_LINE_SENSOR");
            LEFT_LINE_SENSOR     = FileConstantReader.readPort("LEFT_LINE_SENSOR");


            MINIBOT_DEPLOY_SOL_A    = FileConstantReader.readPort("MINIBOT_DEPLOY_SOL_A");
            MINIBOT_DEPLOY_SOL_B    = FileConstantReader.readPort("MINIBOT_DEPLOY_SOL_B");
            MINIBOT_EXTEND_SOL_A    = FileConstantReader.readPort("MINIBOT_EXTEND_SOL_A");
            MINIBOT_EXTENT_SOL_B    = FileConstantReader.readPort("MINIBOT_EXTENT_SOL_B");

            MINIBOT_LIMIT_SWITCH    = FileConstantReader.readPort("MINIBOT_LIMIT_SWITCH");


            ELEVATOR_ENC_A          = FileConstantReader.readPort("ELEVATOR_ENC_A");
            ELEVATOR_ENC_B          = FileConstantReader.readPort("ELEVATOR_ENC_B");
            ELEVATOR_LSW            = FileConstantReader.readPort("ELEVATOR_LSW");
            TUBE_LSW                = FileConstantReader.readPort("TUBE_LSW");

            SHIFTER_SOL_A           = FileConstantReader.readPort("SHIFTER_SOL_A");
            SHIFTER_SOL_B           = FileConstantReader.readPort("SHIFTER_SOL_B");

            ARM_SOL_IN              = FileConstantReader.readPort("ARM_SOL_IN");
            ARM_SOL_OUT             = FileConstantReader.readPort("ARM_SOL_OUT");

            RED_LEDS                = FileConstantReader.readPort("RED_LEDS");
            WHITE_LEDS              = FileConstantReader.readPort("WHITE_LEDS");
            BLUE_LEDS               = FileConstantReader.readPort("BLUE_LEDS");

            PRESSURE_SWITCH         = FileConstantReader.readPort("PRESSURE_SWITCH");
            COMPRESSOR_RELAY        = FileConstantReader.readPort("COMPRESSOR_RELAY");

            TIER_SW                 = FileConstantReader.readPort("TIER_SW");
            START_POS_SW            = FileConstantReader.readPort("START_POS_SW");
            ROTARY_SW               = FileConstantReader.readPort("ROTARY_SW");
        }
    }
}
