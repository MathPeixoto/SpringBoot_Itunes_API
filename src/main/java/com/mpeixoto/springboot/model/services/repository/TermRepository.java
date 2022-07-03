package com.mpeixoto.springboot.model.services.repository;

import com.mpeixoto.springboot.model.entities.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Term repository.
 *
 * @author mpeixoto
 */
public interface TermRepository extends JpaRepository<Term, Long> {

  /**
   * Custom method created to search a Term by his term.
   *
   * @param term Type: String
   * @return A Term
   */
  Term findByTerm(String term);
}
