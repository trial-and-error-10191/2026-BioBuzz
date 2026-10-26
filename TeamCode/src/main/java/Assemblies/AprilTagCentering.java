package Assemblies;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

public class AprilTagCentering {
    Telemetry telemetry;

    public AprilTagCentering (AprilTagProcessor aprilTag, Telemetry telemetry) {
        aprilTag = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        // Lets the camera see the obelisk April Tag from far away, as we only need to see that one once.
        aprilTag.setDecimation(1);

        this.telemetry = telemetry;
    }

    public double findClusterDist(AprilTagProcessor aprilTag) {
        double distAwayFromTags = 0;
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                if (singleDet.metadata != null) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                } else {
                    telemetry.addLine(String.format("\n==== (ID %d) Unknown", singleDet.id));
                    telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", singleDet.center.x, singleDet.center.y));
                }
            }
            else {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
//                if (clusterDet.metadata.name.charAt(0) == 'B') { // Makes the robot only look for red clusters
//                    continue;
//                } if (Math.abs(clusterDet.ftcPose.roll) > 90) { // Makes the robot only look for right side up clusters
//                    continue;
//                }
                distAwayFromTags = clusterDet.ftcPose.range;
//                telemetry.addData("Cluster Amount", clusterDet.percentClusterFound);
//                telemetry.addData("Cluster Range", clusterDet.ftcPose.range);
//                telemetry.update();
            }
        }
        return distAwayFromTags;
    }
}
