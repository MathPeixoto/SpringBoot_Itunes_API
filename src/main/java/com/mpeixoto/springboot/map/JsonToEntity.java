package com.mpeixoto.springboot.map;

import com.mpeixoto.springboot.model.entities.domain.Album;
import com.mpeixoto.springboot.model.entities.domain.Artist;
import com.mpeixoto.springboot.model.entities.json.ArtistsFromItunes;
import com.mpeixoto.springboot.model.entities.json.ItunesArtistsQueryResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class responsible for transform an object that comes from Itunes API into an object that will be
 * saved in database.
 *
 * @author mpeixoto
 */
public class JsonToEntity {

  /**
   * The method responsible for returning a list of artists that can be saved in database.
   *
   * @param itunesArtistsQueryResult The object that comes from ItunesAPI
   * @return A list of artists
   */
  public static List<Artist> artistsJsonToArtistDatabase(
      ItunesArtistsQueryResult itunesArtistsQueryResult) {

    Map<String, Set<Album>> artistsAlbums = new HashMap<>();
    List<Artist> artistList = new ArrayList<>();

    itunesArtistsQueryResult
        .getResults()
        .forEach(
            artistsFromItunes -> {
              String name = artistsFromItunes.getArtistName();
              Album album =
                  Album.builder().albumName(artistsFromItunes.getCollectionName()).build();

              if (!artistsAlbums.containsKey(name)) {
                artistsAlbums.put(name, new HashSet<>(Collections.singletonList(album)));
              } else {
                artistsAlbums.get(name).add(album);
              }
            });

    artistsAlbums.forEach(
        (name, albumDatabases) -> {
          Artist artist = new Artist();
          Stream<ArtistsFromItunes> artistsJsonFiltered =
              itunesArtistsQueryResult.getResults().stream()
                  .filter(artistsFromItunes -> artistsFromItunes.getArtistName().equals(name));

          artist.setArtistName(name);
          artist.setAlbums(albumDatabases);
          artist.setArtistId(artistsJsonFiltered.collect(Collectors.toList()).get(0).getArtistId());
          artistList.add(artist);
        });

    return artistList;
  }
}
