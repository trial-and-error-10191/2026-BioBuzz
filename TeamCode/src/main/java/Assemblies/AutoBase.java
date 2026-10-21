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
        if (desiredNectarDist < robot.ballDiff.ballDistInchPollen || (robot.ballDiff.blobsYellow.isEmpty() && desiredNectarDist > 0)) {
            telemetry.addData("in first if statement", "");
            for (ColorBlobLocatorProcessor.Blob b : desiredBlobs) {
                telemetry.addData("In for loop", "");
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
                    robot.driveTrain.drivePowerForward(0.7, 3);
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.drivePowerTurn(0.7, 0.5);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.drivePowerTurn(-0.7, 0.5);
                }
            }
        }
        if (robot.ballDiff.ballDistInchPollen < desiredNectarDist || (desiredBlobs.isEmpty() && robot.ballDiff.ballDistInchPollen > 0)) {
            for (ColorBlobLocatorProcessor.Blob b : robot.ballDiff.blobsYellow) {
                Circle circleFit = b.getCircle();
                if (circleFit.getX() <= 200 && circleFit.getX() >= 100) {
                    robot.driveTrain.drivePowerForward(0.7, 5);
                }
                if (circleFit.getX() > 200) {
                    robot.driveTrain.drivePowerTurn(0.7, 2);
                } else if (circleFit.getX() < 100) {
                    robot.driveTrain.drivePowerTurn(-0.7, 2);
                }
            }
        }
    }
}