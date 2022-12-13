package org.firstinspires.ftc.teamcode.bells;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bells {
    public static CRServo bellsServo;
    public static DcMotor leftDrive;
    public static DcMotor rightDrive;
    public static DcMotor leftBackDrive;
    public static DcMotor rightBackDrive;


    public Bells(){};

    public static void initBells(HardwareMap hwm){
        //Declare Motors on hardware map
        bellsServo = hwm.get(CRServo.class, "bellsServo");
        leftDrive = hwm.get(DcMotor.class, "leftDrive");
        rightDrive = hwm.get(DcMotor.class, "rightDrive");
        leftBackDrive = hwm.get(DcMotor.class, "leftBackDrive");
        rightBackDrive = hwm.get(DcMotor.class, "rightBackDrive");

        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public static void On(){
        bellsServo.setPower(0.5);
    }

    public static void Off(){
        bellsServo.setPower(0);
    }

    public static double getPower(){return bellsServo.getPower();}

    //Updates the powers for the bells based on the states above

    //States for the intake (Forward, backwards, stop)
}
