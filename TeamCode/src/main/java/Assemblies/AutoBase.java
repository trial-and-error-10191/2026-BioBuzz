package Assemblies;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;

import java.util.List;

public class AutoBase {
    Telemetry telemetry;
    public AutoBase(Telemetry telemetry) {
        this.telemetry = telemetry;
    }
    public void ballFindUpdate(BallDifferentiateColor ballDiff) {
        ballDiff.ballDist();
        ballDiff.listUpdate();
    }
    public void closeBallTarget(Robot robot, boolean nectarRed) {
        double desiredNectarDist;
        List<ColorBlobLocatorProcessor.Blob> desiredBlobs;
        if (nectarRed) {
            desiredNectarDist = robot.ballDiff.ballDistInchRed;
            desiredBlobs = robot.ballDiff.blobsRed;
        } else {
            desiredNectarDist = robot.ballDiff.ballDistInchBlue;
            desiredBlobs = robot.ballDiff.blobsBlue;
        }
        robot.driveTrain.encoderSettings();
        if (desiredNectarDist < robot.ballDiff.ballDistInchPollen) {
            telemetry.addData("in first if statement", "");
            for (ColorBlobLocatorProcessor.Blob b : desiredBlobs) {
                telemetry.addData("In for loop", "");
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
                    robot.driveTrain.driveIndividual(1, desiredNectarDist + 2,
                            desiredNectarDist + 2, desiredNectarDist + 2,
                            desiredNectarDist + 2, robot.driveTrain.getHeading());
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.turnToHeading(1, 10);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.turnToHeading(1, -10);
                }
            }
        }
//        if (robot.ballDiff.ballDistInchPollen < desiredNectarDist) {
//            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsYellow) {
//                Circle circleFit = b.getCircle();
//                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
//                    robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchPollen + 2, robot.driveTrain.getHeading());
//                }
//                if (circleFit.getX() > 200) {
//                    robot.driveTrain.turnToHeading(1, 10);
//                } else if (circleFit.getX() < 100) {
//                    robot.driveTrain.turnToHeading(1, -10);
//                }
//            }
//        }
    }
}