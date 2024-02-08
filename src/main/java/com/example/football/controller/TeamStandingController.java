package com.example.football.controller;

import com.example.football.exception.ApplicationException;
import com.example.football.model.StandingRequest;
import com.example.football.model.StandingResponse;
import com.example.football.model.TeamEntity;
import com.example.football.service.TeamStandingService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/footballStanding")
public class TeamStandingController {

    private static final Logger logger = LoggerFactory.getLogger(TeamStandingController.class);

    @Autowired
    private TeamStandingService teamStandingService;

    @GetMapping(value = "/allStandings")
    @ResponseBody
    public List<TeamEntity> getAllStandings(){
        List<TeamEntity> teamEntities = null;
        try {
            teamEntities = teamStandingService.getAllStandings();
        }catch (Exception e){
            e.printStackTrace();
        }
        return teamEntities;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/getStandings")
    @ResponseBody
    public EntityModel<StandingResponse> getStandingResponse(@RequestParam(name = "countryName") String countryName,
                                                             @RequestParam(name = "leagueName") String leagueName,
                                                             @RequestParam(name = "teamName") String teamName) throws ApplicationException{
        StandingRequest standingRequest = new StandingRequest();
        standingRequest.setCountryName(countryName);
        standingRequest.setLeagueName(leagueName);
        standingRequest.setTeamName(teamName);
        StandingResponse standingResponse = null;
        EntityModel<StandingResponse> resource = null;
        try {
            standingResponse = teamStandingService.getStandingResponse(standingRequest);
            resource = EntityModel.of(standingResponse);
            WebMvcLinkBuilder linkTo= linkTo(methodOn(this.getClass()).getAllStandings());
            resource.add(linkTo.withRel("all-team-standings"));
        }catch (Exception e){
            throw new ApplicationException("400",e.getMessage());
        }
        return resource;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/toggleOnlineOffline")
    @ResponseBody
    public String toggleOnlineOffline(){
        if(TeamStandingService.isApiOnline){
            TeamStandingService.isApiOnline = false;
        }else{
            TeamStandingService.isApiOnline = true;
        }
        return "API is online: " + TeamStandingService.isApiOnline;
    }

    @PostConstruct
    public void setTeamEntityHashMap(){
        teamStandingService.setTeamEntityHashMap();
        logger.info("initializing teamEntityHashMap from 3rd party api.");
    }

}
