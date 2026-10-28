package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class Robot {
    public AprilTagCentering aprilTagCenter;
    public CameraDefinition camDef;
    public DriveTrainMecanum driveTrain;
    public LaunchWheels wheels;
    public Telemetry telemetry;
    public Robot(HardwareMap hwMap, Telemetry telemetry) {
        camDef = new CameraDefinition(hwMap);
        aprilTagCenter = new AprilTagCentering(camDef.aprilTag, telemetry);
        driveTrain = new DriveTrainMecanum(hwMap, telemetry);
        wheels = new LaunchWheels(hwMap, telemetry);
        this.telemetry = telemetry;
    }
    public void launchByAprilTags(char desiredChar) {
        if (aprilTagCenter.findClusterDist(camDef.aprilTag, desiredChar) > 60) {
            wheels.launchMotor.setPower(0.1);
        } else if (aprilTagCenter.findClusterDist(camDef.aprilTag, desiredChar) <= 60 && aprilTagCenter.findClusterDist(camDef.aprilTag, desiredChar) >= 20) {
            wheels.launchMotor.setPower(0.3);
        } else if (aprilTagCenter.findClusterDist(camDef.aprilTag, desiredChar) < 20) {
            wheels.launchMotor.setPower(0.5);
        }
        telemetry.addData("Cluster Distance", aprilTagCenter.findClusterDist(camDef.aprilTag, desiredChar));
        telemetry.addData("Launch Power", wheels.launchMotor.getPower());
        telemetry.update();
    }
}

