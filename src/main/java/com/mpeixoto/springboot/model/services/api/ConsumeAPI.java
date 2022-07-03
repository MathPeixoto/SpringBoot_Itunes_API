package com.mpeixoto.springboot.model.services.api;

import com.mpeixoto.springboot.exception.ApiException;
import java.util.Collections;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * Service class.
 *
 * @author mpeixoto
 */
@Service
public class ConsumeAPI {
  private final RestTemplate restTemplate;
  private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

  /**
   * Constructor responsible for setting the beans.
   *
   * @param restTemplate Type: RestTemplate
   * @param mappingJackson2HttpMessageConverter Type: MappingJackson2HttpMessageConverter
   */
  public ConsumeAPI(
      RestTemplate restTemplate,
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
    this.restTemplate = restTemplate;
    this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
  }

  /**
   * Method responsible for consume an API and return a ResponseEntity of any type.
   *
   * @param url The uri of the API location
   * @param aClass The parameter that will indicate to the exchange method which mapping needs to be
   *     done when receiving a response
   * @param <T> The type of the class
   * @return A ResponseEntity of any required type
   * @throws ApiException It is thrown in case that wasn't possible to consume the required API
   */
  <T> ResponseEntity<T> getEntityFromAPI(String url, Class<T> aClass) throws ApiException {

    try {
      mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
          Collections.singletonList(MediaType.ALL));
      restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
      return restTemplate.exchange(url, HttpMethod.GET, null, aClass);
    } catch (HttpStatusCodeException exp) {
      if (exp.getStatusCode().is4xxClientError() || exp.getStatusCode().is5xxServerError()) {
        throw new ApiException(
            "An error has occurred while trying to consume the required API", exp);
      }
      throw exp;
    }
  }
}
