package com.alpha.health.dp.core.lambda.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Duration {
    private Number years;
    private Number months;
    private Number weeks;
    private Number days;
}
