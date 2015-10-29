import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
  
public class VideoCap extends JFrame{
	
	private static final String haarcascades_frontalface = "C:\\Users\\Florian\\Documents\\Git\\LetterCollage_B\\LetterCollage_B\\res\\haarcascades\\haarcascade_frontalface_default.xml";
	private static final String haarcascades_eye = "C:\\Users\\Florian\\Documents\\Git\\LetterCollage_B\\LetterCollage_B\\res\\haarcascades\\haarcascade_eye.xml";

	
	private static final long serialVersionUID = -1421396949037115883L;

	public static void main(String args[]){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	VideoCap vc = new VideoCap();
    	/*
    	JFrame mainWindow = vc.initializeUI();
    	vc.openCVVidoReader(mainWindow);
    	*/

		vc.openCVVidoReader(new MainFrame());
    }
    
    public void openCVVidoReader(MainFrame mf){
    	//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	VideoCapture camera = new VideoCapture(0);
		MatOfByte mof = new MatOfByte();
		String path = System.getProperty("user.home") + "/Desktop" + "/camera.jpg";
		Mat image = null;
		BufferedImage bufImage = null;
		
    	if(!camera.isOpened()){
    		System.out.println("Error");
    	}
    	else {
    		image = new Mat();
    	    while(true){
    	    	if (camera.read(image)){
	    	    	if (!image.empty()){
	    	    		System.out.println("Captured Frame Width " + image.width() + " Height " + image.height());
	    	    		
	    	    		// Detect faces in the image.
	    	    	    // MatOfRect is a special container class for Rect.
	    	    		//CascadeClassifier faceDetector = new CascadeClassifier(this.getClass().getClassLoader().getResource("haarcascades/haarcascade_frontalface_default.xml").getPath());
	    	    		//Kleiner fail, CascadeClassifier WILL ein pfad, aber classLoader kann keinen internen Pfad zurückgeben, somit nun erstmal hardcoded....
	    	    		System.out.println(this.getClass().getClassLoader().getResource("haarcascades/haarcascade_frontalface_default.xml").getPath());
	    	    		CascadeClassifier faceDetector = new CascadeClassifier(haarcascades_frontalface);
	    	    		CascadeClassifier eyeDetector = new CascadeClassifier(haarcascades_eye);
	    	    		if (!faceDetector.empty()) {
		    	    		MatOfRect faceDetections = new MatOfRect();
		    	    		MatOfRect eyeDetections = new MatOfRect();
		    	    	    faceDetector.detectMultiScale(image, faceDetections);
		    	    	    eyeDetector.detectMultiScale(image, eyeDetections);
		    	    	    System.out.println(String.format("Detected %s faces and %s eyes!", faceDetections.toArray().length, eyeDetections.toArray().length));
		    	    	    // Draw a bounding box around each face.
		    	    	    for (Rect rect : faceDetections.toArray()) {
		    	    	        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		    	    	    }
		    	    	    for (Rect rect : eyeDetections.toArray()) {
		    	    	        Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		    	    	    }
	    	    		}
	    	    		else{
	    	    			System.out.println("Facedetector is empty...cannot detect faces!");
	    	    		}

	    	    		mf.getBuffView().setImage(matToBufferdImage(image));
	    	    		Imgcodecs.imwrite(path, image);
	    	    		//Imgcodecs.imwrite(path, mof);
	    	    		//Highgui.imwrite("camera.jpg", frame);
	    	    		//mof.copyTo(frame);
	
	    	    		//break;
	    	    	}
    	    	}
    	    }	
    	}
    	camera.release();
		//mainWindow.getBuffView().setImage(matToBufferdImage(mof));
		//mainWindow.revalidate();
		//mainWindow.repaint();
    }
    
    public BufferedImage matToBufferdImage(Mat m){
    	int type = BufferedImage.TYPE_BYTE_GRAY;

        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  

        return image;
    }  
    
	
	/*
	//////
	Imgcodecs.imencode(path, frame, mof);
	byte[] byteArray = mof.toArray(); // Mapping #2

	InputStream in = new ByteArrayInputStream(byteArray);
	try {
		bufImage = ImageIO.read(in); // Mapping #3
	} catch (IOException e) {
		e.printStackTrace();
	}
	
	camera.retrieve(frame);
	mainWindow.getBuffView().setImage(bufImage);
	//////
	*/
}   