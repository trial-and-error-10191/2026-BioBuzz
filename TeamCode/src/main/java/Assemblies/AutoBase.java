package Assemblies;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

public class AutoBase {
    double desiredNectarDist;
    List<ColorBlobLocatorProcessor.Blob> desiredBlobs;
    Telemetry telemetry;
    long start = System.nanoTime();
    public AutoBase(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    public void ballFindUpdate(BallDifferentiateColor ballDiff) {
        ballDiff.ballDist();
        ballDiff.listUpdate();
    }
    public void forwardBallCheck(Robot robot, double driveSpeed, double time, boolean nectarRedFind, boolean pollenOnly) {
        robot.driveTrain.smokeyBotSettings();
        robot.driveTrain.powerDirection(true, driveSpeed);
        robot.driveTrain.maxSet();

        start = System.nanoTime();
        // keep looping while we are still active, and BOTH motors are running.
        while (System.nanoTime() - start < time * 1E9) {
            robot.driveTrain.setPower();
            robot.ballDiff.ballCount(nectarRedFind, pollenOnly);
            telemetry.addData("Pollen Dist", robot.ballDiff.ballDistInchPollen);
            telemetry.update();
        }
    }

    public void turnBallCheck(Robot robot, double driveSpeed, double time, boolean nectarRedFind, boolean pollenOnly) {
        robot.driveTrain.smokeyBotSettings();
        robot.driveTrain.powerDirection(false, driveSpeed);
        robot.driveTrain.maxSet();

        start = System.nanoTime();
        // keep looping while we are still active, and BOTH motors are running.
        while (System.nanoTime() - start < time * 1E9) {
            robot.driveTrain.setPower();
            robot.ballDiff.ballCount(nectarRedFind, pollenOnly);
        }
    }
    public void closeBallTarget(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        while (robot.ballDiff.ballAmount < 4) {
            nectFindSettings(robot, nectarRedFind, pollenOnly);
            ballFindUpdate(robot.ballDiff);
            ballLocatePollen(robot, nectarRedFind, pollenOnly);
            pollenDriveTo(robot, nectarRedFind, pollenOnly);
            nectarDriveTo(robot);
        }
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
    public double timeSetToDistance(BallDifferentiateColor ballDiff) {
        ballDiff.ballDist();
        double timeDrive = 0.0222 * ballDiff.ballDistInchPollen;
        return timeDrive;
    }
    public void pollenDriveTo(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        if (pollenOnly || (robot.ballDiff.ballDistInchPollen < desiredNectarDist || (desiredBlobs.isEmpty() && robot.ballDiff.ballDistInchPollen > 0))) {
            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsYellow) {
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100 && robot.ballDiff.ballDistInchPollen != 0) {
                    forwardBallCheck(robot, 0.5, timeSetToDistance(robot.ballDiff), nectarRedFind, pollenOnly);
                    forwardBallCheck(robot, 0, 0.3, nectarRedFind, pollenOnly);
                }
                if (circleFit.getX() > 200) {
                    turnBallCheck(robot, -0.3, 0.1, nectarRedFind, pollenOnly);
                    turnBallCheck(robot, 0, 0.1, nectarRedFind, pollenOnly);
                } else if (circleFit.getX() < 100) {
                    turnBallCheck(robot,0.3, 0.1, nectarRedFind, pollenOnly);
                    turnBallCheck(robot, 0, 0.1, nectarRedFind, pollenOnly);
                }
            }
        }
        telemetry.addData("Balls intaken", robot.ballDiff.ballAmount);
        telemetry.update();
    }
    public void nectarDriveTo(Robot robot) {
        if (desiredNectarDist < robot.ballDiff.ballDistInchPollen || (robot.ballDiff.blobsYellow.isEmpty() && desiredNectarDist > 0)) {
            telemetry.addData("in first if statement", "");
            for (ColorBlobLocatorProcessor.Blob b : desiredBlobs) {
                telemetry.addData("In for loop", "");
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100 && desiredNectarDist != 0) {
                    robot.driveTrain.drivePowerForward(0.5, timeSetToDistance(robot.ballDiff));
                    robot.driveTrain.drivePowerForward(0, 0.3);
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
    public void ballLocatePollen(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        ballFindUpdate(robot.ballDiff);
        while (robot.ballDiff.ballDistInchPollen == 0) {
            robot.driveTrain.drivePowerTurn(-0.3, 0.1);
            robot.driveTrain.drivePowerTurn(0, 0.1);
            ballFindUpdate(robot.ballDiff);
        } if (robot.ballDiff.ballDistInchPollen != 0) {
            pollenDriveTo(robot, nectarRedFind, pollenOnly);
        }
    }
    public void ballFindStop(Robot robot, boolean nectarRedFind, boolean pollenOnly) {
        if (robot.ballDiff.ballAmount >= 4) {
            robot.driveTrain.drivePowerTurn(1, 30);
            robot.ballDiff.ballAmount = 0;
        }
        telemetry.addData("Balls intaken", robot.ballDiff.ballAmount);
        telemetry.update();
    }
}