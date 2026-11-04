package Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Assemblies.Robot;

@Autonomous (name = "BallFind", group = "Robot")
public class BallFindAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.ballDiff.locateNectAndOrPol(false, false, robot.ballDiff.colorLocatorBlue);
        robot.ballDiff.FindBalls();
        waitForStart();
        while (opModeIsActive()) {
            robot.autoBase.closeBallTarget(robot, false, true);
        }
    }
}
