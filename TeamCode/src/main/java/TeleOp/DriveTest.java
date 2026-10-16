package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Assemblies.DriveTrainMecanum;

@TeleOp (name = "Drive Test", group = "Test")
public class DriveTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            driveTrain.drive(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
        }
    }
}
