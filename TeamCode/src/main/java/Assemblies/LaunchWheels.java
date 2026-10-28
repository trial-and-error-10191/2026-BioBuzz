package Assemblies;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
public class LaunchWheels {

    // Define wheels
    public DcMotor launchMotor;

    // Define telemetry
    Telemetry telemetry;

    // Define restrictive variables
    private float changeBy = 0.0003f;
    public double rpmTarget = 0;
    private int rpmLeniency = 60;
    public double currentRPM = 1.0;
    private double lastKnownMS = 0;
    private double lastKnownEnc = 0;
    private boolean cataclysmicError = false;

    // Start motors with minimum movement power (0.13 minimum required power)
    private double power = 0.3;


    // Define fixed values
    private double encodersPerRevolution = 28;

    // Define the Elapsed time for checking RPM
    public ElapsedTime runTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    public LaunchWheels(HardwareMap hwMap, Telemetry telemetry) {
        // Valuate telemetry and wheels
        this.telemetry = telemetry;
        launchMotor = hwMap.get(DcMotor.class, "LaunchWheel");

        // Reverse the wheels to obtain the desired direction
        launchMotor.setDirection(DcMotor.Direction.FORWARD);

        cataclysmicError = (Math.abs(currentRPM) / rpmTarget * 100) > 20;
    }

    /**
     * performs the wheel logic every iteration
     */
    public void wheelsTick() {
        // Wait to get accurate RPM
        if (runTime.milliseconds() > lastKnownMS + 80) {

            // Set initial power equalization's
            if (!(currentRPM > rpmTarget - rpmLeniency && currentRPM < rpmTarget + rpmLeniency)) {
                power += currentRPM > rpmTarget + rpmLeniency ? -changeBy * ((Math.abs(currentRPM - rpmTarget)) / 10) : 0;
                power += currentRPM < rpmTarget - rpmLeniency ? changeBy * ((Math.abs(currentRPM - rpmTarget)) / 10) : 0;
                power = Math.max(-1, Math.min(1, power));
            }

            launchMotor.setPower(power);

            currentRPM =  (((launchMotor.getCurrentPosition() - lastKnownEnc) / encodersPerRevolution) * (60000 / (runTime.milliseconds() - lastKnownMS)));

            lastKnownMS = runTime.milliseconds();
            lastKnownEnc = launchMotor.getCurrentPosition();
        }
    }

    /**
     * set a new rpm goal
     * @param newRPM the new rpm goal
     */
    public void rpmReset(int newRPM) {
        rpmTarget = newRPM;
    }

    /**
     * retrieve the rpm
     * @return the rpm
     */
    public double rpm() {
        return currentRPM;
    }

    /**
     * directly set the motor powers (will cause issues if used in tandem with wheelsTick())
     * @param P new power for the launch motor
     */
    public void setMotorPowers(double P) {
        launchMotor.setPower(P);
        power = P;
    }

    /**
     * experimental ability to set the power to a specific rpm based off of empirically calculated data
     * @param RPM the expected rpm to achieve
     */
    public void setPowerByRpm(double RPM) {
        double judgedPower = (double) rpmTarget / 6000;
        setMotorPowers(judgedPower);
    }
}