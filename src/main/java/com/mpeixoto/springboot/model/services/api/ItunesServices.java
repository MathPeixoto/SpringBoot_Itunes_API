package com.mpeixoto.springboot.model.services.api;

import com.mpeixoto.springboot.ConfigProper;
import com.mpeixoto.springboot.exception.ApiException;
import com.mpeixoto.springboot.map.JsonToEntity;
import com.mpeixoto.springboot.model.entities.domain.Album;
import com.mpeixoto.springboot.model.entities.domain.Artist;
import com.mpeixoto.springboot.model.entities.domain.Term;
import com.mpeixoto.springboot.model.entities.json.ArtistsFromItunes;
import com.mpeixoto.springboot.model.entities.json.ItunesArtistsQueryResult;
import com.mpeixoto.springboot.model.services.repository.AlbumRepository;
import com.mpeixoto.springboot.model.services.repository.ArtistsRepository;
import com.mpeixoto.springboot.model.services.repository.TermRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service class.
 *
 * @author mpeixoto
 */
@Service
public class ItunesServices {
  private final ConsumeAPI consumeAPI;
  private final TermRepository termRepository;
  private final ArtistsRepository artistsRepository;
  private final AlbumRepository albumRepository;
  private final ConfigProper configProper;
  private Artist artistDatabase = new Artist();

  /**
   * Constructor responsible for setting the beans.
   *
   * @param consumeAPI Type: ConsumeAPI
   * @param termRepository Type: TermRepository
   * @param artistsRepository Type: ArtistsRepository
   * @param albumRepository Type: AlbumRepository
   * @param configProper Type: ConfigProper
   */
  public ItunesServices(
      ConsumeAPI consumeAPI,
      TermRepository termRepository,
      ArtistsRepository artistsRepository,
      AlbumRepository albumRepository,
      ConfigProper configProper) {
    this.consumeAPI = consumeAPI;
    this.termRepository = termRepository;
    this.artistsRepository = artistsRepository;
    this.albumRepository = albumRepository;
    this.configProper = configProper;
  }

  /**
   * Method responsible for treating the artists names that came from the Itunes API and put it
   * inside a list of String.
   *
   * @param term the term that was searched
   * @return a list of String
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  public List<String> getArtistsNames(String term) throws ApiException {

    Term repositoryByTerm = termRepository.findByTerm(term);

    // if the term does already exist in database
    if (repositoryByTerm != null) {
      return getArtistsFromRepository(repositoryByTerm);
    }
    // if the term doesn't exist in database, then, it will be searched by 'handlerAPI' and will be
    // saved in the database with its correlated artists
    else {
      ItunesArtistsQueryResult itunesArtistsQueryResult =
          handlerAPI(configProper.getUrlItunes() + "search?term=" + term + "&limit=5");
      itunesArtistsQueryResult
          .getResults()
          .removeIf(artistsFromItunes -> artistsFromItunes.getArtistId() == null);
      List<Artist> artistList = JsonToEntity.artistsJsonToArtistDatabase(itunesArtistsQueryResult);
      insertArtistsInRepository(artistList, term);
      return itunesArtistsQueryResult.getResults().stream()
          .map(ArtistsFromItunes::getArtistName)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
  }

  /**
   * Method responsible for treating the artists albums that came from the Itunes API and put it
   * inside a list of String.
   *
   * @param id the id that was searched
   * @return a list of String
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  public List<String> getArtistsAlbums(String id) throws ApiException {

    Artist artist = artistsRepository.findByArtistId(Long.valueOf(id));
    // if the artist exist in  database, then, his albums will be added in a new list of albums
    if (artist != null) {
      return artist.getAlbums().stream()
          .map(Album::getAlbumName)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }

    // if the artist doesn't exist in database, then, he will be searched by 'handlerAPI' and will
    // be saved in database with his albums
    else {
      List<ArtistsFromItunes> results =
          handlerAPI(configProper.getUrlItunes() + "lookup?id=" + id + "&entity=album")
              .getResults();
      insertAlbumsInRepository(results);
      return results.stream()
          .map(ArtistsFromItunes::getCollectionName)
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
  }

  private List<String> getArtistsFromRepository(Term repositoryByTerm) {

    return repositoryByTerm.getArtists().stream()
        .map(Artist::getArtistName)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private void insertArtistsInRepository(List<Artist> artistList, String term) {

    artistList.forEach(
        artist -> {
          Artist byArtistId = artistsRepository.findByArtistId(artist.getArtistId());
          if (byArtistId == null) {
            Term termDatabase = termRepository.findByTerm(term);
            if (termDatabase == null) {
              Term termDomain =
                  Term.builder()
                      .term(term)
                      .artists(new HashSet<>(Collections.singleton(artist)))
                      .build();
              termRepository.save(termDomain);
            } else {
              if (termDatabase.getArtists().isEmpty()) {
                termDatabase.setArtists(new HashSet<>(Collections.singleton(artist)));
                termRepository.save(termDatabase);
              } else {
                termDatabase.getArtists().add(artist);
                termRepository.save(termDatabase);
              }
            }
          } else {
            Term termDomain = Term.builder().term(term).build();
            termDomain.setArtists(new HashSet<>(Collections.singleton(byArtistId)));
            termRepository.save(termDomain);
          }
        });
  }

  private void insertAlbumsInRepository(List<ArtistsFromItunes> results) {

    artistDatabase.setArtistName(results.get(0).getArtistName());
    artistDatabase.setArtistId(results.get(0).getArtistId());

    List<String> albums =
        results.stream()
            .map(ArtistsFromItunes::getCollectionName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    Set<Album> albumSet =
        albums.stream()
            .map(albumsName -> Album.builder().albumName(albumsName).build())
            .collect(Collectors.toSet());

    albumSet.forEach(
        album -> {
          if (albumRepository.findAll().isEmpty()) {
            artistDatabase.setAlbums(new HashSet<>(albumSet));
            artistsRepository.save(artistDatabase);
          } else {
            artistDatabase.getAlbums().addAll(albumSet);
            artistsRepository.save(artistDatabase);
          }
        });
  }

  private ItunesArtistsQueryResult handlerAPI(String url) throws ApiException {
    return consumeAPI.getEntityFromAPI(url, ItunesArtistsQueryResult.class).getBody();
  }

  void setArtistDatabase(Artist artistDatabase) {
    this.artistDatabase = artistDatabase;
  }
}
