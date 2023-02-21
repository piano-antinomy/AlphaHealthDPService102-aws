package com.alpha.health.dp.core.lambda.model.processor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryClinicalTrialsProcessorRequest extends ProcessorRequest {
    private String condition;
    private String location;

    @Override
    public String toString() {
        return Arrays.asList(this.getCondition(), this.getLocation())
            .stream()
            .map(Object::toString)
            .map(String::trim)
            .map(s -> s.replaceAll(" +", " "))
            .map(s -> s.replaceAll(" ", "%20"))
            .filter(s -> s != null && !s.isEmpty())
            .collect(Collectors.joining("+"));
    }
}
