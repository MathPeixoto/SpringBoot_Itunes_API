package com.mpeixoto.springboot;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Class responsible for declaring the methods that is used along the project.
 *
 * @author mpeixoto
 */
@ConfigurationProperties("variables")
@Data
public class ConfigProper {
  private String urlItunes;
  private String urlTerm;
  private Long artistId;
  private String term;
  private String id;
  private Long differentId;
  private int errorStatusCode;
}
