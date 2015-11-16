package main.java.de.ateam.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVUtils {
	
	
	/**
	 * Converts/writes a Mat into a BufferedImage.
	 * 
	 * @param m Mat of type CV_8UC3 or CV_8UC1
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

	public static Mat bufferedImageToMat2(BufferedImage img) {
        Mat out;
        byte[] data;
        int r, g, b, width = img.getWidth(), height = img.getHeight();

        if(img.getType() == BufferedImage.TYPE_INT_RGB)
        {
            out = new Mat(height, width, CvType.CV_8UC3);
            data = new byte[width * height * (int)out.elemSize()];
            int[] dataBuff = img.getRGB(0, 0, width, height, null, 0, width);
            for(int i = 0; i < dataBuff.length; i++)
            {
                data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
            }
        }
        else
        {
            out = new Mat(height, width, CvType.CV_8UC1);
            data = new byte[width * height * (int)out.elemSize()];
            int[] dataBuff = img.getRGB(0, 0, width, height, null, 0, width);
            for(int i = 0; i < dataBuff.length; i++)
            {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
            }
        }
        out.put(0, 0, data);
        return out;
	}


    // Convert image to Mat
    public static Mat bufferedImageToMat(BufferedImage im) {
        // Convert INT to BYTE
        // im = new BufferedImage(im.getWidth(), im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        // Convert bufferedimage to byte array
        byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();

        // Create a Matrix the same size of image
        Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_8UC3);
        // Fill Matrix with image values
        image.put(0, 0, pixels);

        return image;
    }


	public static boolean loadLibrary(String libraryName){
		try {
			System.loadLibrary(libraryName);
		} catch (UnsatisfiedLinkError e) {
            try {
                NativeUtils.loadLibraryFromJar("/opencv3/win/x64/opencv_java300.dll"); // during runtime. .DLL within .JAR
            } catch (IOException e1) {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Wrong native %s library selected:\n", libraryName));
                sb.append("To change it in Eclipse:\n");
                sb.append("Build path\n\t-> Configure Build path...\n\t\t-> Libraries\n\t\t\t-> Click on 'OpenCV'\n\t\t\t\t-> Native library location\n\t\t\t\t\t-> Edit... \n");
                System.err.println(sb.toString());
                e.printStackTrace();
                return false;
            }
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
