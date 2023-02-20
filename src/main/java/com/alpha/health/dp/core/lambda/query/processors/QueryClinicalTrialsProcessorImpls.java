package com.alpha.health.dp.core.lambda.query.processors;

import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorResponse;
import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import com.alpha.health.dp.core.lambda.model.trials.TrialIdentification;
import com.alpha.health.dp.core.lambda.model.trials.TrialStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

public class QueryClinicalTrialsProcessorImpls implements ServiceRequestProcessor {
    private static final Logger LOGGER = LogManager.getLogger(QueryClinicalTrialsProcessorImpls.class);
    @Override
    public ProcessorResponse process(ProcessorRequest request) {

        LOGGER.info("processing request: " + request);

        // TODO: concrete logic

        return QueryClinicalTrialsProcessorResponse
            .builder()
            .clinicalTrialList(
                Collections.singletonList(
                    ClinicalTrial.builder()
                        .trialIdentification(TrialIdentification.builder().briefTitle("test id").build())
                        .trialStatus(TrialStatus.builder().overallStatus("pending").build())
                        .build()))
            .build();
    }
}
