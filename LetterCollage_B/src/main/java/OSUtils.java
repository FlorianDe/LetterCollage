package main.java;
import java.io.File;

public final class OSUtils
{
   private static String OS = null;
   public static String getOsName()
   {
      if(OS == null) { OS = System.getProperty("os.name"); }
      return OS;
   }
   public static boolean isWindows()
   {
      return getOsName().startsWith("Windows");
   }

   //Evtl anpassen für Mac ;)!
   public static boolean isUnix(){
	   return !isWindows();
   }
   
   //TODO implement for Mac/Unix?
   public static String preparePathForOS(String path){
	   String tmpPath = path.trim();
	   if(isWindows()){
		   while((tmpPath.startsWith(File.separator) || tmpPath.startsWith("/")) && tmpPath.length() > 0){
			   tmpPath = tmpPath.substring(1);
		   }
	   }
	   return tmpPath;
   }
}