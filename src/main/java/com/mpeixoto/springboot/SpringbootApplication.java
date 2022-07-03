package com.mpeixoto.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * The main class.
 *
 * @author mpeixoto
 */
@SpringBootApplication
@EnableConfigurationProperties({ConfigProper.class})
public class SpringbootApplication {

  /**
   * The entry point.
   *
   * @param args Type: Array os strings
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringbootApplication.class, args);
  }

  /**
   * provide a default instance of RestTemplate.
   *
   * @return RestTemplate
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /**
   * provide a default instance of MappingJackson2HttpMessageConverter.
   *
   * @return MappingJackson2HttpMessageConverter
   */
  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
    return new MappingJackson2HttpMessageConverter();
  }

  /**
   * provide a default instance of HttpHeaders.
   *
   * @return HttpHeaders
   */
  @Bean
  public HttpHeaders httpHeaders() {
    return new HttpHeaders();
  }
}
