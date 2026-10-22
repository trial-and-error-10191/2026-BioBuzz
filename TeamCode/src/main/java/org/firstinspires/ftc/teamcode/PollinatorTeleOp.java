package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Pollinator Launch Tuning" , group="Linear OpMode")
public class PollinatorTeleOp extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null; // 2
    private DcMotor rightDrive = null; // 3
    private DcMotor launchMotor = null; // 0
    private DcMotor intakeMotor = null; // 1
    private Servo pollenIntake = null; // slot 0
    private CRServo launchHelper = null; // slot 1

    private final double DEADZONE = 0.05;

    private boolean intaking = false;
    private boolean launching = false;

    private double intakePower = -1.0;
    private double launchMotorPower = 0.7;
    private double launchHelperPower = 0.6;


    @Override
    public void runOpMode() {
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive"); // 2
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive"); // 3
        launchMotor = hardwareMap.get(DcMotor.class, "launch_motor"); // 0
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor"); // 1
        pollenIntake = hardwareMap.get(Servo.class, "pollen_servo"); // 0
        launchHelper = hardwareMap.get(CRServo.class, "launch_helper"); // 1

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        launchMotor.setDirection(DcMotor.Direction.FORWARD);
        intakeMotor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Hold L2 to adjust intake power
            // Hold R2 to adjust launch motor power
            // Hold R1 to adjust launch helper power
            if (gamepad1.left_trigger_pressed) {
                telemetry.addData("", "Adjusting power of intake motors.");
                telemetry.addData("", "");
                telemetry.addData("", "======================================");
                telemetry.addData("", "");
                if (gamepad1.dpadUpWasPressed()) {
                    intakePower += 0.05;
                } else if (gamepad1.dpadDownWasPressed()) {
                    intakePower -= 0.05;
                }
                intakePower = Range.clip(intakePower, -1.0, 1.0);
                telemetry.addData("Current Intake Motor Power", intakePower);
            } else if (gamepad1.right_trigger_pressed) {
                telemetry.addData("", "Adjusting power of launch motors");
                telemetry.addData("", "");
                telemetry.addData("", "======================================");
                telemetry.addData("", "");
                if (gamepad1.dpadUpWasPressed()) {
                    launchMotorPower += 0.05;
                } else if (gamepad1.dpadDownWasPressed()) {
                    launchMotorPower -= 0.05;
                }
                launchMotorPower = Range.clip(launchMotorPower, -1.0, 1.0);
                telemetry.addData("Current Launch Motor Power", launchMotorPower);
            } else if (gamepad1.right_bumper) {
                telemetry.addData("", "Adjusting power of launch helper");
                telemetry.addData("", "");
                telemetry.addData("", "======================================");
                telemetry.addData("", "");
                if (gamepad1.dpadUpWasPressed()) {
                    launchHelperPower += 0.05;
                } else if (gamepad1.dpadDownWasPressed()) {
                    launchHelperPower -= 0.05;
                }
                launchHelperPower = Range.clip(launchHelperPower, -1.0, 1.0);
                telemetry.addData("Current Launch Helper Power", launchHelperPower);
            } else {
                telemetry.addData("", "Adjust motor powers by holding the respective buttons");
                telemetry.addData("", "");
                telemetry.addData("Intake Motors", "Left Trigger (L2)");
                telemetry.addData("", "");
                telemetry.addData("Launch Motors", "Right Trigger (R2)");
                telemetry.addData("", "");
                telemetry.addData("Launch Helper", "Right Bumper (R1)");
            }

            if (Math.abs(gamepad1.left_stick_y) > DEADZONE) {
                leftDrive.setPower(gamepad1.left_stick_y);
            } else {
                leftDrive.setPower(0.0);
            }
            if (Math.abs(gamepad1.right_stick_y) > DEADZONE) {
                rightDrive.setPower(gamepad1.right_stick_y);
            } else {
                rightDrive.setPower(0.0);
            }

            if (gamepad1.aWasPressed()) {
                intaking = !intaking;
            }
            if (intaking) {
                intakeMotor.setPower(intakePower);
            } else {
                intakeMotor.setPower(0.0);
            }

            if (gamepad1.yWasPressed()) {
                launching = !launching;
            }
            if (launching) {
                launchMotor.setPower(launchMotorPower);
                launchHelper.setPower(launchHelperPower);
            } else {
                launchMotor.setPower(0.0);
                launchHelper.setPower(0.0);
            }

            telemetry.update();
        }
    }
}