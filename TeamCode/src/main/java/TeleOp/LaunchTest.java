package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Assemblies.Robot;

@TeleOp (name = "LaunchTest", group = "Test")
public class LaunchTest extends LinearOpMode {
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            robot.driveTrain.drive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            robot.wheels.rpmReset(2950);
            robot.wheels.wheelsTick();
        }
    }
}
