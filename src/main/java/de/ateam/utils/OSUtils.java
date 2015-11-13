package main.java.de.ateam.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public final class OSUtils {
	public enum OS {
		WINDOWS, MAC_OS_X, UNIX, SOLARIS, LINUX, OTHER;
	}

	private static String OSName = null;
	private static OS OSEnum = null;
	public static String getOSName() {
		getOSHelper();
		return OSName;
	}
	
	public static OS getOSEnum() {
		getOSHelper();
		return OSEnum;
	}
	
	static{
		OSUtils.getOSHelper();
	}
	
	private static void getOSHelper(){
		if (OSName == null || OSEnum == null) {
			OSName = System.getProperty("os.name");
			OSEnum = getOSEnumByOSName(OSName);
		}
	}

	public static OS getOSEnumByOSName(String OSName) {
		String osNameMatch = OSName.toLowerCase();
		if (osNameMatch.contains("linux")) {
			return OS.LINUX;
		} else if (osNameMatch.contains("unix")) {
			return OS.UNIX;
		} else if (osNameMatch.contains("windows")) {
			return OS.WINDOWS;
		} else if (osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
			return OS.SOLARIS;
		} else if (osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
			return OS.MAC_OS_X;
		} else {
			return OS.OTHER;
		}
	}

	public static boolean isWindows() {
		return OSEnum.equals(OS.WINDOWS);
	}

	public static boolean isUnix() {
		return OSEnum.equals(OS.UNIX) | OSEnum.equals(OS.MAC_OS_X) | OSEnum.equals(OS.LINUX) | OSEnum.equals(OS.SOLARIS);
	}
	
	public static boolean isLinux() {
		return OSEnum.equals(OS.LINUX);
	}
	
	public static boolean isMacOSX() {
		return OSEnum.equals(OS.MAC_OS_X);
	}
	
	public static boolean isSolaris() {
		return OSEnum.equals(OS.SOLARIS);
	}

	// TODO implement for Mac/Unix/...?
	public static String preparePathForOS(String path) {
		String tmpPath = path.trim();
		if (isWindows()) {
			while ((tmpPath.startsWith(File.separator) || tmpPath.startsWith("/")) && tmpPath.length() > 0) {
				tmpPath = tmpPath.substring(1);
			}
		}
		if(isUnix()){
			tmpPath = tmpPath.replace("%20", " ");
		}
		return tmpPath;
	}

	public static String getResourcePathForOS(String filename) {
		return preparePathForOS((new OSUtils()).getClass().getClassLoader().getResource(filename).getPath());
	}

}