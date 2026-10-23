package Assemblies;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


public class DriveTrainMecanum {
    //DcMotor limbExtend;

    DcMotor leftFrontDrive, rightFrontDrive, leftBackDrive, rightBackDrive;
    double angles = 0;
    private IMU imu = null;
    long start = System.nanoTime();

    Telemetry telemetry;

    // variables for encoders and IMU
    static final double COUNTS_PER_MOTOR_REV = 537.6;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 3.5;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double HEADING_THRESHOLD = 5.0;
    static final double P_TURN_GAIN = 0.02;     // Larger is more responsive, but also less stable.
    static final double P_DRIVE_GAIN = 0.03;
    private double targetHeading = 0;
    private double driveSpeed = 0;
    private double turnSpeed = 0;
    private double leftFrontSpeed = 0;
    private double leftBackSpeed = 0;
    private double rightFrontSpeed = 0;
    private double rightBackSpeed = 0;
    private int leftFrontTarget = 0;
    private int leftBackTarget = 0;
    private int rightFrontTarget = 0;
    private int rightBackTarget = 0;
    private double headingError = 0;
    private double extensionPowerReductionIntensity = 7560;
    private int CurrentLiftCounts = 0;
    // All subsystems should have a hardware function that labels all of the hardware required of it.
    public DriveTrainMecanum(HardwareMap hwMap, Telemetry telemetry) {

        // Initializes motor names:
        //limbExtend = hwMap.get(DcMotor.class, "limbExtend");
        leftFrontDrive = hwMap.get(DcMotor.class, "leftFront");
        leftBackDrive = hwMap.get(DcMotor.class, "leftBack");
        rightFrontDrive = hwMap.get(DcMotor.class, "rightFront");
        rightBackDrive = hwMap.get(DcMotor.class, "rightBack");

        // Initializes motor directions:
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.UP;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);
        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));
        this.telemetry = telemetry;
        imu.resetYaw();
    }

    public void encoderSettings() {
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void fieldOrientedAuto(double axial, double lateral, double yaw) {
        double sensitivity = 0.65;        // initializes sensitivity
        double max;                       // use for max power to wheels

        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        angles = -orientation.getYaw(AngleUnit.RADIANS);

        double robotForward = -axial * Math.cos(angles) + lateral * Math.sin(angles);
        double robotStrafe = lateral * Math.cos(angles) - -axial * Math.sin(angles);

        double leftFrontPower = 0;
        double rightFrontPower = 0;
        double leftBackPower = 0;
        double rightBackPower = 0;

        leftFrontPower = robotForward + robotStrafe + yaw;
        rightFrontPower = robotForward - robotStrafe - yaw;
        leftBackPower = robotForward - robotStrafe + yaw;
        rightBackPower = robotForward + robotStrafe - yaw;

        // All code below this comment normalizes the values so no wheel power exceeds 100%.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // The next four lines gives the calculated power to each motor.
        leftFrontDrive.setPower(leftFrontPower * sensitivity);
        rightFrontDrive.setPower(rightFrontPower * sensitivity);
        leftBackDrive.setPower(leftBackPower * sensitivity);
        rightBackDrive.setPower(rightBackPower * sensitivity);
    }

    public void drive(double axial, double lateral, double yaw) {

        // initializes deadzone
        double deadzone = 0.05;
        // initializes sensitivity
        double sensitivity = 0.75;

        double leftFrontPower = 0;
        double rightFrontPower = 0;
        double leftBackPower = 0;
        double rightBackPower = 0;

        if (Math.abs(axial) > deadzone || Math.abs(lateral) > deadzone || Math.abs(yaw) > deadzone) {
            leftFrontPower = axial + lateral + yaw;
            rightFrontPower = axial - lateral - yaw;
            leftBackPower = axial - lateral + yaw;
            rightBackPower = axial + lateral - yaw;
        }
        double max;

        // All code below this comment normalizes the values so no wheel power exceeds 100%.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));


        if (max > 1.0) {
            leftFrontPower /= max; // leftFrontPower = leftFrontPower / max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Calculates power using sensitivity variable.
        leftFrontPower *= sensitivity;
        leftBackPower *= sensitivity;
        rightFrontPower *= sensitivity;
        rightBackPower *= sensitivity;
        leftFrontPower *=  (1 - (CurrentLiftCounts / extensionPowerReductionIntensity));
        leftBackPower *=  (1 - (CurrentLiftCounts / extensionPowerReductionIntensity));
        rightFrontPower *=  (1 - (CurrentLiftCounts / extensionPowerReductionIntensity));
        rightBackPower *=  (1 - (CurrentLiftCounts / extensionPowerReductionIntensity));

        leftFrontPower *= 0.7; // this motor is 312 rpm, others are 223. 223/312 ~ 0.7

        // The next four lines gives the calculated power to each motor.
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }

    public void driveStraight(double maxDriveSpeed,
                              double distance,
                              double heading) {

        // Determine new target position, and pass to motor controller
        int moveCounts = (int) (distance * COUNTS_PER_INCH);
        leftFrontTarget = leftFrontDrive.getCurrentPosition() + moveCounts;
        leftBackTarget = leftBackDrive.getCurrentPosition() + moveCounts;
        rightFrontTarget = rightFrontDrive.getCurrentPosition() + moveCounts;
        rightBackTarget = rightBackDrive.getCurrentPosition() + moveCounts;

        // Set Target FIRST, then turn on RUN_TO_POSITION
        // If Strafing then reverse motor directions


        leftFrontDrive.setTargetPosition(leftFrontTarget);
        rightFrontDrive.setTargetPosition(rightFrontTarget);
        leftBackDrive.setTargetPosition(leftBackTarget);
        rightBackDrive.setTargetPosition(rightBackTarget);


        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the required driving speed  (must be positive for RUN_TO_POSITION)
        // Start driving straight, and then enter the control loop
        maxDriveSpeed = Math.abs(maxDriveSpeed);
        moveRobot(maxDriveSpeed, 0);

        // keep looping while we are still active, and BOTH motors are running.
        while ((leftFrontDrive.isBusy() && rightFrontDrive.isBusy() && rightBackDrive.isBusy() && leftBackDrive.isBusy())) {

            // Determine required steering to keep on heading
            turnSpeed = getSteeringCorrection(heading, P_DRIVE_GAIN);

            // if driving in reverse, the motor correction also needs to be reversed
            if (distance < 0)
                turnSpeed *= -1.0;

            // Apply the turning correction to the current driving speed.
            moveRobot(driveSpeed, turnSpeed);
        }
    }

    public void drivePowerForward(double driveSpeed, double time) {

        // Determine new target position, and pass to motor controller
        double leftFrontPower = -driveSpeed;
        double leftBackPower = -driveSpeed;
        double rightFrontPower = -driveSpeed;
        double rightBackPower = -driveSpeed;


        double max;

        // All code below this comment normalizes the values so no wheel power exceeds 100%.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));


        if (max > 1.0) {
            leftFrontPower /= max; // leftFrontPower = leftFrontPower / max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        start = System.nanoTime();
        // keep looping while we are still active, and BOTH motors are running.
        while (System.nanoTime() - start < time * 1E9) {
            leftFrontDrive.setPower(leftFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightFrontDrive.setPower(rightFrontPower);
            rightBackDrive.setPower(rightBackPower);
        }
    }

    public void drivePowerTurn(double driveSpeed, double time ) {

        // Determine new target position, and pass to motor controller
        double leftFrontPower = driveSpeed;
        double leftBackPower = driveSpeed;
        double rightFrontPower = -driveSpeed;
        double rightBackPower = -driveSpeed;


        double max;

        // All code below this comment normalizes the values so no wheel power exceeds 100%.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));


        if (max > 1.0) {
            leftFrontPower /= max; // leftFrontPower = leftFrontPower / max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        start = System.nanoTime();
        // keep looping while we are still active, and BOTH motors are running.
        while (System.nanoTime() - start < time * 1E9) {
            leftFrontDrive.setPower(leftFrontPower);
            leftBackDrive.setPower(leftBackPower);
            rightFrontDrive.setPower(rightFrontPower);
            rightBackDrive.setPower(rightBackPower);
        }
    }

    public void driveIndividual(double maxDriveSpeed,
                              double distanceLeftFront,
                              double distanceLeftBack,
                              double distanceRightFront,
                              double distanceRightBack,
                              double heading) {

        // Determine new target position, and pass to motor controller
        int moveCountsLeftFront  = (int) (distanceLeftFront * COUNTS_PER_INCH);
        int moveCountsLeftBack   = (int) (distanceLeftBack * COUNTS_PER_INCH);
        int moveCountsRightFront = (int) (distanceRightFront * COUNTS_PER_INCH);
        int moveCountsRightBack  = (int) (distanceRightBack * COUNTS_PER_INCH);
        leftFrontTarget = leftFrontDrive.getCurrentPosition() + moveCountsLeftFront;
        leftBackTarget = leftBackDrive.getCurrentPosition() + moveCountsLeftBack;
        rightFrontTarget = rightFrontDrive.getCurrentPosition() + moveCountsRightFront;
        rightBackTarget = rightBackDrive.getCurrentPosition() + moveCountsRightBack;

        // Set Target FIRST, then turn on RUN_TO_POSITION
        // If Strafing then reverse motor directions


        leftFrontDrive.setTargetPosition(leftFrontTarget);
        rightFrontDrive.setTargetPosition(rightFrontTarget);
        leftBackDrive.setTargetPosition(leftBackTarget);
        rightBackDrive.setTargetPosition(rightBackTarget);


        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the required driving speed  (must be positive for RUN_TO_POSITION)
        // Start driving straight, and then enter the control loop
        maxDriveSpeed = Math.abs(maxDriveSpeed);
        moveRobot(maxDriveSpeed, 0);

        // keep looping while we are still active, and BOTH motors are running.
        while ((leftFrontDrive.isBusy() && rightFrontDrive.isBusy() && rightBackDrive.isBusy() && leftBackDrive.isBusy())) {

            // Determine required steering to keep on heading
            turnSpeed = getSteeringCorrection(heading, P_DRIVE_GAIN);

            // if driving in reverse, the motor correction also needs to be reversed
            if (distanceLeftFront < 0 || distanceLeftBack < 0 || distanceRightFront < 0 || distanceRightBack < 0)
                turnSpeed *= -1.0;

            // Apply the turning correction to the current driving speed.
            moveRobot(driveSpeed, turnSpeed);
        }

    }

    public void moveRobot(double drive, double turn) {
        driveSpeed = drive;     // save this value as a class member so it can be used by telemetry.
        turnSpeed = turn;      // save this value as a class member so it can be used by telemetry.

        leftFrontSpeed = drive - turn;
        leftBackSpeed = drive - turn;
        rightFrontSpeed = drive + turn;
        rightBackSpeed = drive - turn;

        // Scale speeds down if either one exceeds +/- 1.0;
        double maxLeft = Math.max(Math.abs(leftFrontSpeed), Math.abs(leftBackSpeed));
        double maxRight = Math.max(Math.abs(rightFrontSpeed), Math.abs(rightBackSpeed));
        if (maxLeft > 1.0 || maxRight > 1.0) {
            leftFrontSpeed /= maxLeft;
            leftBackSpeed /= maxLeft;
            rightFrontSpeed /= maxLeft;
            rightBackSpeed /= maxLeft;
        }
        telemetry.addData("LeftFrontSpeed",leftFrontSpeed); telemetry.addData("LeftBackSpeed",leftBackSpeed); telemetry.addData("rightFrontSpeed",rightFrontSpeed); telemetry.addData("RightBackSpeed",rightBackSpeed);
        // Wheel power for testing
        leftFrontDrive.setPower(0.1);
        rightFrontDrive.setPower(0.1);
//        leftBackDrive.setPower(0.1);
//        rightBackDrive.setPower(0.1);

        // Actual wheel power that'll be used at competitions
//        leftFrontDrive.setPower(leftSpeed * 0.85);
//        rightFrontDrive.setPower(rightSpeed);
//        leftBackDrive.setPower(leftSpeed);
//        rightBackDrive.setPower(rightSpeed);
    }

    public double getSteeringCorrection(double desiredHeading, double proportionalGain) {
        targetHeading = desiredHeading;  // Save for telemetry

        // Determine the heading current error
        headingError = targetHeading - getHeading();

        // Normalize the error to be within +/- 180 degrees
        while (headingError > 180) headingError -= 360;
        while (headingError <= -180) headingError += 360;

        // Multiply the error by the gain to determine the required steering correction/  Limit the result to +/- 1.0
        return Range.clip(headingError * proportionalGain, -1, 1);
    }


    private void sendTelemetry(boolean straight) {

        if (straight) {
            telemetry.addData("Motion", "Drive Straight");
            telemetry.addData("Target Pos L:R", "%7d:%7d", leftFrontTarget, leftBackTarget, rightFrontTarget, rightBackTarget);
            telemetry.addData("Actual Pos L:R", "%7d:%7d:%7d:%7d", leftFrontDrive.getCurrentPosition(),
                    rightFrontDrive.getCurrentPosition(), leftBackDrive.getCurrentPosition(), rightBackDrive.getCurrentPosition());
        } else {
            telemetry.addData("Motion", "Turning");
        }

        telemetry.addData("Heading- Target : Current", "%5.2f : %5.0f", targetHeading, getHeading());
        telemetry.addData("Error  : Steer Pwr", "%5.1f : %5.1f", headingError, turnSpeed);
        telemetry.addData("Wheel Speeds LF : LB : RF : RB", "%5.2f : %5.2f : %5.2f : %5.2f", leftFrontSpeed, leftBackSpeed, rightFrontSpeed, rightBackSpeed);
        telemetry.update();
    }

    public void turnToHeading(double maxTurnSpeed, double heading) {

        // Run getSteeringCorrection() once to pre-calculate the current error
        getSteeringCorrection(heading, P_DRIVE_GAIN);

        // keep looping while we are still active, and not on heading.
        while ((Math.abs(headingError) > HEADING_THRESHOLD)) {

            // Determine required steering to keep on heading
            turnSpeed = getSteeringCorrection(heading, P_TURN_GAIN);

            // Clip the speed to the maximum permitted value.
            turnSpeed = Range.clip(turnSpeed, -maxTurnSpeed, maxTurnSpeed);

            // Pivot in place by applying the turning correction
            moveRobot(0, turnSpeed);

            // Display drive status for the driver.
            telemetry.addData("HeadingErr", headingError);
            telemetry.addData("HeadingThresh",HEADING_THRESHOLD);
            telemetry.addData("TurnSpeed",turnSpeed);
            telemetry.update();
        }

        // Stop all motion;
        moveRobot(0, 0);
    }

    public double getHeading() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }
}