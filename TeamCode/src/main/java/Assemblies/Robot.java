package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public DriveTrainMecanum driveTrain;
    public LaunchWheels wheels;
    public Robot(HardwareMap hwMap, Telemetry telemetry) {
        driveTrain = new DriveTrainMecanum(hwMap, telemetry);
        wheels = new LaunchWheels(hwMap, telemetry);
    }
}

