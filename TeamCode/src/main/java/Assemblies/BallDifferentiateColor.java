package Assemblies;

import android.graphics.Color;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ColorSpace;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Scalar;

import java.util.List;

public class BallDifferentiateColor {
    double lastBallDist = 0;
    int ballAmount = 0;
    ColorRange POLLEN_YELLOW = new ColorRange(
            ColorSpace.YCrCb,
            new Scalar(32, 60, 30),
            new Scalar(255, 160, 76)
    );
    public double ballDistInchRed, ballDistInchBlue, ballDistInchPollen;

    public ColorBlobLocatorProcessor colorLocatorRed = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(ColorRange.RED)   // Use a predefined color match
            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
            .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
            .setDrawContours(true)   // Show contours on the Stream Preview
            .setBoxFitColor(0)       // Disable the drawing of rectangles
            .setCircleFitColor(Color.rgb(255, 255, 0)) // Draw a circle
            .setBlurSize(5)          // Smooth the transitions between different colors in image

            // the following options have been added to fill in perimeter holes.
            .setDilateSize(15)       // Expand blobs to fill any divots on the edges
            .setErodeSize(15)        // Shrink blobs back to original size
            .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)

            .build();

    public ColorBlobLocatorProcessor colorLocatorBlue = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(ColorRange.BLUE)   // Use a predefined color match
            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
            .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
            .setDrawContours(true)   // Show contours on the Stream Preview
            .setBoxFitColor(0)       // Disable the drawing of rectangles
            .setCircleFitColor(Color.rgb(255, 255, 0)) // Draw a circle
            .setBlurSize(5)          // Smooth the transitions between different colors in image

            // the following options have been added to fill in perimeter holes.
            .setDilateSize(15)       // Expand blobs to fill any divots on the edges
            .setErodeSize(15)        // Shrink blobs back to original size
            .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)

            .build();

    public ColorBlobLocatorProcessor colorLocatorYellow = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(POLLEN_YELLOW)   // Use a predefined color match
            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
            .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
            .setDrawContours(true)   // Show contours on the Stream Preview
            .setBoxFitColor(0)       // Disable the drawing of rectangles
            .setCircleFitColor(Color.rgb(255, 255, 0)) // Draw a circle
            .setBlurSize(5)          // Smooth the transitions between different colors in image

            // the following options have been added to fill in perimeter holes.
            .setDilateSize(15)       // Expand blobs to fill any divots on the edges
            .setErodeSize(15)        // Shrink blobs back to original size
            .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)

            .build();

    Telemetry telemetry;
    VisionPortal visionPortal;
    VisionPortal.Builder builder = new VisionPortal.Builder();

    // Read the current lists
    List<ColorBlobLocatorProcessor.Blob> blobsRed = colorLocatorRed.getBlobs();
    List<ColorBlobLocatorProcessor.Blob> blobsBlue = colorLocatorBlue.getBlobs();
    List<ColorBlobLocatorProcessor.Blob> blobsYellow = colorLocatorYellow.getBlobs();

    public BallDifferentiateColor(HardwareMap hwMap, Telemetry telemetry) {
        // Create the vision portal by using a builder.
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(320, 240));

        this.telemetry = telemetry;
    }

    public void locateNectAndOrPol(boolean findAll, boolean nectOnly, ColorBlobLocatorProcessor desiredProcessor) {
        if (findAll) {
            builder.addProcessors(colorLocatorRed, colorLocatorBlue, colorLocatorYellow);
        } if (nectOnly) {
            builder.addProcessors(colorLocatorYellow, desiredProcessor);
        } if (!nectOnly && !findAll) {
            builder.addProcessor(colorLocatorYellow);
        }
        visionPortal = builder.build();
    }

    public void listUpdate() {
        blobsRed = colorLocatorRed.getBlobs();
        blobsBlue = colorLocatorBlue.getBlobs();
        blobsYellow = colorLocatorYellow.getBlobs();
    }

    public void FindBalls() {
        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000, blobsRed);  // filter out very small blobs.

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000, blobsBlue);

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000, blobsYellow);

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                0.6, 1, blobsRed);     // filter out non-circular blobs.

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                0.6, 1, blobsBlue);

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                0.6, 1, blobsYellow);
    }

    public void ballDist() {
        for (ColorBlobLocatorProcessor.Blob b : blobsRed) {
            Circle circleFit = b.getCircle();
            ballDistInchRed = Math.pow(circleFit.getRadius(), -0.82) * 378;
        }
        for (ColorBlobLocatorProcessor.Blob b : blobsBlue) {
            Circle circleFit = b.getCircle();
            ballDistInchBlue = Math.pow(circleFit.getRadius(), -0.82) * 378;
        }
        for (ColorBlobLocatorProcessor.Blob b : blobsYellow) {
            Circle circleFit = b.getCircle();
            ballDistInchPollen = Math.pow(Math.E, -0.0346 * circleFit.getRadius()) * 91;
        }
    }

    public void ballCount(boolean nectarRedFind, boolean pollenOnly) {
        double desiredNectarDist = 1E10;
        List<ColorBlobLocatorProcessor.Blob> desiredBlobs = blobsYellow;
        if (nectarRedFind && !pollenOnly) {
            desiredNectarDist = ballDistInchRed;
            desiredBlobs = blobsRed;
        } else if (!nectarRedFind && !pollenOnly) {
            desiredNectarDist = ballDistInchBlue;
            desiredBlobs = blobsBlue;
        }
        ballDist();
        if ((ballDistInchPollen < desiredNectarDist && ballDistInchPollen <= 40 && ballDistInchPollen != 0) || (desiredBlobs.isEmpty() && ballDistInchPollen > 0)) {
            lastBallDist = ballDistInchPollen;
//            telemetry.addData("A", "");
        }
        if (lastBallDist > ballDistInchPollen && ballDistInchPollen <= 35) {
            ballAmount++;
//            telemetry.addData("B", "");
        } else {
            telemetry.addData("Last Ball Dist", lastBallDist);
            telemetry.addData("Pollen Dist", ballDistInchPollen);
        }

        if (desiredNectarDist < ballDistInchPollen && desiredNectarDist <= 3 || (blobsYellow.isEmpty() && desiredNectarDist > 0)) {
            lastBallDist = desiredNectarDist;
//            telemetry.addData("C", "");
        }
        if (lastBallDist > desiredNectarDist && desiredNectarDist == 0) {
            ballAmount++;
//            telemetry.addData("D", "");
        }
        telemetry.update();
    }

    public void ShowFindings() {
        telemetry.addData("Fretting the worst already", "");
        if (!colorLocatorBlue.getBlobs().isEmpty() || !colorLocatorRed.getBlobs().isEmpty() || !colorLocatorYellow.getBlobs().isEmpty()) {
            telemetry.addData("Red nectar distance away from cam (inches)", ballDistInchRed);
            telemetry.addData("Blue nectar distance away from cam (inches)", ballDistInchBlue);
            telemetry.addData("Pollen distance away from cam (inches)", ballDistInchPollen);
            for (ColorBlobLocatorProcessor.Blob b : blobsRed) {
                Circle circleFit = b.getCircle();
                telemetry.addLine(String.format("%5.3f      %3d     (%3d,%3d)",
                        b.getCircularity(), (int) circleFit.getRadius(), (int) circleFit.getX(), (int) circleFit.getY()));
                telemetry.addData("Circle Center X Red", circleFit.getCenter().x);
                telemetry.addData("Circle Center Y Red", circleFit.getCenter().y);
            }
            for (ColorBlobLocatorProcessor.Blob b : blobsBlue) {
                Circle circleFit = b.getCircle();
                telemetry.addLine(String.format("%5.3f      %3d     (%3d,%3d)",
                        b.getCircularity(), (int) circleFit.getRadius(), (int) circleFit.getX(), (int) circleFit.getY()));
                telemetry.addData("Circle Center X Blue", circleFit.getCenter().x);
                telemetry.addData("Circle Center Y Blue", circleFit.getCenter().y);
            }
            for (ColorBlobLocatorProcessor.Blob b : blobsYellow) {
                Circle circleFit = b.getCircle();
                telemetry.addLine(String.format("%5.3f      %3d     (%3d,%3d)",
                        b.getCircularity(), (int) circleFit.getRadius(), (int) circleFit.getX(), (int) circleFit.getY()));
                telemetry.addData("Circle Center X Yellow", circleFit.getCenter().x);
                telemetry.addData("Circle Center Y Yellow", circleFit.getCenter().y);
            }
        }
        telemetry.update();
    }
}
