package com.pivotal.demo.FussballService.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.pivotal.demo.FussballService.model.Team;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="teams",path="teams")
public interface TeamRepository extends PagingAndSortingRepository<Team,Long> {
    Page findAll(Pageable p);
}
