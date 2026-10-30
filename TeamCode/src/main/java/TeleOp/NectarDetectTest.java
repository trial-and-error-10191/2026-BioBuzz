package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Assemblies.BallDifferentiateColor;

@TeleOp (name = "NectarDetect", group = "Test")
public class NectarDetectTest extends LinearOpMode {
    @Override
    public void runOpMode () {
        BallDifferentiateColor ballDiff = new BallDifferentiateColor(hardwareMap, telemetry);
        ballDiff.locateNectAndOrPol(true, false, ballDiff.colorLocatorBlue);
        ballDiff.FindBalls();
        waitForStart();
        while (opModeIsActive()) {
            ballDiff.ballDist();
            ballDiff.listUpdate();
            ballDiff.ShowFindings();
        }
    }
}
