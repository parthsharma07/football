package com.example.football.controller;

import com.example.football.controller.TeamStandingController;
import com.example.football.model.StandingRequest;
import com.example.football.model.StandingResponse;
import com.example.football.model.TeamEntity;
import com.example.football.service.TeamStandingService;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamStandingControllerTest {

    @InjectMocks
    private TeamStandingController teamStandingController;

    @Mock
    private TeamStandingService teamStandingService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllStandingsTest() throws Exception {
        TeamEntity teamEntity1 = new TeamEntity();
        teamEntity1.setTeam_name("Team1");
        TeamEntity teamEntity2 = new TeamEntity();
        teamEntity2.setTeam_name("Team2");

        List<TeamEntity> teamEntities = Arrays.asList(teamEntity1, teamEntity2);

        when(teamStandingService.getAllStandings()).thenReturn(teamEntities);

        List<TeamEntity> result = teamStandingController.getAllStandings();

        assertEquals(teamEntities, result);
    }

    @Test
    public void getStandingResponseTest() throws Exception {
        StandingRequest standingRequest = new StandingRequest();
        standingRequest.setCountryName("countryName");
        standingRequest.setTeamName("teamName");
        standingRequest.setLeagueName("leagueName");
        StandingResponse standingResponse = new StandingResponse();
        standingResponse.setCountry_id("countryId");
        standingResponse.setCountry_name("countryName");
        standingResponse.setLeague_id("leagueId");
        standingResponse.setLeague_name("leagueName");
        standingResponse.setTeam_id("teamId");
        standingResponse.setTeam_name("teamName");
        standingResponse.setOverall_league_position("overallLeaguePosition");
        standingResponse.setHome_league_position("homeLeaguePosition");
        standingResponse.setAway_league_position("awayLeaguePosition");
        when(teamStandingService.getStandingResponse(standingRequest)).thenReturn(standingResponse);
        EntityModel<StandingResponse> result = teamStandingController.getStandingResponse("countryName", "leagueName", "teamName");
        assertEquals(standingResponse, result.getContent());
    }

    @Test
    public void toggleOnlineOfflineTest() {
        TeamStandingService.isApiOnline = false;
        String result = teamStandingController.toggleOnlineOffline();
        assertEquals("API is online: true", result);
    }
}
