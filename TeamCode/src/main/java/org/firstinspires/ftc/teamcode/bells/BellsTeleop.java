package org.firstinspires.ftc.teamcode.bells;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="BellsTeleop", group="Linear Opmode")
public class BellsTeleop extends LinearOpMode{

    public void runOpMode() throws InterruptedException {

        Bells.initBells(hardwareMap);

        waitForStart();

        while(opModeIsActive()){

            Bells.leftDrive.setPower(gamepad1.left_stick_y);
            Bells.leftBackDrive.setPower(gamepad1.left_stick_y);
            Bells.rightDrive.setPower(gamepad1.right_stick_y);
            Bells.rightBackDrive.setPower(gamepad1.right_stick_y);

            if(gamepad1.a) {
                Bells.On();
            }

            if(gamepad1.b){
                Bells.Off();
            }



            telemetry.addData("Actual Power: ", Bells.getPower());
            telemetry.update();
        }
    }
}
