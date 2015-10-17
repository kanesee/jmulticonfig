package me.kanesee.jmulticonfig;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigPrioritizer {

  public void prioritize(List<URL> configs) {
    Collections.sort(configs, new Comparator<URL>() {
      public int compare(URL o1, URL o2) {
        return priority(o1) - priority(o2);
      }
    });
  }
  
  private static final Pattern configPtn = Pattern.compile(JMultiConfig.CONFIG_PATTERN_STR);
  private int priority(URL config) {
    int priority = -1;
    Matcher matcher =configPtn.matcher(config.getFile());
    if( matcher.matches() ) {
      try {
        priority = new Integer(matcher.group(1));
      } catch(NumberFormatException e) {
        
      }
    }
    return priority;
  }
}
