package com.example.football.service;

import com.example.football.model.TeamEntity;
import com.example.football.service.TeamStandingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TeamStandingServiceTest {

    @InjectMocks
    private TeamStandingService teamStandingService;

    @Value("${api.base.url}")
    private String apiUrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Disabled
    @Test
    public void getAllStandingsTest() throws Exception {
        TeamEntity teamEntity1 = new TeamEntity();
        teamEntity1.setTeam_name("Team1");
        TeamEntity teamEntity2 = new TeamEntity();
        teamEntity2.setTeam_name("Team2");
        RestTemplate restTemplate = new RestTemplate();

        List<TeamEntity> teamEntities = Arrays.asList(teamEntity1, teamEntity2);

        ResponseEntity<List<TeamEntity>> responseEntity = ResponseEntity.ok(teamEntities);

        when(restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TeamEntity>>() {}))
                .thenReturn(responseEntity);

        List<TeamEntity> result = teamStandingService.getAllStandings();

        assertEquals(teamEntities, result);
    }
}
