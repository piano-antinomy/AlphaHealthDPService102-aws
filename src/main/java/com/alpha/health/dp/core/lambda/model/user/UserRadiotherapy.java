package com.alpha.health.dp.core.lambda.model.user;

import com.alpha.health.dp.core.lambda.util.Duration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRadiotherapy {
    private String name;
    private String type;
    private String region;
    private String target;
    private Number dose;
    private String purpose;
    private DateTime startDate;
    private DateTime endDate;
    @Setter
    private Duration durationWithdrawal;
}
