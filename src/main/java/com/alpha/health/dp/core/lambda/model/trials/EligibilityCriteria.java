package com.alpha.health.dp.core.lambda.model.trials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EligibilityCriteria {
    private String sqlLikeConditionInclusive;

    private String sqlLikeConditionExclusive;

    private String eligibilityDescription;
}
