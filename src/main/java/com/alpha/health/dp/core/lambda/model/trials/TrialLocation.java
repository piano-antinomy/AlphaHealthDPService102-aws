package com.alpha.health.dp.core.lambda.model.trials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrialLocation {
    public String locationStatus;
    public String locationCity;
    public String locationState;
    public String locationZip;
    public String locationCountry;
}

