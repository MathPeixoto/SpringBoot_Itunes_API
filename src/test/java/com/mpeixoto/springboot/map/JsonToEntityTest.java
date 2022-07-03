package com.mpeixoto.springboot.map;

import static org.junit.Assert.assertThat;

import com.mpeixoto.springboot.model.entities.domain.Album;
import com.mpeixoto.springboot.model.entities.domain.Artist;
import com.mpeixoto.springboot.model.entities.json.ArtistsFromItunes;
import com.mpeixoto.springboot.model.entities.json.ItunesArtistsQueryResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Class responsible for testing if the JsonToEntity class is working well.
 *
 * @author mpeixoto
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonToEntityTest {

  private static final long ARTIST_ID = 1000L;
  private ItunesArtistsQueryResult artistsQueryResult;
  private Artist artist;

  /** Method responsible for setting the variables before the test. */
  @Before
  public void setUp() {

    Album albumOne = Album.builder().albumName("test album name one").build();
    Album albumTwo = Album.builder().albumName("test album name two").build();

    artist =
        Artist.builder()
            .albums(new HashSet<>(Arrays.asList(albumOne, albumTwo)))
            .artistName("test artist name one")
            .artistId(ARTIST_ID)
            .build();

    ArtistsFromItunes artistFromItunesOne =
        ArtistsFromItunes.builder()
            .artistId(ARTIST_ID)
            .artistName("test artist name one")
            .collectionName("test album name one")
            .build();

    ArtistsFromItunes artistFromItunesTwo =
        ArtistsFromItunes.builder()
            .artistId(ARTIST_ID)
            .artistName("test artist name one")
            .collectionName("test album name two")
            .build();

    artistsQueryResult =
        ItunesArtistsQueryResult.builder()
            .resultCount(1)
            .results(Arrays.asList(artistFromItunesOne, artistFromItunesTwo))
            .build();
  }

  /** Method responsible for testing if the list returned is the expected. */
  @Test
  public void artistsJsonToArtistDatabase() {
    assertThat(
        JsonToEntity.artistsJsonToArtistDatabase(artistsQueryResult),
        Is.is(Collections.singletonList(artist)));
  }
}
