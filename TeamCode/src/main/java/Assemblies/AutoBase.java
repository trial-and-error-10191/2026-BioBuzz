package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

public class AutoBase {
    Telemetry telemetry;
    public AutoBase(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    public void ballFindUpdate(BallDifferentiateColor ballDiff) {
        ballDiff.ballDist();
        ballDiff.listUpdate();
    }
    public void closeBallTarget(Robot robot) {
        if (robot.ballDiff.ballDistInchRed < robot.ballDiff.ballDistInchBlue && robot.ballDiff.ballDistInchRed < robot.ballDiff.ballDistInchPollen) {
            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsRed) {
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
                    robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchRed + 2, robot.driveTrain.getHeading());
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.turnToHeading(1, 10);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.turnToHeading(1, -10);
                }
            }
        }
        if (robot.ballDiff.ballDistInchBlue < robot.ballDiff.ballDistInchRed && robot.ballDiff.ballDistInchBlue < robot.ballDiff.ballDistInchPollen) {
            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsBlue) {
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
                    robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchBlue + 2, robot.driveTrain.getHeading());
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.turnToHeading(1, 10);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.turnToHeading(1, -10);
                }
            }
        }
        if (robot.ballDiff.ballDistInchPollen < robot.ballDiff.ballDistInchBlue && robot.ballDiff.ballDistInchPollen < robot.ballDiff.ballDistInchRed) {
            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsYellow) {
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
                    robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchPollen + 2, robot.driveTrain.getHeading());
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.turnToHeading(1, 10);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.turnToHeading(1, -10);
                }
            }
        }
    }
}