package com.mpeixoto.springboot.model.services.repository;

import com.mpeixoto.springboot.model.entities.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Artist repository.
 *
 * @author mpeixoto
 */
public interface ArtistsRepository extends JpaRepository<Artist, Long> {

  /**
   * Custom method created to search an Artist by ArtistId.
   *
   * @param artistId Type: Long
   * @return An Artist
   */
  Artist findByArtistId(Long artistId);
}
