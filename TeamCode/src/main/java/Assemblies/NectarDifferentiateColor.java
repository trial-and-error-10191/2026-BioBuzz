package Assemblies;

import android.graphics.Color;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

import java.util.List;

public class NectarDifferentiateColor {

    public ColorBlobLocatorProcessor colorLocator = new ColorBlobLocatorProcessor.Builder()
            .setTargetColorRange(ColorRange.BLUE)   // Use a predefined color match
            .setTargetColorRange(ColorRange.RED)   // Use a predefined color match
            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
            .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.75, 0.75, -0.75))
            .setDrawContours(true)   // Show contours on the Stream Preview
            .setBoxFitColor(0)       // Disable the drawing of rectangles
            .setCircleFitColor(Color.rgb(0, 0, 255)) // Draw a circle
            .setBlurSize(5)          // Smooth the transitions between different colors in image

            // the following options have been added to fill in perimeter holes.
            .setDilateSize(15)       // Expand blobs to fill any divots on the edges
            .setErodeSize(15)        // Shrink blobs back to original size
            .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)

            .build();

    Telemetry telemetry;
    VisionPortal visionPortal;
    VisionPortal.Builder builder = new VisionPortal.Builder();
    public NectarDifferentiateColor (HardwareMap hwMap, Telemetry telemetry) {
        // Create the vision portal by using a builder.
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(320, 240));
        visionPortal = builder.build();

        this.telemetry = telemetry;
    }
    public void FindNectar() {

        // Read the current list
        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                50, 20000, blobs);  // filter out very small blobs.

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                0.6, 1, blobs);     // filter out non-circular blobs.

        telemetry.addLine("Circularity Radius Center");
    }

    public void ShowFindings() {
        telemetry.addData("Fretting the worst already", "");
        if (!colorLocator.getBlobs().isEmpty()) {
            telemetry.addData("Blue nectar amount", colorLocator.getBlobs().get(0));
        }
        telemetry.update();
    }
}
