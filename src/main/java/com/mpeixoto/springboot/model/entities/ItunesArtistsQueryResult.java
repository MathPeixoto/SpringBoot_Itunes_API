package com.mpeixoto.springboot.model.entities;

import java.util.List;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItunesArtistsQueryResult {
  private Integer resultCount;
  private List<Artist> results;
}
