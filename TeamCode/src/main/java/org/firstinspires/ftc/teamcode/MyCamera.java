package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MyCamera {
    private VisionPortal visionPortal = null;        // Used to manage the video source.
    private AprilTagProcessor aprilTag = null;

    public MyCamera(HardwareMap hwMap) {
        // Create the AprilTag processor.
        aprilTag = new AprilTagProcessor.Builder()

                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(true) // Changed in V12.0
                //.setDrawTagOutline(true)
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                //.setDrawCubeProjection(false)

                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.

                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second (default)
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second (default)
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        //aprilTag.setDecimation(3);

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(aprilTag);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Disable or re-enable the aprilTag processor at any time.
        //visionPortal.setProcessorEnabled(aprilTag, true);
    }

    ArrayList<AprilTagSingleDetection> FindAprilTags() {
        ArrayList<AprilTagSingleDetection> result = new ArrayList<AprilTagSingleDetection>();
        List<AprilTagDetection> myList = aprilTag.getDetections();
        for (AprilTagDetection detection : myList) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection sd = (AprilTagSingleDetection) detection;
                if (sd.metadata != null) {
                    result.add(sd);
                }
            }
        }
        return result;
    }


    ArrayList<AprilTagSingleDetection> FilterAprilTags(ArrayList<AprilTagDetection> aprilTags) {
        HashSet<Integer> aprilTagTargets = new HashSet<Integer>();
        aprilTagTargets.add(41);
        aprilTagTargets.add(42);
        aprilTagTargets.add(43);
        aprilTagTargets.add(44);

        ArrayList<AprilTagSingleDetection> newAprilTagList = new ArrayList<AprilTagSingleDetection>();
        for (AprilTagDetection detection : aprilTags) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection sd = (AprilTagSingleDetection) detection;
                if (sd.metadata != null) {
                    if (aprilTagTargets.contains(sd.id)) {
                        newAprilTagList.add(sd);
                    }
                }
            }
        }
        return newAprilTagList;
    }
}
