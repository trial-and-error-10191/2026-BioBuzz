package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Pollinator Launch Tuning" , group="Linear OpMode")
//@Disabled
public class BasicOmniOpMode_Linear extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null; // 2
    private DcMotor rightDrive = null; // 3
    private DcMotor launchMotor = null; // 0
    private DcMotor intakeMotor = null; // 1
    private Servo pollenIntake = null; // slot 0
    private CRServo launchHelper = null; // slot 1


    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive"); // 2
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive"); // 3
        launchMotor = hardwareMap.get(DcMotor.class, "launch_motor"); // 0
        intakeMotor = hardwareMap.get(DcMotor.class, "intake_motor"); // 1
        pollenIntake = hardwareMap.get(Servo.class, "pollen_servo"); // 0
        launchHelper = hardwareMap.get(CRServo.class, "launch_helper"); // 1

        // ########################################################################################
        // !!!            IMPORTANT Drive Information. Test your motor directions.            !!!!!
        // ########################################################################################
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot (the wheels turn the same direction as the motor shaft)
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction.  So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
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
            intakeMotor.setPower(-1.0);
            launchMotor.setPower(0.7);
            launchHelper.setPower(0.6);
        }
    }
}
