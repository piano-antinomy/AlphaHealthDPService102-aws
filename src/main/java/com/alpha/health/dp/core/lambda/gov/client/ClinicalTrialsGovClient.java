package com.alpha.health.dp.core.lambda.gov.client;

import com.alpha.health.dp.core.lambda.gov.model.ClinicalTrialGovClientResponse;
import com.alpha.health.dp.core.lambda.gov.model.StudyFieldsResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;

public class ClinicalTrialsGovClient {
    private static String FIELD_LIST = "fields=NCTId,Condition,BriefTitle,OfficialTitle," +
        "LocationCity,LocationState,LocationZip,LocationCountry,LocationStatus,LocationFacility," +
        "OverallStatus,StatusVerifiedDate,StartDate";
    private static String CLINICAL_TRIALS_GOV_URL = "https://clinicaltrials.gov/api/query/study_fields?";

    private static final Logger LOGGER = LogManager.getLogger(ClinicalTrialsGovClient.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * gets Json response from clinical trials gov api
     * @param request
     * @return
     */
    public ClinicalTrialGovClientResponse getStudiesWithSpecifiedFields(QueryClinicalTrialsProcessorRequest request) {
        try {
            final String constructedURL = CLINICAL_TRIALS_GOV_URL
                + "expr="
                + request.toString()
                + "&"
                + FIELD_LIST
                + "&fmt=JSON";

            final URL url = new URL(constructedURL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            LOGGER.info("querying " + constructedURL);
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                LOGGER.error("HttpResponseCode from https://clinicaltrials.gov/: " + responseCode + "... returning empty result");
                return ClinicalTrialGovClientResponse.builder()
                    .studyFieldsResponse(StudyFieldsResponse.builder().studyFields(Collections.emptyList()).build())
                    .build();
            }

            final String response = new BufferedReader(
                new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

            return objectMapper.readValue(response, ClinicalTrialGovClientResponse.class);

        } catch (final Exception e) {
            throw new RuntimeException("SERVER Error: " + e);
        }


    }
}
