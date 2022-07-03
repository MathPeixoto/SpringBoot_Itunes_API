package com.mpeixoto.springboot.controller;

import com.mpeixoto.springboot.exception.ApiException;
import com.mpeixoto.springboot.model.services.api.ItunesServices;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class controller.
 *
 * @author mpeixoto
 */
@RestController
@RequestMapping("/music")
public class MusicController {
  private final ItunesServices listOfArtists;
  private final HttpHeaders httpHeaders;

  /**
   * Constructor responsible for setting the beans.
   *
   * @param listOfArtists Type: ItunesServices
   * @param httpHeaders Type: HttpHeaders
   */
  public MusicController(ItunesServices listOfArtists, HttpHeaders httpHeaders) {
    this.listOfArtists = listOfArtists;
    this.httpHeaders = httpHeaders;
  }

  /**
   * Endpoint that returns a list of strings inside a ResponseEntity that contains five names that
   * was searched based on a term.
   *
   * @param term The query parameter that is used inside a searching
   * @return A ResponseEntity that contains the list of artists' name, a header and the HttpStatus
   *     ok
   * @throws ApiException Exception that is thrown in case it wasn't possible to consume the Itunes
   *     API
   */
  @GetMapping("/artists")
  public ResponseEntity<List<String>> listOfArtists(@RequestParam("term") String term)
      throws ApiException {
    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    List<String> artistsNamesWithoutDuplicates =
        listOfArtists.getArtistsNames(term).stream().distinct().collect(Collectors.toList());
    return new ResponseEntity<>(artistsNamesWithoutDuplicates, httpHeaders, HttpStatus.OK);
  }

  /**
   * Endpoint that returns a list of strings inside a ResponseEntity that contains the albums that
   * was searched based on a id.
   *
   * @param id The path parameter that is used inside a searching
   * @return A ResponseEntity that contains the list of artists' albums, a header and the HttpStatus
   *     ok
   * @throws ApiException Exception that is thrown in case it wasn't possible to consume the Itunes
   *     API
   */
  @GetMapping("/artists/{id}/albums")
  public ResponseEntity<List<String>> listOfAlbums(@PathVariable String id) throws ApiException {
    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    return new ResponseEntity<>(listOfArtists.getArtistsAlbums(id), httpHeaders, HttpStatus.OK);
  }
}
