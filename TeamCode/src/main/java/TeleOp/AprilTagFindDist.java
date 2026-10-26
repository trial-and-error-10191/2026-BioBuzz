package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Assemblies.Robot;

@TeleOp (name = "AprilTagDist", group = "Test")
public class AprilTagFindDist extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();
        while (opModeIsActive()) {
            robot.aprilTagCenter.findClusterDist(robot.camDef.aprilTag);
        }
    }
}
