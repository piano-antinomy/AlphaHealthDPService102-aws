package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDrug {
    public int cycles;
    public String type;
    public String purpose;
    public String name;
    public DateTime startDate;
    public DateTime endDate;
    public Number durationDays;
}
