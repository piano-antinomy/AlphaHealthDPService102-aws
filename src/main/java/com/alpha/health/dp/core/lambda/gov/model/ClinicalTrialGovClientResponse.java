package com.alpha.health.dp.core.lambda.gov.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalTrialGovClientResponse {

    @JsonProperty("StudyFieldsResponse")
    private StudyFieldsResponse studyFieldsResponse;
}
