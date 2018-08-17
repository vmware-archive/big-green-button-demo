package com.pivotal.demo.FussballService.repo;

import com.pivotal.demo.FussballService.model.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;

@RepositoryRestResource(collectionResourceRel="matches",path="matches")
public interface MatchRepository extends PagingAndSortingRepository<Match,Long> {
    Page findAll(Pageable p);
    Page findAllByDateBetween(@Param("start") Date start, @Param("end") Date end, Pageable p);
    @Query("select m from Match m where friendly=false and year(m.date) = ?1")
    Page findAllByYearOrderByDateDesc(@Param("year") Integer year, Pageable p);
    @Query("select m from Match m where year(m.date) = ?1")
    Page findAllByYearIncludingFriendlyOrderByDateDesc(@Param("year") Integer year, Pageable p);
}
