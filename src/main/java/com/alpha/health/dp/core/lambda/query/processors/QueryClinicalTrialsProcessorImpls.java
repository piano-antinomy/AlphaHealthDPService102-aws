package com.alpha.health.dp.core.lambda.query.processors;

import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorResponse;
import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import com.alpha.health.dp.core.lambda.model.trials.StudyCondition;
import com.alpha.health.dp.core.lambda.model.trials.StudyGroup;
import com.alpha.health.dp.core.lambda.model.trials.TrialIdentification;
import com.alpha.health.dp.core.lambda.model.trials.TrialLocation;
import com.alpha.health.dp.core.lambda.model.trials.TrialStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QueryClinicalTrialsProcessorImpls implements ServiceRequestProcessor {
    private static final Logger LOGGER = LogManager.getLogger(QueryClinicalTrialsProcessorImpls.class);

    @Override
    public ProcessorResponse process(ProcessorRequest request) {

        if (!(request instanceof QueryClinicalTrialsProcessorRequest)) {
            throw new IllegalArgumentException("Processor request has an invalid type " + request.getClass());
        }

        final QueryClinicalTrialsProcessorRequest queryClinicalTrialsProcessorRequest =
            (QueryClinicalTrialsProcessorRequest) request;

        LOGGER.info("processing query request: " + queryClinicalTrialsProcessorRequest);

        return buildResponse(queryClinicalTrialsProcessorRequest);
    }

    private ProcessorResponse buildResponse(
        QueryClinicalTrialsProcessorRequest queryClinicalTrialsProcessorRequest) {
        // Merge all location fields

        List<ClinicalTrial> list = Collections.singletonList(buildClinicalTrial(queryClinicalTrialsProcessorRequest));

        return QueryClinicalTrialsProcessorResponse.builder()
            .clinicalTrialList(list)
            .build();
    }

    /**
     * TODO, change to real DAO model.
     * @param queryClinicalTrialsProcessorRequest
     * @return
     */
    private ClinicalTrial buildClinicalTrial(QueryClinicalTrialsProcessorRequest queryClinicalTrialsProcessorRequest) {

        List<TrialLocation> locationList = Collections.singletonList(TrialLocation.builder()
            .locationCity(queryClinicalTrialsProcessorRequest.getLocation())
            .locationState("WA")
            .instituteName("Beijie Seattle Care Center (BSCC)")
            .locationCountry("United States")
            .locationZip("98109")
            .locationStatus("Recruiting")
            .build());

        StudyGroup group_0 = StudyGroup.builder()
            .groupDescription("Civasheet 7")
            .groupId("0")
            .groupIntervention("Radiotherapy:external-beam radiation")
            .build();

        StudyGroup group_1 = StudyGroup.builder()
            .groupDescription("Radiotherapy:Civasheet 60 Gy or 75 Gy;")
            .groupId("1")
            .groupIntervention("Radiotherapy:Civasheet 60 Gy or 75 Gy;")
            .build();

        return ClinicalTrial.builder()
            .trialIdentification(TrialIdentification.builder()
                .nctId("NCT04472338")
                .briefTitle("Prostate Screening for Men With Inherited Risk of Developing Aggressive Prostate Cancer, PATROL Study")
                .OfficialTitle("Prostate Screening for Men With Inherited Risk of Developing Aggressive Prostate Cancer, PATROL Study")
                .build())
            .trialStatus(TrialStatus.builder()
                .overallStatus("Recruiting")
                .startDate(DateTime.now().minusMonths(1))
                .statusVerifiedDate(DateTime.now().minusDays(1))
                .build())
            .trialLocations(locationList)
            .studyCondition(StudyCondition.builder().conditions(Collections.singletonList("Prostate Carcinoma")).build())
            .studyType("Observational")
            .studyPhase("Phase 1")
            .studyGroups(Arrays.asList(group_0, group_1))
            .build();
    }

}
