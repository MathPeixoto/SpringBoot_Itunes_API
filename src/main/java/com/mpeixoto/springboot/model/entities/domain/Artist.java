package com.mpeixoto.springboot.model.entities.domain;

import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class Artist {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true)
  private Long artistId;

  private String artistName;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Album> albums;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Artist)) {
      return false;
    }
    Artist artist = (Artist) o;
    return Objects.equals(getArtistId(), artist.getArtistId())
        && Objects.equals(getArtistName(), artist.getArtistName())
        && Objects.equals(getAlbums(), artist.getAlbums());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getArtistId(), getArtistName(), getAlbums());
  }
}
