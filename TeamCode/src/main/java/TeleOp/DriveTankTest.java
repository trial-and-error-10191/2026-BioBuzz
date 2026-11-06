package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Assemblies.DriveTrainMecanum;

@TeleOp (name = "TankTest", group = "Test")
public class DriveTankTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            driveTrain.driveTank(gamepad1.left_stick_y, -gamepad1.right_stick_x);
        }
    }
}
