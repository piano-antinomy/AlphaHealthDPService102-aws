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
public class UserBiopsy {
    private String tissue;
    private int gleasonPrimary;
    private int gleasonSecondary;
    private DateTime date;
    @Setter
    private int gleasonScore;
    @Setter
    private int gleasonGrade;
    @Setter
    private Duration duration;
    @Setter
    private Boolean isLatest;
}
