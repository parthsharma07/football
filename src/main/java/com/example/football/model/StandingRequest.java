package com.example.football.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class StandingRequest {

    private String countryName;
    private String leagueName;
    private String teamName;

}
