package com.mpeixoto.springboot.model.entities.json;

import java.util.List;
import java.util.Objects;
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
  private List<ArtistsFromItunes> results;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ItunesArtistsQueryResult)) {
      return false;
    }
    ItunesArtistsQueryResult that = (ItunesArtistsQueryResult) o;
    return Objects.equals(getResultCount(), that.getResultCount())
        && Objects.equals(getResults(), that.getResults());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getResultCount(), getResults());
  }
}
