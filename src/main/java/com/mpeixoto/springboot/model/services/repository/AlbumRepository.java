package com.mpeixoto.springboot.model.services.repository;

import com.mpeixoto.springboot.model.entities.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Album repository.
 *
 * @author mpeixoto
 */
public interface AlbumRepository extends JpaRepository<Album, Long> {}
