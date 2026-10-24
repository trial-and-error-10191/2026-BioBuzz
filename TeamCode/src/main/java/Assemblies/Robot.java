package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class Robot {
    public AprilTagProcessor aprilTag;
    public AprilTagCentering aprilTagCenter;
    public CameraDefinition camDef;
    public DriveTrainMecanum driveTrain;
    public LaunchWheels wheels;
    public Robot(HardwareMap hwMap, Telemetry telemetry) {
        camDef = new CameraDefinition(hwMap);
        aprilTagCenter = new AprilTagCentering(camDef.aprilTag, telemetry);
        driveTrain = new DriveTrainMecanum(hwMap, telemetry);
        wheels = new LaunchWheels(hwMap, telemetry);
    }
}

