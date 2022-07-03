package com.mpeixoto.springboot.model.services.api;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.mpeixoto.springboot.ConfigProper;
import com.mpeixoto.springboot.exception.ApiException;
import com.mpeixoto.springboot.model.entities.domain.Album;
import com.mpeixoto.springboot.model.entities.domain.Artist;
import com.mpeixoto.springboot.model.entities.domain.Term;
import com.mpeixoto.springboot.model.entities.json.ArtistsFromItunes;
import com.mpeixoto.springboot.model.entities.json.ItunesArtistsQueryResult;
import com.mpeixoto.springboot.model.services.repository.AlbumRepository;
import com.mpeixoto.springboot.model.services.repository.ArtistsRepository;
import com.mpeixoto.springboot.model.services.repository.TermRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Class response for testing th ItunesServices class.
 *
 * @author mpeixoto
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ItunesServicesTest {
  private static Long ARTIST_ID;
  private static String TERM;
  private static String ID;
  private Artist artist;
  private Album album;
  @MockBean private ConsumeAPI consumeAPI;
  @MockBean private ResponseEntity<ItunesArtistsQueryResult> itunesResponseEntity;
  @MockBean private ItunesArtistsQueryResult itunes;
  @MockBean private TermRepository termRepository;
  @MockBean private ArtistsRepository artistsRepository;
  @MockBean private AlbumRepository albumRepository;
  @MockBean private List<Album> albumList;
  @Autowired private ItunesServices itunesServices;
  @Autowired private ConfigProper configProper;

  /**
   * Method responsible for instantiate a new List<Artists> and mocking commons behaviours before
   * each method test.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Before
  public void setUp() throws ApiException {
    ARTIST_ID = configProper.getArtistId();
    ID = configProper.getId();
    TERM = configProper.getTerm();
    String urlItunes = configProper.getUrlItunes();
    String urlArtistName = urlItunes + "search?term=" + TERM + "&limit=5";
    String urlAlbum = urlItunes + "lookup?id=" + ID + "&entity=album";

    album = Album.builder().id(configProper.getDifferentId()).albumName("test album").build();

    artist =
        Artist.builder()
            .id(configProper.getDifferentId())
            .artistId(ARTIST_ID)
            .artistName("test name")
            .albums(new HashSet<>(Collections.singleton(album)))
            .build();

    List<ArtistsFromItunes> artists =
        Collections.singletonList(
            ArtistsFromItunes.builder()
                .artistId(ARTIST_ID)
                .artistName("test name")
                .collectionName("test album")
                .build());

    when(consumeAPI.getEntityFromAPI(urlArtistName, ItunesArtistsQueryResult.class))
        .thenReturn(itunesResponseEntity);
    when(consumeAPI.getEntityFromAPI(urlAlbum, ItunesArtistsQueryResult.class))
        .thenReturn(itunesResponseEntity);
    when(itunesResponseEntity.getBody()).thenReturn(itunes);
    when(itunes.getResults()).thenReturn(artists);
  }

  /**
   * Method responsible for testing if getArtistsNames method is returning a correct List<String>
   * given a term as the parameter that exist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getArtistsNamesGivenATermThatExistInDatabaseShouldReturnAListOfArtists()
      throws ApiException {
    Term term =
        Term.builder()
            .id(configProper.getDifferentId())
            .term("test")
            .artists(new HashSet<>(Collections.singleton(artist)))
            .build();
    when(termRepository.findByTerm(TERM)).thenReturn(term);

    List<String> getArtistName = Collections.singletonList(artist.getArtistName());
    assertThat(itunesServices.getArtistsNames(TERM), is(getArtistName));
  }

  /**
   * Method responsible for testing if getArtistsNames method is returning a correct List<String>
   * given a term as the parameter that doesn't exist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getArtistsNamesGivenATermThatDoesNotExistInDatabaseShouldReturnAListOfArtists()
      throws ApiException {
    ArtistsFromItunes artistsFromItunes =
        ArtistsFromItunes.builder()
            .artistName(artist.getArtistName())
            .artistId(artist.getArtistId())
            .collectionName(album.getAlbumName())
            .build();
    List<ArtistsFromItunes> artistsFromItunesList = new ArrayList<>();
    artistsFromItunesList.add(artistsFromItunes);

    when(termRepository.findByTerm(TERM)).thenReturn(null);
    when(itunes.getResults()).thenReturn(artistsFromItunesList);
    when(artistsRepository.findByArtistId(artist.getArtistId())).thenReturn(null);

    List<String> getArtistName = Collections.singletonList(artist.getArtistName());
    assertThat(itunesServices.getArtistsNames(TERM), is(getArtistName));
  }

  /**
   * Method responsible for testing if getArtistsNames method is returning a correct List<String>
   * given a term as the parameter that doesn't exist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void
      getArtistsNamesGivenATermThatExistWithNoArtistsAndTheArtistDoesNotInDatabaseShouldReturnAListOfArtists()
          throws ApiException {
    ArtistsFromItunes artistsFromItunes =
        ArtistsFromItunes.builder()
            .artistName(artist.getArtistName())
            .artistId(artist.getArtistId())
            .collectionName(album.getAlbumName())
            .build();
    List<ArtistsFromItunes> artistsFromItunesList = new ArrayList<>();
    artistsFromItunesList.add(artistsFromItunes);
    Term term =
        Term.builder()
            .id(configProper.getDifferentId())
            .term("test")
            .artists(new HashSet<>())
            .build();

    when(termRepository.findByTerm(TERM)).thenReturn(null, term);
    when(itunes.getResults()).thenReturn(artistsFromItunesList);
    when(artistsRepository.findByArtistId(artist.getArtistId())).thenReturn(null);

    List<String> getArtistName = Collections.singletonList(artist.getArtistName());
    assertThat(itunesServices.getArtistsNames(TERM), is(getArtistName));
  }

  /**
   * Method responsible for testing if getArtistsNames method is returning a correct List<String>
   * given a term as the parameter that doesn't exist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void
      getArtistsNamesGivenATermThatExistWithArtistsAndTheArtistDoesNotInDatabaseShouldReturnAListOfArtists()
          throws ApiException {
    ArtistsFromItunes artistsFromItunes =
        ArtistsFromItunes.builder()
            .artistName(artist.getArtistName())
            .artistId(artist.getArtistId())
            .collectionName(album.getAlbumName())
            .build();
    List<ArtistsFromItunes> artistsFromItunesList = new ArrayList<>();
    artistsFromItunesList.add(artistsFromItunes);
    Term term =
        Term.builder()
            .id(configProper.getDifferentId())
            .term("test")
            .artists(new HashSet<>(Collections.singleton(artist)))
            .build();

    when(termRepository.findByTerm(TERM)).thenReturn(null, term);
    when(itunes.getResults()).thenReturn(artistsFromItunesList);
    when(artistsRepository.findByArtistId(artist.getArtistId())).thenReturn(null);

    List<String> getArtistName = Collections.singletonList(artist.getArtistName());
    assertThat(itunesServices.getArtistsNames(TERM), is(getArtistName));
  }

  /**
   * Method responsible for testing if getArtistsNames method is returning a correct List<String>
   * given a term as the parameter that doesn't exist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void
      getArtistsNamesGivenATermThatDoesNotExistAndAnArtistThatExistInDatabaseShouldReturnAListOfArtists()
          throws ApiException {
    ArtistsFromItunes artistsFromItunes =
        ArtistsFromItunes.builder()
            .artistName(artist.getArtistName())
            .artistId(artist.getArtistId())
            .collectionName(album.getAlbumName())
            .build();
    List<ArtistsFromItunes> artistsFromItunesList = new ArrayList<>();
    artistsFromItunesList.add(artistsFromItunes);

    when(termRepository.findByTerm(TERM)).thenReturn(null);
    when(itunes.getResults()).thenReturn(artistsFromItunesList);
    when(artistsRepository.findByArtistId(ARTIST_ID)).thenReturn(artist);

    List<String> getArtistName = Collections.singletonList(artist.getArtistName());
    assertThat(itunesServices.getArtistsNames(TERM), is(getArtistName));
  }

  /**
   * Method responsible for testing if getArtistsAlbums method is returning a correct List<String>
   * given a term as the parameter.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getArtistsAlbumsGivenAnIdAndAnArtistThatExistShouldReturnAListOfAlbums()
      throws ApiException {

    when(artistsRepository.findByArtistId(Long.valueOf(ID))).thenReturn(artist);
    List<String> getArtistsAlbums =
        artist.getAlbums().stream().map(Album::getAlbumName).collect(Collectors.toList());
    assertThat(itunesServices.getArtistsAlbums(ID), is(getArtistsAlbums));
  }

  /**
   * Method responsible for testing if getArtistsAlbums method is returning a correct List<String>
   * given a term as the parameter and there aren't albums related with the artist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getArtistsAlbumsGivenAnIdAndAnArtistThatDoesNotExistShouldReturnAListOfAlbums()
      throws ApiException {
    when(artistsRepository.findByArtistId(Long.valueOf(ID))).thenReturn(null);
    when(albumRepository.findAll()).thenReturn(albumList);
    when(albumList.isEmpty()).thenReturn(true);

    List<String> getArtistsAlbums =
        artist.getAlbums().stream().map(Album::getAlbumName).collect(Collectors.toList());
    assertThat(itunesServices.getArtistsAlbums(ID), is(getArtistsAlbums));
  }

  /**
   * Method responsible for testing if getArtistsAlbums method is returning a correct List<String>
   * given a term as the parameter and there are albums related with the artist in database.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getArtistsAlbumsGivenAnIdAndAnArtistThatDoesExistShouldReturnAListOfAlbums()
      throws ApiException {
    itunesServices.setArtistDatabase(artist);

    when(artistsRepository.findByArtistId(Long.valueOf(ID))).thenReturn(null);
    when(albumRepository.findAll()).thenReturn(albumList);
    when(albumList.isEmpty()).thenReturn(false);

    List<String> getArtistsAlbums =
        artist.getAlbums().stream().map(Album::getAlbumName).collect(Collectors.toList());
    assertThat(itunesServices.getArtistsAlbums(ID), is(getArtistsAlbums));
  }
}
