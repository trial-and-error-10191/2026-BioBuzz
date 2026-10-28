package Assemblies;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

public class AprilTagCentering {
    double distAwayFromTags;
    Telemetry telemetry;

    public AprilTagCentering (AprilTagProcessor aprilTag, Telemetry telemetry) {
        aprilTag = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        // Lets the camera see the obelisk April Tag from far away, as we only need to see that one once.
        aprilTag.setDecimation(1);

        this.telemetry = telemetry;
    }

    public double findClusterDist(AprilTagProcessor aprilTag, char desiredChar) {
        distAwayFromTags = 0;
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        singleTagDetect(currentDetections);
        clusterTagDetect(currentDetections, desiredChar);
        return distAwayFromTags;
    }
    public void singleTagDetect(List<AprilTagDetection> currentDetections) {
        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                if (singleDet.metadata != null) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                }
            }
        }
    }
    public int aprilTagClusterFilter(AprilTagClusterDetection clusterDet, char desiredChar) {
        int killSwitch = 0;
        if (clusterDet.metadata.name.charAt(0) != desiredChar) { // Makes the robot only look for desired clusters
            killSwitch = 1;
        }
        if (Math.abs(clusterDet.ftcPose.roll) > 90) {
            killSwitch = 1;
        }
        return killSwitch;
    }
    public void clusterTagDetect(List<AprilTagDetection> currentDetections, char desiredChar) {
        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagClusterDetection) {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                if (aprilTagClusterFilter(clusterDet, desiredChar) == 1) {
                    continue;
                }
                distAwayFromTags = clusterDet.ftcPose.range;
                telemetry.addData("Cluster Amount", clusterDet.percentClusterFound);
                telemetry.addData("Cluster Range", clusterDet.ftcPose.range);
                telemetry.addData("Cluster Name", clusterDet.metadata.name);
                telemetry.update();
            }
        }
    }
}