package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.odometry.OdometryGlobalCoordinatePosition;

import java.util.Locale;

public class DriveTrain {
    //Declare Motors
    public static DcMotor leftFront;
    public static DcMotor leftBack;
    public static DcMotor rightFront;
    public static DcMotor rightBack;

    //Dead Wheels
    public static DcMotor leftWheel;
    public static DcMotor rightWheel;
    public static DcMotor middleWheel;

    public static BNO055IMU imu;
    public static Orientation angles;
    public static Acceleration gravity;

    //Constructor
    public DriveTrain(){}

    //Initialize
    public static void initDriveTrain(HardwareMap hwm){
        //Declare Motors on hardware map
        leftFront = hwm.get(DcMotor.class, "leftFront");
        leftBack = hwm.get(DcMotor.class, "leftBack");
        rightFront = hwm.get(DcMotor.class, "rightFront");
        rightBack = hwm.get(DcMotor.class, "rightBack");

        leftWheel = hwm.get(DcMotor.class, "leftFront");
        rightWheel = hwm.get(DcMotor.class, "leftBack");
        middleWheel = hwm.get(DcMotor.class, "rightFront");

        leftWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        middleWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        middleWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu = hwm.get(BNO055IMU.class, "imu");

        //Reverse Motors
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

        //leftWheel.setDirection(DcMotorSimple.Direction.REVERSE);
        //rightWheel.setDirection(DcMotorSimple.Direction.FORWARD);
        //middleWheel.setDirection(DcMotorSimple.Direction.FORWARD);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        BNO055IMU.Parameters parameters1 = new BNO055IMU.Parameters();
        parameters1.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters1.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters1.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters1.loggingEnabled = true;
        parameters1.loggingTag = "IMU";
        parameters1.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters1);

        angles = DriveTrain.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }



    public static void mecanumDrive(double y, double x, double z){
        leftFront.setPower(y + x + z); //+ + +
        leftBack.setPower(y - x + z); //+ - +
        rightFront.setPower(y - x - z); //+ - -
        rightBack.setPower(y + x - z); // + + -
    }

    public static void odometryDrive(double x, double y, double z, double adjust){
        double speed = Math.sqrt(2) * Math.hypot(x, y);
        double command = Math.atan2(y, -x) + Math.PI/2;
        double rotation = z;

        if(z > 0) {
            if (z < 0.01) {
                rotation = 0;
            } else if (z > 0.9) {
                rotation = z * z;
            } else {
                rotation = (z * z) + 0.013;
            }
        }
        if(z < 0) {
            if (z > -0.01) {
                rotation = 0;
            } else if (z < -0.9) {
                rotation = -(z * z);
            }
            else{
                rotation = (-(z*z)) - 0.013;
            }
        }

        angles = DriveTrain.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
        double adjustedXHeading = Math.cos(command + (angles.firstAngle + adjust) + Math.PI/4);
        double adjustedYHeading = Math.sin(command + (angles.firstAngle + adjust) + Math.PI/4);

        leftFront.setPower((speed * adjustedYHeading + rotation) * Constants.TELEOP_LIMITER);
        rightFront.setPower((speed * adjustedXHeading - rotation) * Constants.TELEOP_LIMITER);
        leftBack.setPower((speed * adjustedXHeading + rotation) * Constants.TELEOP_LIMITER);
        rightBack.setPower((speed * adjustedYHeading - rotation) * Constants.TELEOP_LIMITER);

    }

    public static int getLeftPosition(){
        return -leftWheel.getCurrentPosition();
    }

    public static int getRightPosition(){
        return rightWheel.getCurrentPosition();
    }

    public static int getMiddlePosition(){
        return middleWheel.getCurrentPosition();
    }

    public static void setRunMode(String input) {
        if (input.equals("STOP_AND_RESET_ENCODER")) {
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (input.equals("RUN_WITHOUT_ENCODER")) {
            leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (input.equals("RUN_USING_ENCODER")) {
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if (input.equals("RUN_TO_POSITION")) {
            leftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public static void stop(){
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);

    }

    public static void autoBrake(int timer){
        double currentPower = leftFront.getPower();
        double power = -1;

        while(timer > 0) {
            leftFront.setPower(power);
            leftBack.setPower(power);
            rightFront.setPower(power);
            rightBack.setPower(power);
            timer--;
        }
        leftFront.setPower(0);
        leftBack.setPower(0);
        rightFront.setPower(0);
        rightBack.setPower(0);
    }

    public static void composeTelemetry (Telemetry telemetry) {

        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
                gravity = imu.getGravity();
            }
        });
        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });
        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override
                    public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override
                    public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel * gravity.xAccel
                                        + gravity.yAccel * gravity.yAccel
                                        + gravity.zAccel * gravity.zAccel));
                    }
                });
    }

    /*public static void resetEncoder(){
        deadWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        deadWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }*/

    public static void gyroTele(Telemetry telemetry){
        telemetry.addData("Heading: ", formatAngle(angles.angleUnit, angles.firstAngle));
    }
    static String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.RADIANS.fromUnit(angleUnit, angle));
    }
    static String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.RADIANS.normalize(degrees));
    }
    public static void resetGyro(){
        BNO055IMU.Parameters parameters1 = new BNO055IMU.Parameters();
        parameters1.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters1.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters1.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters1.loggingEnabled = true;
        parameters1.loggingTag = "IMU";
        parameters1.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters1);

        angles = DriveTrain.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
    }

}
