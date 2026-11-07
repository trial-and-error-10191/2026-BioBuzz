package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.ftccommon.internal.manualcontrol.responses.MotorModeResponse;

@TeleOp(name="Single Motor Test")
public class MotorTest extends LinearOpMode {
    DcMotor motor = null;

    @Override
    public void runOpMode() {
        motor = hardwareMap.get(DcMotor.class, "test_motor");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Controller Input:", gamepad1.left_stick_y);
            motor.setPower(gamepad1.left_stick_y);
            telemetry.addData("", "");
            telemetry.addData("==========================================", "");
            telemetry.addData("", "");
            telemetry.addData("Motor Response:", motor.getPower());
            telemetry.update();
        }
    }
}
