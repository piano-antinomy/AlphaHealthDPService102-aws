package com.alpha.health.dp.core.lambda.model.processor;

import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import lombok.Builder;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
public class QueryClinicalTrialsProcessorResponse extends ProcessorResponse {
    public List<ClinicalTrial> clinicalTrialList;
}
