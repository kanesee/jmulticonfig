package com.github.kanesee.jmulticonfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class ConfigLoader {

  public Properties load(List<URL> configs) {
    Properties props = new Properties();
    for(URL config : configs) {
      try {
        InputStream is = config.openStream();
        props.load(is);
      } catch(IOException e) {
        System.err.println("Error reading " + config);
      }
    }
    return props;
  }
}
