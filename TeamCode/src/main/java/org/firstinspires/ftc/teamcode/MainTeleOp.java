package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Arm;
import org.firstinspires.ftc.teamcode.subsystems.DriveTrain;

@TeleOp(name="TeleOop", group="Linear Opmode")

public class MainTeleOp extends LinearOpMode{
    public void runOpMode() throws InterruptedException {
        Arm.initArm(hardwareMap);
        DriveTrain.initDriveTrain(hardwareMap);

        double power = .1;
        waitForStart();

        while(opModeIsActive()){
            //DriveTrain.cartesianDrive(-gamepad1.left_stick_x * .5, gamepad1.left_stick_y * .5, gamepad1.right_stick_x * .5, 0);

            DriveTrain.mecanumDrive(-gamepad1.left_stick_y * 0.35, gamepad1.left_stick_x * 0.35, gamepad1.right_stick_x * 0.35);

            if(gamepad1.a)
                Arm.claw.setPosition(.06);
            if(gamepad1.y)
                Arm.claw.setPosition(.3);

            /*if(!gamepad1.x){
                DriveTrain.leftFront.setPower(power);
                DriveTrain.leftBack.setPower(power);
                DriveTrain.rightFront.setPower(power);
                DriveTrain.rightBack.setPower(power);
            }


            if(gamepad1.a)
                power+=.01;
            if(gamepad1.y)
                power-=.01;*/

            telemetry.addData("Left Wheel: ", DriveTrain.getLeftPosition());
            telemetry.addData("Right Wheel: ", DriveTrain.getRightPosition());
            telemetry.addData("Middle Wheel: ", DriveTrain.getMiddlePosition());
            telemetry.addData("Power: ", power);
            telemetry.update();

        }
    }
}
