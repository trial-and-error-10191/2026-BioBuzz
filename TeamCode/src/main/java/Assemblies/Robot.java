package Assemblies;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Robot {
    public AutoBase autoBase;
    public BallDifferentiateColor ballDiff;
    public DriveTrainMecanum driveTrain;
    public Robot(HardwareMap hwMap, Telemetry telemetry) {
        autoBase = new AutoBase(telemetry);
        ballDiff = new BallDifferentiateColor(hwMap, telemetry);
        driveTrain = new DriveTrainMecanum(hwMap, telemetry);
    }
}
