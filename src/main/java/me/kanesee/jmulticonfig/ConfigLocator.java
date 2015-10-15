package me.kanesee.jmulticonfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;

import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * Adopted from <a href="http://www.onjava.com/pub/a/onjava/excerpt/jebp_3/index1.html?page=3">Servlet Best Practices, Part 1</a>
 * Augmented with wilcard resource matching.
 * 
 * A class to locate resources, retrieve their contents, and determine their
 * last modified time. To find the resource the class searches the CLASSPATH
 * first, then Resource.class.getResource("/" + name). If the Resource finds
 * a "file:" URL, the file path will be treated as a file. Otherwise, the
 * path is treated as a URL and has limited last modified info.
 * 
 * @author Kane See
 */
public class ConfigLocator implements Serializable {

  private String filenamePattern;
  private File[] files;
  private URL url;

  public ConfigLocator(String filenamePattern) throws IOException {
    this.filenamePattern = filenamePattern;
    SecurityException exception = null;

    try {
      // Search using the CLASSPATH. If found, "file" is set and the call
      // returns true.  A SecurityException might bubble up.
      tryClasspath(filenamePattern);
    }
    catch (SecurityException e) {
      exception = e;  // Save for later.
    }

    try {
      // Search using the classloader getResource(  ). If found as a file,
      // "file" is set; if found as a URL, "url" is set.
      tryLoader(filenamePattern);
    }
    catch (SecurityException e) {
      exception = e;  // Save for later.
    }
    
    if( files != null && files.length > 0 )
      return;

    // If you get here, something went wrong. Report the exception.
    String msg = "";
    if (exception != null) {
      msg = ": " + exception;
    }

    throw new IOException("Resource '" + filenamePattern + "' could not be found in " +
      "the CLASSPATH (" + System.getProperty("java.class.path") +
      "), nor could it be located by the classloader responsible for the " +
      "web application (WEB-INF/classes)" + msg);
  }

  /**
   * Returns the resource name, as passed to the constructor
   */
  public String getName(  ) {
    return filenamePattern;
  }

  /**
   * Returns an input stream to read the resource contents
   */
//  public InputStream getInputStream(  ) throws IOException {
//    if (files != null) {
//      return new BufferedInputStream(new FileInputStream(files));
//    }
//    else if (url != null) {
//      return new BufferedInputStream(url.openStream(  ));
//    }
//    return null;
//  }
  
  public File[] getFiles() { return files; }

  /**
   * Returns when the resource was last modified. If the resource 
   * was found using a URL, this method will work only if the URL 
   * connection supports last modified information. If there's no 
   * support, Long.MAX_VALUE is returned. Perhaps this should return 
   * -1, but you should return MAX_VALUE on the assumption that if
   * you can't determine the time, it's maximally new.
   */
//  public long lastModified(  ) {
//    if (files != null) {
//      return files.lastModified(  );
//    }
//    else if (url != null) {
//      try {
//        return url.openConnection(  ).getLastModified(  );  // Hail Mary
//      }
//      catch (IOException e) { return Long.MAX_VALUE; }
//    }
//    return 0;  // can't happen
//  }
   
  /**
   * Returns the directory containing the resource, or null if the 
   * resource isn't directly available on the filesystem. 
   * This value can be used to locate the configuration file on disk,
   * or to write files in the same directory.
   */
//  public String getDirectory(  ) {
//    if (files != null) {
//      return files.getParent(  );
//    }
//    else if (url != null) {
//      return null;
//    }
//    return null;
//  }
 
  // Returns true if found
  private boolean tryClasspath(String filename) {
    String classpath = System.getProperty("java.class.path");
    String[] paths = split(classpath, File.pathSeparator);
    files = searchDirectories(paths, filename);
    return (files != null);
  }

  private static File[] searchDirectories(String[] paths, String filePattern) {
    SecurityException exception = null;
    for (int i = 0; i < paths.length; i++) {
      try {
//        File file = new File(paths[i], filename);
//        if (file.exists(  ) && !file.isDirectory(  )) {
//          return file;
//        }
        File file = new File(paths[i]);
        if( file.isDirectory()) {
          FileFilter fileFilter = new WildcardFileFilter(filePattern);
          File[] configFiles = file.listFiles(fileFilter);
          if( configFiles != null ) {
            return configFiles;
          }
        }
      }
      catch (SecurityException e) {
        // Security exceptions can usually be ignored, but if all attempts
        // to find the file fail, report the (last) security exception.
        exception = e;
      }
    }
    // Couldn't find any match
    if (exception != null) {
      throw exception;
    }
    else {
      return null;
    }
  }

  // Splits a String into pieces according to a delimiter.
  // Uses JDK 1.1 classes for backward compatibility.
  // JDK 1.4 actually has a split(  ) method now.
  private static String[  ] split(String str, String delim) {
    // Use a Vector to hold the split strings.
    Vector v = new Vector(  );

    // Use a StringTokenizer to do the splitting.
    StringTokenizer tokenizer = new StringTokenizer(str, delim);
    while (tokenizer.hasMoreTokens(  )) {
      v.addElement(tokenizer.nextToken(  ));
    }

    String[  ] ret = new String[v.size(  )];
    v.copyInto(ret);
    return ret;
  }

  // Returns true if found
  private boolean tryLoader(String filenamePattern) {
//    filenamePattern = "/" + filenamePattern;
//    URL res = ConfigLocator.class.getResource(filenamePattern);
//    if (res == null) {
//      return false;
//    }
    List<URL> resources = CPScanner.scanResources(
        new ResourceFilter().resourceName(filenamePattern)
        );
//    List<URL> resources = CPScanner.scanResources(
//        new ResourceFilter().packageName("net.sf.corn.cps.sample").resourceName("*.xml"));

    // Try converting from a URL to a File.
//    File resFile = urlToFile(res);
//    if (resFile != null) {
//      files = resFile;
//    }
//    else {
//      url = res;
//    }
    return true;
  }

  private static File urlToFile(URL res) {
    String externalForm = res.toExternalForm();
    if (externalForm.startsWith("file:")) {
      return new File(externalForm.substring(5));
    }
    return null;
  }

  public String toString(  ) {
    return "[Resource: File: " + files + " URL: " + url + "]";
  }
}