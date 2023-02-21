package com.alpha.health.dp.core.lambda.gov.client;

import com.alpha.health.dp.core.lambda.gov.model.ClinicalTrialGovClientResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ideally this should be an integration test instead of unit test
 *
 * For now this queries clinicaltrials.gov to make sure all data models are up to date.
 *
 * put it disabled to manual test only, so it will be skipped in mvn package.
 */

@Disabled
public class ClinicalTrialsGovClientTest {
    private final ClinicalTrialsGovClient target = new ClinicalTrialsGovClient();
    @Test
    protected void test_clinicalTrialsGovReturnsForEmptyRequest() {
        // Arrange
        final QueryClinicalTrialsProcessorRequest request =
            new QueryClinicalTrialsProcessorRequest("", "");

        // Act
        ClinicalTrialGovClientResponse response = target.getStudiesWithSpecifiedFields(request);

        // Assert
        Assertions.assertTrue(response.getStudyFieldsResponse().getStudyFields().size() > 1);
    }

    @Test
    protected void test_clinicalTrialsGovReturnsForLocationQuery() {
        // Arrange
        final QueryClinicalTrialsProcessorRequest request =
            new QueryClinicalTrialsProcessorRequest("", "Seattle");

        // Act
        ClinicalTrialGovClientResponse response = target.getStudiesWithSpecifiedFields(request);

        // Assert

        List<String> locationCityList =
            response.getStudyFieldsResponse().getStudyFields().iterator().next().getLocationCity();

        Set<String> mergedLocationCities = response.getStudyFieldsResponse().getStudyFields().stream()
            .flatMap(s -> s.getLocationCity().stream())
            .collect(Collectors.toSet());

        System.out.println(mergedLocationCities);
        Assertions.assertTrue(mergedLocationCities.contains(request.getLocation()));
    }

    @Test
    protected void test_clinicalTrialsGovReturnsForAllFieldsQuery() {
        // Arrange
        final QueryClinicalTrialsProcessorRequest request =
            new QueryClinicalTrialsProcessorRequest("migraine", "Seattle");

        System.out.println(request);
    }
}
