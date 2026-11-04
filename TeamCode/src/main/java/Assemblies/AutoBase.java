package Assemblies;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

public class AutoBase {
    double desiredNectarDist;
    List<ColorBlobLocatorProcessor.Blob> desiredBlobs;
    Telemetry telemetry;
    public AutoBase(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    public void ballFindUpdate(BallDifferentiateColor ballDiff) {
        ballDiff.ballDist();
        ballDiff.listUpdate();
    }
    public void closeBallTarget(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        nectFindSettings(robot, nectarRedFind, pollenOnly);
        ballFindUpdate(robot.ballDiff);
        ballLocatePollen(robot);
        pollenDriveTo(robot, pollenOnly);
        nectarDriveTo(robot);
        ballFindStop(robot, nectarRedFind, pollenOnly);
    }
    public void nectFindSettings(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        if (nectarRedFind && !pollenOnly) {
            desiredNectarDist = robot.ballDiff.ballDistInchRed;
            desiredBlobs = robot.ballDiff.blobsRed;
        } else if (!nectarRedFind && !pollenOnly) {
            desiredNectarDist = robot.ballDiff.ballDistInchBlue;
            desiredBlobs = robot.ballDiff.blobsBlue;
        } else {
            desiredNectarDist = 0;
            desiredBlobs = robot.ballDiff.blobsYellow;
        }
    }
    public void pollenDriveTo(Robot robot, boolean pollenOnly) {
        if (pollenOnly || (robot.ballDiff.ballDistInchPollen < desiredNectarDist || (desiredBlobs.isEmpty() && robot.ballDiff.ballDistInchPollen > 0))) {
            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsYellow) {
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100 && robot.ballDiff.ballDistInchPollen != 0) {
                    robot.driveTrain.drivePowerForward(0.7, 2);
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.drivePowerTurn(0.3, 0.1);
                    robot.driveTrain.drivePowerTurn(0, 0.1);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.drivePowerTurn(-0.3, 0.1);
                    robot.driveTrain.drivePowerTurn(0, 0.1);
                }
            }
        }
    }
    public void nectarDriveTo(Robot robot) {
        if (desiredNectarDist < robot.ballDiff.ballDistInchPollen || (robot.ballDiff.blobsYellow.isEmpty() && desiredNectarDist > 0)) {
            telemetry.addData("in first if statement", "");
            for (ColorBlobLocatorProcessor.Blob b : desiredBlobs) {
                telemetry.addData("In for loop", "");
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100 && desiredNectarDist != 0) {
                    robot.driveTrain.drivePowerForward(0.3, 3);
                    robot.driveTrain.drivePowerForward(0, 0.5);
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.drivePowerTurn(0.3, 0.1);
                    robot.driveTrain.drivePowerTurn(0, 0.1);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.drivePowerTurn(-0.3, 0.1);
                    robot.driveTrain.drivePowerTurn(0, 0.1);
                }
            }
        }
    }
    public void ballLocatePollen(Robot robot) {
        ballFindUpdate(robot.ballDiff);
        while (robot.ballDiff.ballDistInchPollen == 0) {
            robot.driveTrain.drivePowerTurn(-0.3, 0.1);
            robot.driveTrain.drivePowerTurn(0, 0.1);
            ballFindUpdate(robot.ballDiff);
        }
    }
    public void ballFindStop(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        robot.ballDiff.ballCount(nectarRedFind, pollenOnly);
        if (robot.ballDiff.ballAmount >= 4) {
            robot.ballDiff.ballAmount = 0;
            robot.driveTrain.drivePowerTurn(1, 30);
        }
        telemetry.addData("Balls intaken", robot.ballDiff.ballAmount);
        telemetry.update();
    }
}