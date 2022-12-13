package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.bells.Bells;

@TeleOp(name="LinearSlideTest", group="Linear Opmode")
public class LinearSlideTes extends LinearOpMode{

    public static DcMotor leftMotor;
    public static DcMotor rightMotor;

    public void runOpMode() throws InterruptedException {

        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "leftMotor");

        waitForStart();

        while(opModeIsActive()){

            leftMotor.setPower(gamepad1.left_stick_y);
            rightMotor.setPower(gamepad1.left_stick_y);




            telemetry.addData("Power: ", leftMotor.getPower());
            telemetry.addData("Power2:", rightMotor.getPower());
            telemetry.update();
        }
    }
}
