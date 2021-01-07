package de.ateam.utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

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

        if (img.getType() == BufferedImage.TYPE_INT_RGB) {
            out = new Mat(height, width, CvType.CV_8UC3);
            data = new byte[width * height * (int) out.elemSize()];
            int[] dataBuff = img.getRGB(0, 0, width, height, null, 0, width);
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
            }
        } else {
            out = new Mat(height, width, CvType.CV_8UC1);
            data = new byte[width * height * (int) out.elemSize()];
            int[] dataBuff = img.getRGB(0, 0, width, height, null, 0, width);
            for (int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
            }
        }
        out.put(0, 0, data);
        return out;
    }


    public static Mat bufferedImageToMat(BufferedImage im) {
        // Convert INT to BYTE
        // im = new BufferedImage(im.getWidth(), im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        // Convert bufferedimage to byte array
        byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();


        // Create a Matrix the same size of image
        int im_cvType;
        switch (im.getType()) {
            case BufferedImage.TYPE_BYTE_GRAY:
                im_cvType = CvType.CV_8UC1;
                break;
            default:
                im_cvType = CvType.CV_8UC3;
                break;
        }
        Mat image = new Mat(im.getHeight(), im.getWidth(), im_cvType);
        // Fill Matrix with image values
        image.put(0, 0, pixels);

        return image;
    }


    public static boolean loadLibrary(String libraryName) {
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

    // konvertiert rgb in hsv - müsste ggf in eine andere helper klasse wenn mehrere statische umwandlungsfunktionen gebraucht werden
    public static float[] rgbTohsv(int[] color) {
        float r = (float) color[0] / 255, g = (float) color[1] / 255, b = (float) color[2] / 255;
        float[] hsv = new float[3];
        int id = 0;
        float max = r;
        float min = r;
        // min max
        if (max < g) {
            max = g;
            id = 1;
        }
        if (max < b) {
            max = b;
            id = 2;
        }
        if (min > g) {
            min = g;
            id = 1;
        }
        if (min > b) {
            min = b;
            id = 2;
        }

        // sonderfall
        if (max == min) {
            hsv[0] = 0;
        } else {
            // h setzen
            switch (id) {
                case 0:
                    hsv[0] = 60 * ((g - b) / (max - min));
                    break;
                case 1:
                    hsv[0] = 60 * ((b - r) / (max - min));
                    break;
                case 2:
                    hsv[0] = 60 * ((r - g) / (max - min));
                    break;
            }
        }
        // um in 360 zu bleiben
        if (hsv[0] < 0)
            hsv[0] += 360;
        // s setzen
        if (max == 0)
            hsv[1] = 0;
        else
            hsv[1] = (max - min) / max;

        // v setzen
        hsv[2] = max;

        return hsv;
    }

    public static float hsvSimilarity(float[] a, float[] b) {
        float sim = 0;
        float h = Math.abs(a[0] - b[0]) / 360, s = Math.abs(a[1] - b[1]), v = Math.abs(a[2] - b[2]);
        sim = (1 - h) * a[1] * a[2];
        if (s < 0.6 || v < 0.6) {
            sim = ((1 - v) + (1 - s)) / 2;
        }
        return sim;
    }

    public static float[] getHSVPixel(byte[] pixels, int x, int y, int width) {
        int j = x + (y * width);
        int[] ref = new int[3];
        ref[0] = pixels[3 * j + 2] & 0xFF;
        ref[1] = pixels[3 * j + 1] & 0xFF;
        ref[2] = pixels[3 * j] & 0xFF;
        return rgbTohsv(ref);
    }

    public static BufferedImage copyImage(BufferedImage source, int imgType) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), imgType);
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage copyImage(BufferedImage source) {
        return copyImage(source, source.getType());
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
