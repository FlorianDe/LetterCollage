package de.ateam.controller.roi;

import de.ateam.utils.FileLoader;
import de.ateam.utils.OpenCVUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Florian on 20.01.2016.
 */
public class SaliencyMapDetector {

    static {
        OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public SaliencyMapDetector() {

    }

    public static void main(String[] args) {
        Mat img = null;
        for (int i = 1; i <= 23; i++) {
            boolean succesfullyLoadedImage = true;
            String fileExtension = "jpg";
            String fileName = "img/people/" + "test" + i + ".";

            try {
                img = OpenCVUtils.bufferedImageToMat(ImageIO.read(FileLoader.loadFile(fileName + fileExtension)));
            } catch (IllegalArgumentException | IOException e) {
                System.out.println("Couldn't load " + fileName + fileExtension + ". Trying .png instead!");
                fileExtension = "png";
            }
            try {
                img = OpenCVUtils.bufferedImageToMat(ImageIO.read(FileLoader.loadFile(fileName + fileExtension)));
            } catch (IllegalArgumentException | IOException e) {
                System.out.println("Couldn't load " + fileName + fileExtension + ". Trying next image!");
                succesfullyLoadedImage = false;
            }
            if (succesfullyLoadedImage) {
                System.out.println("Loaded " + fileName + fileExtension);

                img.convertTo(img, CvType.CV_32F);

                SaliencyMapDetector smd = new SaliencyMapDetector();
                ArrayList<Mat> salMaps = new ArrayList<>();
                for (int j = 33; j <= 42; j++) {
                    Mat salMap = smd.calculate(img.clone(), j / 100.0);

                    Mat result = new Mat(salMap.size(), CvType.CV_8UC3);
                    salMap.convertTo(result, CvType.CV_8UC3);
                    BufferedImage buf = OpenCVUtils.matToBufferedImage(result);

                    String savePath = fileName + "thresh_" + j + "_." + fileExtension;
                    try {
                        ImageIO.write(buf, fileExtension, new File("C:\\Users\\Florian\\Desktop\\" + savePath));
                        System.out.println("Saved " + savePath + "\n");
                    } catch (IOException e) {
                        System.out.println("Couldn't save " + savePath + "\n");
                    }
                }
            }
        }
    }


    public Mat calculate(Mat image, double threshold) {
        Mat saliencyMap = new Mat(image.size(), CvType.CV_8U);
        Mat Ie = image.clone();

        image.convertTo(saliencyMap, CvType.CV_8U);
        Mat xGrad = new Mat(image.size(), CvType.CV_16S);
        Mat yGrad = new Mat(image.size(), CvType.CV_16S);
        Mat xGradAbs = new Mat(image.size(), CvType.CV_8U);
        Mat yGradAbs = new Mat(image.size(), CvType.CV_8U);
        int ddepth = 3;
        Imgproc.Sobel(saliencyMap, xGrad, ddepth, 1, 0);
        Imgproc.Sobel(saliencyMap, yGrad, ddepth, 0, 1);

        Core.convertScaleAbs(xGrad, xGradAbs);
        Core.convertScaleAbs(yGrad, yGradAbs);

        Core.addWeighted(xGradAbs, 0.5, yGradAbs, 0.5, 1, image, CvType.CV_32F);


        //TODO WHY ERROR?!
        Mat Iobr = image;//morphReconstruct(Ie, image);

        //Calculates the mean value M of array elements, independently for each channel
        Scalar meanScalar = Core.mean(Iobr);
        double meanVal = meanOfScalar(meanScalar);

        //rare case when saliency map is completely eroded
        if (Double.compare(meanVal, 0.0) == 0) {
            byte[] arrayData = new byte[image.height() * image.width() * image.channels()];
            image.get(0, 0, arrayData);

            for (int y = 0; y < arrayData.length; y++) {
                arrayData[y] = (byte) ((arrayData[y] > 0.0) ? 255 : 0);
            }
            saliencyMap.put(0, 0, arrayData);
            return saliencyMap;
        }


        //calculate regional maxima
        saliencyMap = getRegionalMaxima(Iobr);


        //Create a mat with connected components
        Mat labelimg = new Mat(saliencyMap.size(), CvType.CV_32S);
        Mat saliencyMapGrayScale1 = new Mat();
        Mat saliencyMapGrayScale2 = new Mat();
        Imgproc.cvtColor(saliencyMap, saliencyMapGrayScale1, Imgproc.COLOR_BGR2GRAY);

        saliencyMapGrayScale1.convertTo(saliencyMapGrayScale2, CvType.CV_8U);
        Imgproc.connectedComponents(saliencyMapGrayScale2, labelimg);


        //INVERT MAT!!!
        //Mat labelimgHelper = new Mat(labelimg.size(), CvType.CV_8U);
        //Core.subtract(new Mat(labelimg.height(),labelimg.width(),labelimg.type(),new Scalar(255)),labelimg,labelimgHelper);
        //labelimgHelper.convertTo(labelimgHelper, CvType.CV_8U);
        //Imgproc.blur(labelimgHelper,labelimgHelper, new Size(3,3));

        HashMap<Integer, RegionIntensity> regions = regionProbs2(labelimg, image);
        rescale(regions);
        regions = filterThreshold(regions, threshold);
        saliencyMap = isMember(labelimg, regions);


        Mat labelimgHelper = new Mat(saliencyMap.size(), CvType.CV_8U);
        //Core.subtract(new Mat(saliencyMap.size(),saliencyMap.type(),new Scalar(255)),saliencyMap,labelimgHelper);
        saliencyMap.convertTo(labelimgHelper, CvType.CV_8U);


        //Imgproc.blur(labelimgHelper,labelimgHelper, new Size(3,3));
        //Imgproc.erode(labelimgHelper, labelimgHelper, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new Size(3,3),new Point(1,1)));
        //Mat res = new Mat();
        Imgproc.dilate(labelimgHelper, saliencyMap, getCenteredStructuringEllipseElement(9));
        //Imgproc.erode(labelimgHelper, labelimgHelper, getCenteredStructuringEllipseElement(15));

        System.out.println("SaliencyMapDetector saliencyMap channels:" + saliencyMap.channels());

        return saliencyMap;
    }

    public Mat getCenteredStructuringEllipseElement(int radius) {
        return Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2 * radius + 1, 2 * radius + 1), new Point(radius, radius));
    }

    private Mat isMember(Mat labelimg, HashMap<Integer, RegionIntensity> regions) {
        Mat res = Mat.zeros(labelimg.size(), CvType.CV_8U);

        for (Map.Entry<Integer, RegionIntensity> entry : regions.entrySet()) {
            for (Point p : entry.getValue()) {
                res.put((int) p.y, (int) p.x, new byte[]{(byte) 255});
            }
        }

        return res;
    }

    private HashMap<Integer, RegionIntensity> filterThreshold(HashMap<Integer, RegionIntensity> regions, double threshold) {
        HashMap<Integer, RegionIntensity> res = new HashMap<>();
        for (Map.Entry<Integer, RegionIntensity> entry : regions.entrySet()) {
            if (entry.getValue().getIntensitiy() > threshold)
                res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public HashMap<Integer, RegionIntensity> regionProbs2(Mat regions, Mat image) {
        HashMap<Integer, RegionIntensity> regionsKeyPoints = new HashMap<>();
        for (int y = 0; y < regions.height(); y++) {
            for (int x = 0; x < regions.width(); x++) {
                double[] values = regions.get(y, x);
                if (values.length > 0) {
                    int key = (int) values[0];
                    if (!regionsKeyPoints.containsKey(key)) {
                        regionsKeyPoints.put(key, new RegionIntensity());
                    } else {
                        regionsKeyPoints.get(key).add(new Point(x, y));
                    }
                }
            }
        }

        for (RegionIntensity ri : regionsKeyPoints.values()) {
            double avg = 0;
            for (Point p : ri) {
                avg += image.get((int) p.y, (int) p.x)[0];
            }
            avg = (ri.size() != 0) ? avg / ri.size() : 0;
            ri.setIntensitiy(avg);
        }

        return regionsKeyPoints;
    }

    public Mat regionProbs(Mat src_gray) {

        Mat _img = new Mat();
        double otsu_thresh_val = Imgproc.threshold(src_gray, _img, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        //TODO ANPASSEN?! Eigtl (otsu_thresh_val*0.5, otsu_thresh_val)
        return regionProbs(src_gray, otsu_thresh_val, otsu_thresh_val * 2);
    }

    public Mat regionProbs(Mat src_gray, double threshMin, double threshMax) {
        long start = System.currentTimeMillis();
        Mat canny_output = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        /// Detect edges using canny
        //Imgproc.Canny( src_gray, canny_output, threshMin, threshMax);
        /// Find contours
        Imgproc.findContours(src_gray, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        /// Draw contours
        Mat drawing = Mat.zeros(canny_output.size(), CvType.CV_8UC3);
        Scalar blackColor = new Scalar(255, 255, 255);
        long end1 = System.currentTimeMillis();
        ArrayList<ContourExtension> contoursExtensions = new ArrayList<>();
        ArrayList<MatOfPoint> contoursAfterThreshhold = new ArrayList<>();
        double max = 0;
        for (MatOfPoint mop : contours) {
            //max = ((mop.total() > max)?mop.total():max);
            if (mop.total() > 40) {
                ContourExtension cnte = new ContourExtension(mop);
                contoursExtensions.add(cnte);
                if (cnte.meanIntensity > max) {
                    max = cnte.meanIntensity;
                }
                Point[] points = mop.toArray();
            }
        }
        //rescale(contoursExtensions);
        for (ContourExtension contureExt : contoursExtensions) {
            if ((contureExt.meanIntensity / max) > 0.2) {

            }
            contoursAfterThreshhold.add(contureExt.conture);
        }

        long end2 = System.currentTimeMillis();
        byte[] data = new byte[(int) (drawing.total() * drawing.channels())];
        drawing.get(0, 0, data);
        for (int i = 0; i < contoursAfterThreshhold.size(); i++) {
            //Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );
            Imgproc.drawContours(drawing, contoursAfterThreshhold, i, blackColor, 3, 8, hierarchy, 0, new Point());
            Point[] points = contoursAfterThreshhold.get(i).toArray();
            byte col = (byte) contoursAfterThreshhold.get(i).total();
            for (int j = 0; j < points.length; j++) {
                int ind = (int) (points[j].x + (points[j].y * drawing.width())) * drawing.channels();
                for (int c = 0; c < drawing.channels(); c++) {
                    //data[ind + c] = (byte)col;
                }
            }
        }
        //drawing.put(0,0,data);
        long end3 = System.currentTimeMillis();
        System.out.printf("regionProbsPart1: %sms,\nregionProbsPart1: %sms,\n" +
                "regionProbsPart1: %sms", end1 - start, end2 - start, end3 - start);

        return drawing;
    }

    public Mat morphReconstruct(Mat marker, Mat mask) {
        Mat dst = new Mat(marker.height(), marker.width(), marker.type());

        Core.min(marker, mask, dst);
        Imgproc.dilate(dst, dst, new Mat());
        Core.min(dst, mask, dst);
        Mat temp1 = new Mat(marker.size(), CvType.CV_8UC1);
        Mat temp2 = new Mat(marker.size(), CvType.CV_8UC1);
        do {
            dst.copyTo(temp1);
            Imgproc.dilate(dst, dst, new Mat());
            Core.min(dst, mask, dst);
            Core.compare(temp1, dst, temp2, Core.CMP_NE);
        }
        while (Core.sumElems(temp2).val[0] != 0);

        return dst;
    }

    public Mat getRegionalMaxima(Mat image) {
        Mat res = new Mat(image.size(), image.type());

        float[] data = new float[(int) (image.total() * image.channels())];
        image.get(0, 0, data);
        for (int i = 0; i < data.length / image.channels(); i++) {
            for (int c = 0; c < image.channels(); c++) {
                int xP = i % image.width();
                int yP = i / image.width();
                float max = 0;
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        int xR = xP + x;
                        int yR = yP + y;
                        if (xR >= 0 && xR < image.width() && yR >= 0 && yR < image.height() && x != 0 && y != 0) {
                            float val = data[(xR + (yR * image.width())) * image.channels() + c];
                            if (val > max) {
                                max = val;
                            }
                        }
                    }
                }
                int ind = (xP + (yP * image.width())) * image.channels() + c;
                data[ind] = (float) ((data[ind] >= max) ? 1 : 0);
            }
        }
        res.put(0, 0, data);
        return res;
    }

    public double meanOfScalar(Scalar scalar) {
        double meanVal = 0;
        int channels = scalar.val.length;
        for (int c = 0; c < channels; c++) {
            meanVal += scalar.val[c];
        }
        return meanVal /= channels;
    }

    public void rescale(HashMap<Integer, RegionIntensity> regions) {
        rescale(regions, 0.0);
    }

    public void rescale(HashMap<Integer, RegionIntensity> regions, double a) {
        rescale(regions, a, 1.0);
    }

    public void rescale(HashMap<Integer, RegionIntensity> regions, double a, double b) {
        double m = Double.MAX_VALUE;
        double M = 0.0;

        for (RegionIntensity region : regions.values()) {
            double actIntensity = region.getIntensitiy();
            if (actIntensity > M) {
                M = actIntensity;
            }
            if (actIntensity < m) {
                m = actIntensity;
            }
        }

        //System.out.println("Min:"+m);
        //System.out.println("Max:"+M);

        if (M - m < 0.00000000000000000000001) {
            return;
        } else {
            for (Map.Entry<Integer, RegionIntensity> entry : regions.entrySet()) {
                entry.getValue().setIntensitiy((b - a) * (entry.getValue().getIntensitiy() - m) / (M - m) + a);
            }
        }
    }

    class RegionIntensity extends ArrayList<Point> {
        double intensitiy;

        public RegionIntensity() {
            super();
        }

        public double getIntensitiy() {
            return intensitiy;
        }

        public void setIntensitiy(double intensitiy) {
            this.intensitiy = intensitiy;
        }
    }

    class ContourExtension {
        public MatOfPoint conture;
        public double meanIntensity;

        public ContourExtension(MatOfPoint conture) {
            this.conture = conture;
            Point[] points = conture.toArray();
            double xMin = Double.MAX_VALUE;
            double yMin = Double.MAX_VALUE;
            double xMax = 0;
            double yMax = 0;
            // find min max
            for (int i = 0; i < points.length; i++) {
                if (points[i].x > xMax)
                    xMax = points[i].x;
                if (points[i].y > yMax)
                    yMax = points[i].y;
                if (points[i].x < xMin)
                    xMin = points[i].x;
                if (points[i].y < yMin)
                    yMin = points[i].y;
            }
            this.meanIntensity = ((xMax - xMin) * (yMax - yMin));
            //this.meanIntensity = meanOfScalar(Core.mean(conture));
        }
    }

}
