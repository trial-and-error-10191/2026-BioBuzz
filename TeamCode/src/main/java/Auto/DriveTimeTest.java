package Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Assemblies.DriveTrainMecanum;

@Autonomous (name = "Drive Time Test", group = "Test")
public class DriveTimeTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DriveTrainMecanum driveTrain = new DriveTrainMecanum(hardwareMap, telemetry);
        waitForStart();
        driveTrain.drivePowerForward(0.5, 1); // Goes 45 inches at this speed
    }
}
