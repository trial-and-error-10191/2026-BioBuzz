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
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import java.util.List;

public class BallDifferentiateColor {

    ColorBlobLocatorProcessor colorLocatorRed = new ColorBlobLocatorProcessor.Builder()
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

    ColorBlobLocatorProcessor colorLocatorBlue = new ColorBlobLocatorProcessor.Builder()
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

    ColorBlobLocatorProcessor colorLocatorYellow = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(ColorRange.YELLOW)   // Use a predefined color match
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
        builder.addProcessors(colorLocatorRed, colorLocatorBlue, colorLocatorYellow);
        visionPortal = builder.build();

        this.telemetry = telemetry;
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

    public void ShowFindings() {
        telemetry.addData("Fretting the worst already", "");
        if (!colorLocatorBlue.getBlobs().isEmpty() || !colorLocatorRed.getBlobs().isEmpty() || !colorLocatorYellow.getBlobs().isEmpty()) {
            telemetry.addData("Red nectar amount", colorLocatorRed.getBlobs().size());
//            telemetry.addData("Blue nectar amount", colorLocatorBlue.getBlobs().size());
//            telemetry.addData("Pollen amount", colorLocatorYellow.getBlobs().size());
        }
        for (ColorBlobLocatorProcessor.Blob b : blobsRed) {
            Circle circleFit = b.getCircle();
            telemetry.addLine(String.format("%5.3f      %3d     (%3d,%3d)",
                    b.getCircularity(), (int) circleFit.getRadius(), (int) circleFit.getX(), (int) circleFit.getY()));
            telemetry.addData("Circle Center", circleFit.getCenter());
        }
        telemetry.update();
    }
}
