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
    private TrialIdentification trialIdentification;

    private TrialStatus trialStatus;

    private List<TrialLocation> trialLocations;

    private StudyCondition studyCondition;

    private String studyPhase;

    private String studyType;

    private List<StudyGroup> studyGroups;

    private EligibilityCriteria eligibilityCriteria;
}
