package test.java;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import main.java.MainFrame;
import main.java.OSUtils;
import main.java.OpenCVUtils;
import main.java.VideoCap;

public class ContentAwareTest {
	public static String horseCutted  = OSUtils.getResourcePathForOS("img/horsefenceCutted.png");
	public static String horseMask  = OSUtils.getResourcePathForOS("img/horsefenceMask.png");
	public static String savePath = System.getProperty("user.home") + "/Desktop" + "/horseInpainted.jpg";
	
	public static void main(String args[]) {
		if(OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME)){
			ContentAwareTest cat = new ContentAwareTest();
			cat.contentAware(new MainFrame());
		}
	}
	
	public void contentAware(MainFrame mf){
		Mat imgHorseCutted = Imgcodecs.imread(horseCutted);
		Mat imgHorseMask = Imgcodecs.imread(horseMask);

		Imgproc.cvtColor(imgHorseMask, imgHorseMask, Imgproc.COLOR_RGB2GRAY);
		//imgHorseMask.convertTo(imgHorseMask,  CvType.CV_8UC1);
		/*
		int height = (int) image.size().height;
		int width = (int) image.size().width;
		Mat maskMat = new Mat();
		maskMat.create(image.size(), CvType.CV_8U);
		maskMat.setTo(new Scalar(255,255,255));
		Point r1 = new Point(width/2-width/10, height/2-height/10);
		Point r2 = new Point(width/2+width/10, height/2+height/10);
		Scalar color = new Scalar(1);
		Imgproc.rectangle(maskMat, r1, r2, color, Core.FILLED);
		*/
		
		Photo.inpaint(imgHorseCutted, imgHorseMask, imgHorseCutted, imgHorseCutted.size().area()*0.00015, Photo.INPAINT_TELEA); 
		mf.getBuffView().setBufferedImage(OpenCVUtils.matToBufferedImage(imgHorseCutted));
		mf.getMaskView().setBufferedImage(OpenCVUtils.matToBufferedImage(imgHorseMask));
		Imgcodecs.imwrite(savePath, imgHorseCutted);
	}
}
