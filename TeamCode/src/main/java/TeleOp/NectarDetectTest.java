package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Assemblies.NectarDifferentiateColor;

@TeleOp (name = "NectarDetect", group = "Test")
public class NectarDetectTest extends LinearOpMode {
    @Override
    public void runOpMode () {
        NectarDifferentiateColor nectarDiff = new NectarDifferentiateColor(hardwareMap, telemetry);
        nectarDiff.FindNectar();
        waitForStart();
        while (opModeIsActive()) {
            nectarDiff.ShowFindings();
        }
    }
}
