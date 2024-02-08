package com.example.football.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class StandingResponse {

    private String country_name;
    private String country_id;
    private String league_name;
    private String league_id;
    private String team_name;
    private String team_id;
    private String overall_league_position;
    private String home_league_position;
    private String away_league_position;

}
