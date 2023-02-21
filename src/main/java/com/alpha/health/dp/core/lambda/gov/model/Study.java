package com.alpha.health.dp.core.lambda.gov.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Study {
    @JsonProperty("Rank")
    private Integer rank;

    @JsonProperty("NCTId")
    private List<String> nCTId;

    @JsonProperty("Condition")
    private List<String> conditions;

    @JsonProperty("BriefTitle")
    private List<String> briefTitle;

    @JsonProperty("OfficialTitle")
    private List<String> officialTitle;

    @JsonProperty("LocationCity")
    private List<String> locationCity;

    @JsonProperty("LocationState")
    private List<String> LocationState;

    @JsonProperty("LocationZip")
    private List<String> LocationZip;

    @JsonProperty("LocationCountry")
    private List<String> locationCountry;

    @JsonProperty("LocationStatus")
    private List<String> locationStatus;

    @JsonProperty("OverallStatus")
    private List<String> overallStatus;

    @JsonProperty("StatusVerifiedDate")
    private List<String> statusVerifiedDate;

    @JsonProperty("StartDate")
    private List<String> startDate;
}

