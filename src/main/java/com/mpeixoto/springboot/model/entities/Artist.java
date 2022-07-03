package com.mpeixoto.springboot.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The POJO class.
 *
 * @author mpeixoto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
class Artist {
  private Long artistId;
  private String artistName;
  private String collectionName;
}
