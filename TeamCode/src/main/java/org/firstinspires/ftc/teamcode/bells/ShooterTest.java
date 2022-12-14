package org.firstinspires.ftc.teamcode.bells;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "ShooterTest", group = "16634")
public class ShooterTest extends LinearOpMode {


    //Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotorEx shooterLeft = null;
    //private DcMotor shooterRight = null;
    private DcMotor intake = null;
    //    private
    private DcMotor belt = null;
    private Servo ringhold = null;
    private Servo flipper = null;
    //private Servo wobblegoal = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Init motors
        frontLeftDrive = hardwareMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive = hardwareMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive = hardwareMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive = hardwareMap.get(DcMotor.class, "backRightDrive");
        shooterLeft = hardwareMap.get(DcMotorEx.class, "shooterLeft");
        //shooterRight = hardwareMap.get(DcMotor.class, "shooterRight");
        intake = hardwareMap.get(DcMotor.class, "intake");
        belt = hardwareMap.get(DcMotor.class, "belt");
        ringhold = hardwareMap.get(Servo.class, "ringhold");
        flipper = hardwareMap.get(Servo.class, "flipper");
        //wobblegoal = hardwareMap.get(Servo.class, "wobblegoal");

        //We set the directions for each motor.
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        //shooterRight.setDirection(DcMotor.Direction.FORWARD);
        shooterLeft.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        shooterLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {


            //We Import all of the Methods created below in order to use it in our actual program
            driving();
            shooter();
            intakesys();
            servoslol();
            unRun();

            telemetry.update();
       /*double frontLeftDrive = gamepad1.left_stick_y;
       double backLeftDrive = gamepad1.left_stick_y;
       double frontRightDrive = gamepad1.right_stick_y;
       double backRightDrive = gamepad1.right_stick_y;*/

       /*if(gamepad1.a).
           servo.setPosition(0.5);
       }
       if(gamepad1.b){
           servo.setPosition(0.0);
       }*/

        }

    }

    public void driving() {
        //Driving method, allows robot drive around

        //Setting the controls for driving
        double turn = -gamepad1.left_stick_x;
        double drive = -gamepad1.left_stick_y;
        double strafeFrontRight = gamepad1.right_stick_x * 0.9;
        double strafeFrontLeft = gamepad1.right_stick_x;
        double strafeBackRight = gamepad1.right_stick_x;
        double strafeBackLeft = gamepad1.right_stick_x;
        double MAX_SPEED = 1.0;

        //Sets robot power based on a range of controls
        double frontLeftPower = Range.clip(drive - turn + strafeFrontLeft, -1, 1);
        double backLeftPower = Range.clip(drive - turn - strafeBackLeft, -1, 1);
        double frontRightPower = Range.clip(drive + turn - strafeFrontRight, -1, 1);
        double backRightPower = Range.clip(drive + turn + strafeBackRight, -1, 1);
        double brake = 1;

        //breaks robot so it slows down
        if (gamepad1.left_trigger == 1) {
            brake = 0.5;
        }


        //sets individual motor power, includes brakes
        backLeftDrive.setPower(backLeftPower * brake);
        backRightDrive.setPower(backRightPower * brake);
        frontLeftDrive.setPower(frontLeftPower * brake);
        frontRightDrive.setPower(frontRightPower * brake);
    }

    //shooting method, runs shooter
    public void shooter() {

        double slow = 1;
        if (gamepad2.a) {
            slow = 0.85;
        }

        PIDFCoefficients pidNew = new PIDFCoefficients(45, 0,  2, 0);
        shooterLeft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidNew);
        if (gamepad2.right_trigger >= .5) {
            shooterLeft.setPower(-0.95 * slow);
            //shooterRight.setPower(0.6);
        } else if (gamepad2.right_trigger < .5) {
            shooterLeft.setPower(0);
            //shooterRight.setPower(0);
        }

        telemetry.addData("Shooter Pos: ", shooterLeft.getCurrentPosition());
        telemetry.addData("Shooter:", shooterLeft.getPower());
        telemetry.addData("time", getRuntime());
        telemetry.addData("Shooter Speed", shooterLeft.getCurrentPosition() / getRuntime() / 35);
    }

    //runs intake system
    public void intakesys() {
        if (gamepad2.left_trigger >= 0.5) {
            intake.setPower(0.4);
            belt.setPower(0.6);
        } else if (gamepad2.left_trigger < 0.5) {
            intake.setPower(0);
            belt.setPower(0);
        }
        telemetry.addData("Belt Pos: ", belt.getCurrentPosition());
    }

    public void unRun() {
        if (gamepad2.dpad_down == true) {
            intake.setPower(-0.8);
            belt.setPower(-0.8);
        }

    }

    //controls all servo motors
    public void servoslol() {
        if (gamepad1.left_bumper) {
            flipper.setPosition(0);
        } else if (gamepad1.right_bumper) {
            flipper.setPosition(0.5);
        }

        if (gamepad2.left_bumper) {
            ringhold.setPosition(0.5);
        } else if (gamepad2.right_bumper) {
            ringhold.setPosition(0.95);
        }

        /*if (gamepad2.x) {
            wobblegoal.setPosition(0.3);
        } else if (gamepad2.y) {
            wobblegoal.setPosition(0.15);
        }*/
    }


    //Drives arm Motor
}
