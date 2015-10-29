package main.java;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class OpenCVUtils {
	
	
	/**
	 * Converts/writes a Mat into a BufferedImage.
	 * 
	 * @param matrix Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */
	public static BufferedImage matToBufferedImage(Mat m) {
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (m.channels() > 1) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}

		int bufferSize = m.channels() * m.cols() * m.rows();
		byte[] b = new byte[bufferSize];
		m.get(0, 0, b); // get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(b, 0, targetPixels, 0, b.length);

		return image;
	}
	
	public static boolean loadLibrary(String libraryName){
		try {
			System.loadLibrary(libraryName);
		} catch (UnsatisfiedLinkError e) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("Wrong native %s library selected:\n", libraryName));
			sb.append("To change it in Eclipse:\n");
			sb.append("Build path\n\t-> Configure Build path...\n\t\t-> Libraries\n\t\t\t-> Click on 'OpenCV'\n\t\t\t\t-> Native library location\n\t\t\t\t\t-> Edit... \n");
			System.err.println(sb.toString());
			return false;
		}
		return true;
	}

	
	/*
	 * ////// Imgcodecs.imencode(path, frame, mof); byte[] byteArray =
	 * mof.toArray(); // Mapping #2
	 * 
	 * InputStream in = new ByteArrayInputStream(byteArray); try { bufImage =
	 * ImageIO.read(in); // Mapping #3 } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * camera.retrieve(frame); mainWindow.getBuffView().setImage(bufImage);
	 * //////
	 */
}
