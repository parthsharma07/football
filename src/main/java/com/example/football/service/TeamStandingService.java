package com.example.football.service;

import com.example.football.constants.LeagueIdConstants;
import com.example.football.model.StandingRequest;
import com.example.football.model.StandingResponse;
import com.example.football.model.TeamEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.SECONDS;

@Service
public class TeamStandingService {

    private static final Logger logger = LoggerFactory.getLogger(TeamStandingService.class);

    public static boolean isApiOnline = true;

    public static HashMap<String, TeamEntity> teamEntityHashMap = new HashMap<>();

    @Value("${api.base.url}")
    private String apiUrl;

    @Value("${api.key}")
    private String apiKey;

    public void setTeamEntityHashMap() {
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<TeamEntity>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<TeamEntity>> responseEntity1 = restTemplate.exchange(apiUrl + LeagueIdConstants.PREMIER_LEAGUE + apiKey, HttpMethod.GET, null, responseType);
        List<TeamEntity> teamEntities1 = responseEntity1.getBody();
        ResponseEntity<List<TeamEntity>> responseEntity2 = restTemplate.exchange(apiUrl + LeagueIdConstants.NON_LEAGUE_PREMIER + apiKey, HttpMethod.GET, null, responseType);
        List<TeamEntity> teamEntities2 = responseEntity2.getBody();
        assert teamEntities1 != null;
        for(TeamEntity teamEntity: teamEntities1){
            teamEntityHashMap.put(teamEntity.getTeam_name(), teamEntity);
        }
        assert teamEntities2 != null;
        for(TeamEntity teamEntity: teamEntities2){
            teamEntityHashMap.put(teamEntity.getTeam_name(), teamEntity);
        }
    }

    public StandingResponse getStandingResponse(StandingRequest standingRequest) throws Exception{

        StandingResponse standingResponse = null;
        try{
            if(isApiOnline){
                RestTemplate restTemplate = new RestTemplate();
                Object returnModel;
                String leagueId = standingRequest.getLeagueName().equals("Premier League") ? LeagueIdConstants.PREMIER_LEAGUE : (standingRequest.getLeagueName().equals("Non League Premier") ? LeagueIdConstants.NON_LEAGUE_PREMIER : "");
                ParameterizedTypeReference<List<TeamEntity>> responseType = new ParameterizedTypeReference<>() {};
                ResponseEntity<List<TeamEntity>> responseEntity = restTemplate.exchange(apiUrl + leagueId + apiKey, HttpMethod.GET, null, responseType);
                List<TeamEntity> teamEntities = responseEntity.getBody();
                for(TeamEntity teamEntity: teamEntities){
                    if(teamEntity.getTeam_name().equals(standingRequest.getTeamName()) && teamEntity.getCountry_name().equals(standingRequest.getCountryName()) && teamEntity.getLeague_name().equals(standingRequest.getLeagueName())){
                        standingResponse = setStandingResponse(teamEntity);
                        logger.info("StandingResponse from 3rd party api: {}", standingResponse);
                        break;
                    }
                }
            }else {
                TeamEntity teamEntity = teamEntityHashMap.get(standingRequest.getTeamName());
                if(teamEntity.getTeam_name().equals(standingRequest.getTeamName()) && teamEntity.getCountry_name().equals(standingRequest.getCountryName()) && teamEntity.getLeague_name().equals(standingRequest.getLeagueName())){
                    standingResponse = setStandingResponse(teamEntity);
                    logger.info("StandingResponse from offline support hashmap: {}", standingResponse);
                }
            }
        }catch (Exception e){
            throw e;
        }
        if(Objects.isNull(standingResponse)){
            throw new Exception("Wrong details passed. Please check the details and try again.");
        }
        return standingResponse;
    }

    private StandingResponse setStandingResponse(TeamEntity teamEntity){
        StandingResponse standingResponse = new StandingResponse();
        standingResponse.setCountry_name(teamEntity.getCountry_name());
        standingResponse.setCountry_id("44");
        standingResponse.setLeague_name(teamEntity.getLeague_name());
        standingResponse.setLeague_id(teamEntity.getLeague_id());
        standingResponse.setTeam_name(teamEntity.getTeam_name());
        standingResponse.setTeam_id(teamEntity.getTeam_id());
        standingResponse.setOverall_league_position(teamEntity.getOverall_league_position());
        standingResponse.setHome_league_position(teamEntity.getHome_league_position());
        standingResponse.setAway_league_position(teamEntity.getAway_league_position());
        return standingResponse;
    }

    public List<TeamEntity> getAllStandings(String leagueName) throws Exception{
        List<TeamEntity> teamEntities = null;
        String leagueId = leagueName.equals("Premier League") ? LeagueIdConstants.PREMIER_LEAGUE : (leagueName.equals("Non League Premier") ? LeagueIdConstants.NON_LEAGUE_PREMIER : "");
        try{
            RestTemplate restTemplate = new RestTemplate();
            ParameterizedTypeReference<List<TeamEntity>> responseType = new ParameterizedTypeReference<>() {};
            ResponseEntity<List<TeamEntity>> responseEntity = restTemplate.exchange(apiUrl + leagueId + apiKey, HttpMethod.GET, null, responseType);
            teamEntities = responseEntity.getBody();
        }catch (Exception e){
            throw e;
        }
        return teamEntities;
    }

}
