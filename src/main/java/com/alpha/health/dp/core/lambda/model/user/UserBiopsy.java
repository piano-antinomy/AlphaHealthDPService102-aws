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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBiopsy {
    private int gleasonScore;
    private int gleasonPrimary;
    private int gleasonSecondary;
    private int gleasonGrade;
    private DateTime date;
    @Setter
    private Number durationDays;

}
