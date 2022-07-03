package com.mpeixoto.springboot.model.entities.domain;

import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The entity class.
 *
 * @author mpeixoto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Term {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true)
  private String term;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<Artist> artists;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Term)) {
      return false;
    }
    Term term1 = (Term) o;
    return Objects.equals(getTerm(), term1.getTerm())
        && Objects.equals(getArtists(), term1.getArtists());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTerm(), getArtists());
  }
}
