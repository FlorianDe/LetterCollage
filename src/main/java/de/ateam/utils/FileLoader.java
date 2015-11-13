package main.java.de.ateam.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Florian on 13.11.2015.
 */
public class FileLoader {

    public static final FileLoader instance;
    static {
        instance = new FileLoader();
    }

    //TODO Florian bau mal hier noch den FileLoader ein
    public static File getJarDir(Class aclass) {
        URL url;
        String extURL;
        try {
            url = aclass.getProtectionDomain().getCodeSource().getLocation();
        } catch (SecurityException ex) {
            url = aclass.getResource(aclass.getSimpleName() + ".class");
        }

        extURL = url.toExternalForm();

        if (extURL.endsWith(".jar"))
            extURL = extURL.substring(0, extURL.lastIndexOf("/"));
        else {
            String suffix = "/"+(aclass.getName()).replace(".", "/")+".class";
            extURL = extURL.replace(suffix, "");
            if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
                extURL = extURL.substring(4, extURL.lastIndexOf("/"));
        }

        try {
            url = new URL(extURL);
        } catch (MalformedURLException mux) {}

        try {
            return new File(url.toURI());
        } catch(URISyntaxException ex) {
            return new File(url.getPath());
        }
    }

    //TODO CHECK WHETHER JAR OR NOT!
    public static URL loadFile(String filePath){
        return instance.getClass().getClassLoader().getResource(filePath);
    }
}
