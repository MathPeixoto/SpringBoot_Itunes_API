package com.mpeixoto.springboot.model.entities.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The entity class.
 *
 * @author mpeixoto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Album {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  private String albumName;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Album)) {
      return false;
    }
    Album album = (Album) o;
    return Objects.equals(getAlbumName(), album.getAlbumName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getAlbumName());
  }
}
