package com.alpha.health.dp.core.lambda.model.trials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrialLocation {
    private String locationStatus;
    private String instituteName;
    private String locationCity;
    private String locationState;
    private String locationZip;
    private String locationCountry;

    @Override
    public String toString() {
        return Arrays.asList(this.getLocationCity(), this.getLocationState(), this.getLocationCountry(), this.locationZip)
            .stream()
            .map(Object::toString)
            .filter(s -> s != null && !s.isEmpty())
            .collect(Collectors.joining("+"));
    }
}

