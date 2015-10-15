package me.kanesee.jmulticonfig;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;

public class JMultiConfig {
  
  private static Properties s_props = null;

  public static synchronized Properties getConfig() {
    if( s_props == null ) {
//      List<URL> resources = CPScanner.scanResources(
//          new ResourceFilter().packageName("net.sf.corn.cps.sample").resourceName("*.xml"));
      
      List<URL> resources = CPScanner.scanResources(
          new ResourceFilter()
            .packageName("me.kanesee")
              .resourceName("jmulticonfig.3.properties")
          );
      for(URL resource : resources) {
        System.out.println(resource);
      }
      
    }
    return s_props;
  }
  
  public static void main(String[] args) throws Exception {
    System.out.println(JMultiConfig.class.getClassLoader().getResource("jmulticonfig.1.properties"));
    System.out.println(JMultiConfig.class.getClassLoader().getResource("jmulticonfig.3.properties"));
    
//    File[] files = new ConfigLocator("jmulticonfig.*.properties").getFiles();
//    for(File file : files) {
//      System.out.println(file.getAbsolutePath());
//    }
    
//    Configuration m_jconfig = ConfigurationManager.getConfiguration();
//    System.out.println(m_jconfig.getProperty("topicViewerFileLocation", null, "psi4_social"));
    
    getConfig();
    
  }
}
