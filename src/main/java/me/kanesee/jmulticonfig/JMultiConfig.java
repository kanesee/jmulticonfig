package me.kanesee.jmulticonfig;

import java.net.URL;
import java.util.List;
import java.util.Properties;

public class JMultiConfig {
  
  // jmulticonfig.*.properties
  public static final String CONFIG_PATTERN_STR = ".*jmulticonfig\\.([0-9]+)\\.properties$";

  private static Properties s_props = null;

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
