package de.ateam.controller.roi;

import de.ateam.controller.ICollageController;
import de.ateam.model.roi.RegionOfInterestImage;
import de.ateam.model.text.Letter;
import de.ateam.model.text.LetterCollection;
import org.jocl.*;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static org.jocl.CL.*;
import static org.jocl.CL.clCreateContext;
import static org.jocl.CL.clGetDeviceIDs;

/**
 * Created by Florian on 20.11.2015.
 */
public class RegionOfInterestCLCalculator {
    ArrayList<RegionOfInterestImage> roiImages;
    ArrayList<Letter> letters;
    public final int ITERATION_SIZE = 10;

    Mat[] mat_roiImages;
    Mat[] mat_letters;
    ArrayList<CalculationResult>[][] calculations;

    ICollageController controller;



    // opencl
    private static boolean ableforCL = false;
    private static final int platformIndex = 0;
    private static final long deviceType = CL_DEVICE_TYPE_ALL;
    private static final int deviceIndex = 0;

    private static cl_context context;
    private static cl_device_id device;

    private static cl_kernel intersectionKernel;
    private static cl_command_queue command_queue;
    private cl_mem letter_data_mem;
    private cl_mem letter_index_mem;
    private cl_mem roi_data_mem;
    private cl_mem roi_index_mem;
    private cl_mem calc_res_mem;

    static {
        defaultInitialisation();
        String kernel = loadAndInitKernel();
        if(kernel != "") {
            intersectionKernel  = createKernel(kernel, "intersectionKernel");
        }
    }


    public RegionOfInterestCLCalculator(ArrayList<RegionOfInterestImage> roiImages, ArrayList<Letter> letters, ICollageController controller) {
        this.roiImages = roiImages;
        this.letters = letters;
        this.controller = controller;

        //Retrieve all Mat objects!
        mat_roiImages = new Mat[roiImages.size()];
        for (int i = 0; i < roiImages.size(); i++) {
            mat_roiImages[i] = roiImages.get(i).getCalculationMask();
        }

        mat_letters = new Mat[letters.size()];
        for (int i = 0; i < letters.size(); i++) {
            mat_letters[i] = letters.get(i).getCalculationMask();
        }

        calculations = new ArrayList[mat_roiImages.length][mat_letters.length];
    }

    public float[] calculateIntersection(Mat letter, Mat image) {
        long start = System.currentTimeMillis();
        char letterBuffer[] = new char[letter.width()*letter.height()];
        char imageBuffer[] = new char[image.width()*image.height()];
        float[] calcResults = new float[imageBuffer.length * ITERATION_SIZE];

        // args
        int kernelArgs[] = new int[]{image.width(),image.height(),letter.width(),letter.height()};
        int samplerArgs[] = new int[]{ITERATION_SIZE,0,0,0};
        int letterCenter[] = new int[] {0,0};
        int imageCenter[] = new int[] {0,0};
        long globalWorkSize[] = new long[2];
        globalWorkSize[0] = image.width();
        globalWorkSize[1] = image.height();
        System.out.printf("Starting intersectionkernel with [x: %d, y: %d] -> %d workers\n", globalWorkSize[0], globalWorkSize[1], globalWorkSize[0] * globalWorkSize[1]);


        // get letterdata
        byte[] b = new byte[letterBuffer.length];
        int hit = 0;
        letter.get(0,0, b);
        for(int j = 0; j < b.length; j++) {
            int x = j % letter.width();
            int y = j / letter.width();
            letterBuffer[j] = (char)b[j];
            if(letterBuffer[j] > 0) {
                hit++;
                letterCenter[0] += x;
                letterCenter[1] += y;
            }
        }
        letterCenter[0] /= hit;
        letterCenter[1] /= hit;
        samplerArgs[1] = hit;

        // get imagedata
        b = new byte[imageBuffer.length];
        hit = 0;
        image.get(0,0, b);
        for(int j = 0; j < b.length; j++) {
            int x = j % image.width();
            int y = j / image.width();
            imageBuffer[j] = (char)b[j];
            if(imageBuffer[j] > 0) {
                hit++;
                imageCenter[0] += x;
                imageCenter[1] += y;
            }
        }
        if(hit == 0) {
            System.out.println("image has no rois!");
            return calcResults;
        }
        imageCenter[0] /= hit;
        imageCenter[1] /= hit;
        samplerArgs[2] = hit;

        // letterbuffer
        letter_data_mem = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR ,letterBuffer.length * Sizeof.cl_char, Pointer.to(letterBuffer), null);
        // imagebuffer
        roi_data_mem = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_USE_HOST_PTR ,imageBuffer.length * Sizeof.cl_char, Pointer.to(imageBuffer), null);

        // result binding
        calc_res_mem = clCreateBuffer(context, CL_MEM_WRITE_ONLY, calcResults.length * Sizeof.cl_float, null, null);

        clSetKernelArg(intersectionKernel, 0, Sizeof.cl_mem, Pointer.to(letter_data_mem));
        clSetKernelArg(intersectionKernel, 1, Sizeof.cl_mem, Pointer.to(roi_data_mem));
        clSetKernelArg(intersectionKernel, 2, Sizeof.cl_mem, Pointer.to(calc_res_mem));
        clSetKernelArg(intersectionKernel, 3, Sizeof.cl_long2, Pointer.to(globalWorkSize));
        clSetKernelArg(intersectionKernel, 4, Sizeof.cl_int4, Pointer.to(kernelArgs));
        clSetKernelArg(intersectionKernel, 5, Sizeof.cl_int4, Pointer.to(samplerArgs));
        clSetKernelArg(intersectionKernel, 6, Sizeof.cl_int2, Pointer.to(letterCenter));
        clSetKernelArg(intersectionKernel, 7, Sizeof.cl_int2, Pointer.to(imageCenter));


        clEnqueueNDRangeKernel(command_queue, intersectionKernel, 2, null, globalWorkSize, null, 0, null, null);
        clEnqueueReadBuffer(command_queue, calc_res_mem, CL_TRUE, 0, calcResults.length *Sizeof.cl_float, Pointer.to(calcResults), 0, null,null);
        int bla = 0;
        for(int i = 0; i < calcResults.length; i++) {
            if(calcResults[i] == 0) {
                System.out.println("Ende "+ i);
                break;
            }
        }

        System.out.println("[calculateIntersection] Kernel Computationtime: " + (System.currentTimeMillis() - start) + " ms with " + calcResults.length + " results");
        return calcResults;
    }


    public void calculateIntersectionMatrix(){
        long start = System.currentTimeMillis();
        int possibilities = 0;
        float[] calcResults = calculateIntersection(mat_letters[0], mat_roiImages[0]);

        System.out.println("[calculateIntersectionMatrix] Time: " + (System.currentTimeMillis() - start) + " ms   Possibilites:"+ possibilities);
    }


    //return the overlapped roi area in percentage/100
    public CalculationResult calculateIntersection(Mat mat_roiImage, Mat mat_letter, double maxAspectRatio, double scaleFactor, int dY, int dX){
        double percentage = 0;
        
        //TODO FUCKED UP
        return null;//new CalculationResult(scaleFactor, dX, dY, percentage);
    }

    public CalculationResult getBestResultsForImageLeter(int image, int letter){
        CalculationResult crRet = CalculationResult.getZero();
        for (CalculationResult cr : calculations[image][letter]){
            if(crRet.getIntersectAreaPercentage() < cr.getIntersectAreaPercentage()){
                crRet = cr;
            }
        }
        return crRet;
    }

    public Mat[] getMat_letters() {
        return mat_letters;
    }

    public Mat[] getMat_roiImages() {
        return mat_roiImages;
    }

    private static void defaultInitialisation() {
        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Check if the platform supports OpenCL 1.2
        long sizeArray[] = { 0 };
        clGetPlatformInfo(platform, CL_PLATFORM_VERSION, 0, null, sizeArray);
        byte buffer[] = new byte[(int)sizeArray[0]];
        clGetPlatformInfo(platform, CL_PLATFORM_VERSION,
                buffer.length, Pointer.to(buffer), null);
        String versionString = new String(buffer, 0, buffer.length-1);
        System.out.println("Platform version: "+versionString);
        String versionNumberString = versionString.substring(7, 10);
        try
        {
            String majorString = versionNumberString.substring(0, 1);
            String minorString = versionNumberString.substring(2, 3);
            int major = Integer.parseInt(majorString);
            int minor = Integer.parseInt(minorString);
            if (major == 1 && minor < 2)
            {
                System.err.println(
                        "Platform only supports OpenCL "+versionNumberString);
                ableforCL = false;
            }
        }
        catch (NumberFormatException e)
        {
            System.err.println(
                    "Invalid version number: "+versionNumberString);
            ableforCL = false;
        }

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        device = devices[deviceIndex];

        // Create a context for the selected device
        context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);
        command_queue = clCreateCommandQueue(context, device, 0, null);
        ableforCL = true;
    }

    private static String loadAndInitKernel() {
        URL res = RegionOfInterestCLCalculator.class.getResource("/kernel/intersection_kernel.cl");
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(res.openStream()));
            StringBuffer sb = new StringBuffer();
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine+"\n");
            in.close();
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static cl_kernel createKernel(String str_kernel, String name) {
        cl_program clProgram = null;
        cl_kernel clKernel = null;
        try {
            clProgram = clCreateProgramWithSource(context,1, new String[]{str_kernel}, null,null);
            clBuildProgram(clProgram, 0, null, "-cl-kernel-arg-info", null, null);
            clKernel = clCreateKernel(clProgram, name, null);
            clReleaseProgram(clProgram);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return clKernel;
    }
}
