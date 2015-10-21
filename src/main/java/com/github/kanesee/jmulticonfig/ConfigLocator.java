package com.github.kanesee.jmulticonfig;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;

public class ConfigLocator {

  public List<URL> locate() {
    List<URL> configs = new ArrayList<URL>();
    
    List<URL> resources = CPScanner.scanResources(
        new ResourceFilter().archiveName("*"));
    for (URL resource : resources) {
        if (resource.getFile().matches(JMultiConfig.CONFIG_PATTERN_STR)) {
//            System.out.println("*** -> " + resource);
          configs.add(resource);
        }
    }      

    return configs;
  }

}