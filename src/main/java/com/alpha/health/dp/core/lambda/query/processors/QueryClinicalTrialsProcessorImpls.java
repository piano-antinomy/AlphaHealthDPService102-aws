package com.alpha.health.dp.core.lambda.query.processors;

import com.alpha.health.dp.core.lambda.gov.client.ClinicalTrialsGovClient;
import com.alpha.health.dp.core.lambda.gov.model.ClinicalTrialGovClientResponse;
import com.alpha.health.dp.core.lambda.gov.model.Study;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorResponse;
import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import com.alpha.health.dp.core.lambda.model.trials.StudyCondition;
import com.alpha.health.dp.core.lambda.model.trials.TrialIdentification;
import com.alpha.health.dp.core.lambda.model.trials.TrialLocation;
import com.alpha.health.dp.core.lambda.model.trials.TrialStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryClinicalTrialsProcessorImpls implements ServiceRequestProcessor {
    private static final Logger LOGGER = LogManager.getLogger(QueryClinicalTrialsProcessorImpls.class);
    private static final DateTimeParser[] DATE_TIME_PARSERS = {
        DateTimeFormat.forPattern("MMM d, yyyy").getParser(),
        DateTimeFormat.forPattern("MMM yyyy").getParser()
    };
    public final ClinicalTrialsGovClient clinicalTrialsGovClient = new ClinicalTrialsGovClient();
    @Override
    public ProcessorResponse process(ProcessorRequest request) {

        if (!(request instanceof QueryClinicalTrialsProcessorRequest)) {
            throw new IllegalArgumentException("Processor request has an invalid type " + request.getClass());
        }

        final QueryClinicalTrialsProcessorRequest queryClinicalTrialsProcessorRequest =
            (QueryClinicalTrialsProcessorRequest) request;

        LOGGER.info("processing query request: " + queryClinicalTrialsProcessorRequest);

        ClinicalTrialGovClientResponse clinicalTrialsGovClientResponse =
            clinicalTrialsGovClient.getStudiesWithSpecifiedFields(queryClinicalTrialsProcessorRequest);

        return buildResponse(queryClinicalTrialsProcessorRequest, clinicalTrialsGovClientResponse);
    }

    private ProcessorResponse buildResponse(
        QueryClinicalTrialsProcessorRequest queryClinicalTrialsProcessorRequest,
        ClinicalTrialGovClientResponse clinicalTrialsGovClientResponse) {
        // Merge all location fields

        List<ClinicalTrial> list = clinicalTrialsGovClientResponse.getStudyFieldsResponse().getStudyFields().stream()
            .map(t -> mapStudyToTrialsType(t, queryClinicalTrialsProcessorRequest.getLocation()))
            .filter(c -> !c.getTrialLocations().isEmpty())
            .collect(Collectors.toList());

        return QueryClinicalTrialsProcessorResponse.builder()
            .clinicalTrialList(list)
            .build();
    }

    private ClinicalTrial mapStudyToTrialsType(final Study study, final String location) {

        Set<TrialLocation> locationSet = new HashSet<>();

        /** debug only.
        System.out.println("city list " + study.getLocationCity());
        System.out.println("city size " + study.getLocationCity().size());

        System.out.println("state list " + study.getLocationState());
        System.out.println("state size " + study.getLocationState().size());

        System.out.println("country list " + study.getLocationCountry());
        System.out.println("country size " + study.getLocationCountry().size());

        System.out.println("zip list " + study.getLocationZip());
        System.out.println("zip size " + study.getLocationZip().size());

        System.out.println("status list " + study.getLocationStatus());
        System.out.println("status size " + study.getLocationStatus().size());
         *
         */

        for (int i = 0; i < study.getLocationCity().size(); i++) {
            locationSet.add(
                TrialLocation.builder()
                    .locationCity(study.getLocationCity().get(i))
                    .locationState(
                        study.getLocationState().size() > i ? study.getLocationState().get(i) : "")
                    .locationCountry(
                        study.getLocationCountry().size() > i ? study.getLocationCountry().get(i) : "")
                    .locationZip(
                        study.getLocationZip().size() > i ? study.getLocationZip().get(i) : "")
                    .locationStatus(study.getLocationStatus().size() > i ?
                        study.getLocationStatus().get(i) : study.getOverallStatus().iterator().next())
                    .build());
        }

        List<TrialLocation> filteredLocationList = locationSet.stream()
            .filter(trialLocation -> trialLocation.toString().contains(location))
            .collect(Collectors.toList());

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().append( null, DATE_TIME_PARSERS).toFormatter();

        return ClinicalTrial.builder()
            .trialIdentification(TrialIdentification.builder()
                .nctId(study.getNCTId().iterator().next())
                .briefTitle(study.getBriefTitle().iterator().next())
                .OfficialTitle(study.getBriefTitle().iterator().next())
                .build())
            .trialStatus(TrialStatus.builder()
                .overallStatus(study.getOverallStatus().iterator().next())
                .startDate(
                    formatter.parseDateTime(study.getStartDate().iterator().next()))
                .statusVerifiedDate(
                    formatter.parseDateTime(study.getStatusVerifiedDate().iterator().next()))
                .build())
            .trialLocations(filteredLocationList)
            .studyCondition(StudyCondition.builder().conditions(study.getConditions()).build())
            .build();
    }

}
