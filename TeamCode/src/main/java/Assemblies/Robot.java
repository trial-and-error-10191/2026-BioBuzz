package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public DriveTrainMecanum driveTrain;
    public LaunchWheels launchWheels;
    public Robot(HardwareMap hwMap, Telemetry telemetry) {
        driveTrain = new DriveTrainMecanum(hwMap, telemetry);
        launchWheels = new LaunchWheels(hwMap, telemetry);
    }
}

