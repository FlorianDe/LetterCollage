package test;

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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
/*
import org.opencv.highgui.Highgui;        
import org.opencv.highgui.VideoCapture;        
  */      
public class VideoCap extends JFrame{
	
    public static void main (String args[]){
    	/*
    	VideoCap vc = new VideoCap();
    	JFrame mainWindow = vc.initializeUI();
    	vc.openCVVidoReader(mainWindow);
    	*/

		MainFrame mf = new MainFrame();
		openCVVidoReader(mf);
    }
    
    public static void openCVVidoReader(MainFrame mainWindow){
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	VideoCapture camera = new VideoCapture(0);
		MatOfByte mof = new MatOfByte();
		String path = System.getProperty("user.home") + "/Desktop" + "/camera.jpg";
		Mat frame = null;
		BufferedImage bufImage = null;
		
    	if(!camera.isOpened()){
    		System.out.println("Error");
    	}
    	else {
    		frame = new Mat();
    	    while(true){
    	    	if (camera.read(frame)){
	    	    	if (!frame.empty()){
	    	    		System.out.println("Captured Frame Width " + frame.width() + " Height " + frame.height());
	    	    		
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
	    	    		mainWindow.getBuffView().setImage(matToBufferdImage(frame));
	    	    		Imgcodecs.imwrite(path, frame);
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
    
    public static BufferedImage matToBufferdImage(Mat m){
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
}   