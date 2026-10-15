package Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Assemblies.Robot;

@Autonomous (name = "BallFind", group = "Robot")
public class BallFindAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.ballDiff.FindBalls();
        waitForStart();
        robot.autoBase.ballFindUpdate(robot.ballDiff);
        robot.autoBase.closeBallTarget(robot);
    }
}
