package com.pivotal.demo.FussballService.model.projections;

import com.pivotal.demo.FussballService.model.Match;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

@Projection(name = "matchSummary", types = Match.class)
public interface MatchSummary {
    Date getDate();
    @Value("#{target.homeTeam.name}")
    String getHomeTeamName();
    @Value("#{target.awayTeam.name}")
    String getAwayTeamName();
    @Value("#{target.location.city}")
    String getLocationCity();
    @Value("#{target.location.country}")
    String getLocationCountry();
    @Value("#{target.location.latitude}")
    Float getLocationLat();
    @Value("#{target.location.longitude}")
    Float getLocationLong();
    Integer getHomeScore();
    Integer getAwayScore();
    Boolean getFriendly();
}