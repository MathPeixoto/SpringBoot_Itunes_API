package com.mpeixoto.springboot.model.entities.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistsFromItunes {
  private Long artistId;
  private String artistName;
  private String collectionName;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ArtistsFromItunes)) {
      return false;
    }
    ArtistsFromItunes that = (ArtistsFromItunes) o;
    return Objects.equals(getArtistId(), that.getArtistId())
        && Objects.equals(getArtistName(), that.getArtistName())
        && Objects.equals(getCollectionName(), that.getCollectionName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getArtistId(), getArtistName(), getCollectionName());
  }
}
