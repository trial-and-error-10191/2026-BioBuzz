package org.firstinspires.ftc.teamcode.Map;

import android.util.Pair;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.HashMap;

abstract class MapBase {

    // Tag linkages
    HashMap<Integer, Pose3D> aprilTagMap = new HashMap<Integer, Pose3D>();

    // my field position.
    Pose3D Position = new Pose3D(new Position(), new YawPitchRollAngles(AngleUnit.DEGREES,0,0,0,0));

    /**
     * set the initial pose on the field.
     * @param startingPose
     */
    public final void setStartingPose(Pose3D startingPose) {customSetStartingPose(startingPose);}
    abstract void customSetStartingPose(Pose3D startingPose);

    /**
     * add pairs of april tag ID to pose on the field.
     * @param AprilTagPosePairs all pairs of poses and tags to add this call.
     */
    @SafeVarargs
    public final void addAprilTagPoses(Pair<Integer, Pose3D>... AprilTagPosePairs) {
        for (Pair<Integer, Pose3D> tagPair : AprilTagPosePairs) {
            aprilTagMap.put(tagPair.first, tagPair.second);
        }
    }

    /**
     * Update current field pose based upon known pose of apriltag versus the observed pose.
     */
    public final void updateFromTags() {

    }

    /**
     * Update the pose based upon motor movement
     */
    public final void updateFromMotors() {customUpdateFromMotors();}
    abstract void customUpdateFromMotors();
}
