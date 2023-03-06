package com.alpha.health.dp.core.lambda.model.processor;

import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
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
public class QueryClinicalTrialsProcessorResponse extends ProcessorResponse {
    public List<ClinicalTrial> clinicalTrialList;
}
