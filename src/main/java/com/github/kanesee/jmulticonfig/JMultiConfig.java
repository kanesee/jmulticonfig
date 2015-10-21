package com.github.kanesee.jmulticonfig;

import java.net.URL;
import java.util.List;
import java.util.Properties;

/**
 * Main class to get properties
 * Reads all jmulticonfig.*.properties files in the classpath, 
 * prioritizes them, and returns the properties within them.
 * @author ksee
 *
 */
public class JMultiConfig {
  
  // jmulticonfig.*.properties
  public static final String CONFIG_PATTERN_STR = ".*jmulticonfig\\.([0-9]+)\\.properties$";

  private static Properties s_props = null;

  /**
   * Reads all jmulticonfig.*.properties files in the classpath, 
   * prioritizes them, and returns the properties within them.
   * 
   * @return
   */
  public static synchronized Properties getConfig() {
    if( s_props == null ) {
      List<URL> configs = new ConfigLocator().locate();
      new ConfigPrioritizer().prioritize(configs);
      s_props = new ConfigLoader().load(configs);
    }
    return s_props;
  }
  
  public static void main(String[] args) throws Exception {
    Properties props = getConfig();
    for(String key : props.stringPropertyNames()) {
      System.out.println(key+" = "+props.getProperty(key));
    }
  }
}
