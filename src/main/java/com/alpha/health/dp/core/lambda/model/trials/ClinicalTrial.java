package com.alpha.health.dp.core.lambda.model.trials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ClinicalTrial {
    public TrialIdentification trialIdentification;

    public TrialStatus trialStatus;

    public TrialLocation trialLocation;

    public List<TrialLocation> allTrialLocations;

    public StudyCondition studyCondition;
}
