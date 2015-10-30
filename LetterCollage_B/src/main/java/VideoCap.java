package main.java;

import java.awt.Dimension;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.photo.Photo;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

//FUNKTIONEN ANSCHAUEN:
/*
 * Mat.adjustROI(...);
 * Photo.inpaint(...);
 * 
 * 
 */
public class VideoCap extends JFrame {

	private static final String haarcascades_frontalface = "haarcascades/haarcascade_frontalface_default.xml";
	private static final String haarcascades_eye = "haarcascades/haarcascade_eye.xml";
	boolean faceDetectionOn = true;
	boolean eyeDetectionOn = true;

	private static final long serialVersionUID = -1421396949037115883L;

	public static void main(String args[]) {
		if (OpenCVUtils.loadLibrary(Core.NATIVE_LIBRARY_NAME)) {
			VideoCap vc = new VideoCap();
			vc.openCVVidoReader(new MainFrame());
		}
	}

	public void openCVVidoReader(MainFrame mf) {
		VideoCapture camera = new VideoCapture(0);
		MatOfByte mof = new MatOfByte();
		String path = System.getProperty("user.home") + "/Desktop" + "/camera.jpg";
		Mat image = null;
		BufferedImage bufImage = null;
		Object someObject = new Object();

		if (!camera.isOpened()) {
			System.out.println("No Camera found...=>Error");
		} else {
			setDefaultCameraOption(camera);
			//cropCameraSizeByK(camera, 0.5);

			image = new Mat();

			int steps = 100;
			long startTime = System.currentTimeMillis();
			int i = 0;
			while (i++ < steps) {
				if (camera.read(image)) {
					if (!image.empty()) {
						System.out.println("Captured Frame Width " + image.width() + " Height " + image.height());

						// Detect faces in the image.
						// MatOfRect is a special container class for Rect.
						if (faceDetectionOn) {
							String haarcascades_frontalfacePath = OSUtils.preparePathForOS(
									this.getClass().getClassLoader().getResource(haarcascades_frontalface).getPath());
							CascadeClassifier faceDetector = new CascadeClassifier(haarcascades_frontalfacePath);
							System.out.println(haarcascades_frontalfacePath);
							// CascadeClassifier eyeDetector = new
							// CascadeClassifier(haarcascades_eye);
							if (!faceDetector.empty()) {
								MatOfRect faceDetections = new MatOfRect();
								faceDetector.detectMultiScale(image, faceDetections);
								System.out.println(String.format("Detected %s faces!", faceDetections.toArray().length));

								// Draw a bounding box around each face.
								for (Rect rect : faceDetections.toArray()) {
									Imgproc.rectangle(image, new Point(rect.x, rect.y),
											new Point(rect.x + rect.width, rect.y + rect.height),
											new Scalar(0, 0, 255));
								}
							} else {
								System.out.println("Facedetector is empty...cannot detect faces!");
							}
						}

						//Imgproc.GaussianBlur(image, image, new Size(15, 15), 2, 2);
						Imgproc.putText(image, "A-TEAM [B]", new Point(30, 30), 2, 0.8, new Scalar(200, 200, 250), 2);
						mf.getBuffView().setBufferedImage(OpenCVUtils.matToBufferedImage(image));
					}
				}
			}

			long ms = (System.currentTimeMillis() - startTime);
			System.out.println(ms + " ms for " + steps + " steps!");
			System.out.println("Real FPS:" + (steps * 1000.0) / ms);
		}
		camera.release();
	}

	public void cropCameraSize(VideoCapture camera, int HEIGHT, int WIDTH) {
		if (camera.isOpened()) {
			// to get the actual width of the camera
			System.out.print("Width: " + camera.get(Videoio.CAP_PROP_FRAME_WIDTH));
			// to get the actual height of the camera
			System.out.println(", Height: " + camera.get(Videoio.CAP_PROP_FRAME_HEIGHT));

			boolean wset = camera.set(Videoio.CAP_PROP_FRAME_WIDTH, WIDTH);
			boolean hset = camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, HEIGHT);
			if (!hset || !wset) {
				System.out.println("Width Changed: " + wset);
				System.out.println("Height Changed: " + hset);

				// to get the actual width of the camera
				System.out.println(camera.get(Videoio.CAP_PROP_FRAME_WIDTH));
				// to get the actual height of the camera
				System.out.println(camera.get(Videoio.CAP_PROP_FRAME_HEIGHT));
			}
		}
	}

	public void cropCameraSizeByK(VideoCapture camera, double factor) {
		cropCameraSize(camera, (int) (camera.get(Videoio.CAP_PROP_FRAME_HEIGHT) * factor),
				(int) (camera.get(Videoio.CAP_PROP_FRAME_WIDTH) * factor));
	}

	public void setDefaultCameraOption(VideoCapture camera) {
		camera.set(Videoio.CAP_PROP_FPS, 25.0);
		camera.set(Videoio.CAP_PROP_CONVERT_RGB, 1.0);
		camera.set(Videoio.CAP_MODE_GRAY, 2);

		System.out.println("CAP_PROP_FPS: " + camera.get(Videoio.CAP_PROP_FPS));
		System.out.println("CAP_PROP_CONVERT_RGB: " + camera.get(Videoio.CAP_PROP_CONVERT_RGB));
		System.out.println("CAP_MODE_GRAY: " + camera.get(Videoio.CAP_MODE_GRAY));

	}
}