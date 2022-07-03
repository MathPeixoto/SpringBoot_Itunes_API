package com.mpeixoto.springboot.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.mpeixoto.springboot.exception.ApiException;
import com.mpeixoto.springboot.model.services.api.ItunesServices;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Class response for testing th MusicController class.
 *
 * @author mpeixoto
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MusicControllerTest {
  @MockBean private ItunesServices listOfArtists;
  @MockBean private HttpHeaders httpHeaders;
  @Autowired private MusicController musicController;

  /**
   * Method responsible for testing if the endpoint is returning a list of strings inside a
   * ResponseEntity that was searched based on a term.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void listOfArtistsGivenATermShouldReturnAListOfArtists() throws ApiException {
    String term = "ocean12";
    when(listOfArtists.getArtistsNames(term)).thenReturn(Arrays.asList("George", "Brad"));
    assertTrue(
        Objects.requireNonNull(musicController.listOfArtists(term).getBody())
            .containsAll(Arrays.asList("George", "Brad")));
    assertThat(musicController.listOfArtists(term).getBody(), hasSize(2));
  }

  /**
   * Method responsible for testing if the endpoint is returning a list of strings inside a
   * ResponseEntity that was searched based on an id.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void listOfAlbumsGivenAnIdShouldReturnAListOfAlbums() throws ApiException {
    String id = "101711723";
    ResponseEntity<List<String>> listResponseEntity =
        new ResponseEntity<>(listOfArtists.getArtistsAlbums(id), httpHeaders, HttpStatus.OK);
    assertThat(musicController.listOfAlbums(id), is(listResponseEntity));
  }
}
