package com.mpeixoto.springboot.model.services.api;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.mpeixoto.springboot.ConfigProper;
import com.mpeixoto.springboot.exception.ApiException;
import com.mpeixoto.springboot.model.entities.ItunesArtistsQueryResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Class response for testing th ConsumeAPI class.
 *
 * @author mpeixoto
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ConsumeAPITest {
  private ResponseEntity<ItunesArtistsQueryResult> itunesResponseEntity;
  @MockBean private RestTemplate restTemplate;
  @SpyBean private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
  @Autowired private ConsumeAPI consumeAPI;
  @Autowired private ConfigProper configProper;
  /** Rule that is used to testing the exceptions. */
  @Rule public ExpectedException expectedException = ExpectedException.none();

  /** Method responsible for instantiate a new ResponseEntity before each method test. */
  @Before
  public void setUp() {
    itunesResponseEntity = new ResponseEntity<>(new ItunesArtistsQueryResult(), HttpStatus.OK);
  }

  /**
   * Method responsible for testing if getEntityFromAPI method is returning a correct ResponseEntity
   * given a class as the parameter.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getEntityFromAPIGivenAnUrlShouldReturnAResponseEntity() throws ApiException {
    when(restTemplate.exchange(
            configProper.getUrlTerm(), HttpMethod.GET, null, ItunesArtistsQueryResult.class))
        .thenReturn(itunesResponseEntity);
    assertThat(
        consumeAPI.getEntityFromAPI(configProper.getUrlTerm(), ItunesArtistsQueryResult.class),
        is(itunesResponseEntity));
  }

  /**
   * Method responsible for testing if the method has been failed as expected given a status code
   * 400.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getEntityFromAPIGivenAStatusCode404ShouldReturnApiException() throws ApiException {
    when(restTemplate.exchange(
            configProper.getUrlTerm(), HttpMethod.GET, null, ItunesArtistsQueryResult.class))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
    expectedException.expect(ApiException.class);
    expectedException.expectMessage(
        "An error has occurred while trying to consume the required API");
    consumeAPI.getEntityFromAPI(configProper.getUrlTerm(), ItunesArtistsQueryResult.class);
  }

  /**
   * Method responsible for testing if the method has been failed as expected given a status code
   * 500.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getEntityFromAPIGivenAStatusCode500ShouldReturnApiException() throws ApiException {
    when(restTemplate.exchange(
            configProper.getUrlTerm(), HttpMethod.GET, null, ItunesArtistsQueryResult.class))
        .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
    expectedException.expect(ApiException.class);
    expectedException.expectMessage(
        "An error has occurred while trying to consume the required API");
    consumeAPI.getEntityFromAPI(configProper.getUrlTerm(), ItunesArtistsQueryResult.class);
  }

  /**
   * Method responsible for testing if the method has been failed as expected given a status code
   * 300.
   *
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  @Test
  public void getEntityFromAPIGivenAStatusCode300ShouldReturnHttpStatusCodeException()
      throws ApiException {
    when(restTemplate.exchange(
            configProper.getUrlTerm(), HttpMethod.GET, null, ItunesArtistsQueryResult.class))
        .thenThrow(
            new HttpClientErrorException(HttpStatus.valueOf(configProper.getErrorStatusCode())));
    expectedException.expect(HttpStatusCodeException.class);
    consumeAPI.getEntityFromAPI(configProper.getUrlTerm(), ItunesArtistsQueryResult.class);
  }
}
