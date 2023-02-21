package com.alpha.health.dp.core.lambda.model.trials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrialStatus {
    private String overallStatus;

    private DateTime statusVerifiedDate;

    private DateTime startDate;

}
