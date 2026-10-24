package Assemblies;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;

public class AprilTagCentering {
    Telemetry telemetry;

    double tag30Dist = 0;
    double tag31Dist = 0;
    double tag32Dist = 0;
    double tag33Dist = 0;
    double tag34Dist = 0;
    double tag35Dist = 0;
    double tag36Dist = 0;
    double tag37Dist = 0;

    public double averageDist = 0;

    int mediator = 0;

    public AprilTagCentering (AprilTagProcessor aprilTag, Telemetry telemetry) {
        aprilTag = new AprilTagProcessor.Builder()
                .setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                .build();

        // Lets the camera see the obelisk April Tag from far away, as we only need to see that one once.
        aprilTag.setDecimation(1);

        this.telemetry = telemetry;
    }
    public void findTagDist(AprilTagProcessor aprilTag) {
        tag30Dist = 0;
        tag31Dist = 0;
        tag32Dist = 0;
        tag33Dist = 0;
        tag34Dist = 0;
        tag35Dist = 0;
        tag36Dist = 0;
        tag37Dist = 0;
//        averageDist = 0;
        mediator = 0;
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                if (singleDet.metadata != null) {
                    if (singleDet.id == 30) {
                        tag30Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 31) {
                        tag31Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 32) {
                        tag32Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 33) {
                        tag33Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 34) {
                        tag34Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 35) {
                        tag35Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 36) {
                        tag36Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 37) {
                        tag37Dist = detection.ftcPose.range;
                        mediator++;
                    }
                    if (singleDet.id == 30 || singleDet.id == 31 || singleDet.id == 32 || singleDet.id == 33
                    || singleDet.id == 34 || singleDet.id == 35 || singleDet.id == 36 || singleDet.id == 37) {
                        averageDist = (tag30Dist + tag31Dist + tag32Dist + tag33Dist
                        + tag34Dist + tag35Dist + tag36Dist + tag37Dist) / mediator;
                    }
                }
            }
        }
    }

    public void philsFindTagDist(AprilTagProcessor aprilTag) {
        averageDist = 0.0;
        mediator = 0;

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection: currentDetections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDetection = (AprilTagSingleDetection) detection;
                if (singleDetection.metadata != null) {
                    if (singleDetection.id == 30) {
                        mediator++;
                        averageDist += singleDetection.ftcPose.range;
                    }
                    if (singleDetection.id == 31) {
                        mediator++;
                        averageDist += singleDetection.ftcPose.range;
                    }
                    if (singleDetection.id == 32) {
                        mediator++;
                        averageDist += singleDetection.ftcPose.range;
                    }
                }
            }
        }
        if (mediator > 0) {
            averageDist /= mediator;
        }

    }
}
