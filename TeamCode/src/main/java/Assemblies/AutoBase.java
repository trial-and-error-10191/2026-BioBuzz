package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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
            robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchRed + 2, 0);
        }
        if (robot.ballDiff.ballDistInchBlue < robot.ballDiff.ballDistInchRed && robot.ballDiff.ballDistInchBlue < robot.ballDiff.ballDistInchPollen) {
            robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchBlue + 2, 0);
        }
        if (robot.ballDiff.ballDistInchPollen < robot.ballDiff.ballDistInchBlue && robot.ballDiff.ballDistInchPollen < robot.ballDiff.ballDistInchRed) {
            robot.driveTrain.driveStraight(1, robot.ballDiff.ballDistInchPollen + 2, 0);
        }
    }
}