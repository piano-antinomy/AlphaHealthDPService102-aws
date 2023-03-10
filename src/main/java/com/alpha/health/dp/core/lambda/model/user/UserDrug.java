package com.alpha.health.dp.core.lambda.model.user;

import com.alpha.health.dp.core.lambda.util.Duration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDrug {
    private int cycles;
    private String type;
    private String disease;
    private String purpose;
    private String name;
    private DateTime startDate;
    private DateTime endDate;

    @Setter
    private Duration durationWithdrawal;
}
