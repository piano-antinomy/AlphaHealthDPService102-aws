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
public class UserSurgery {
    private String name;
    private DateTime date;
    private String type;
    private Boolean hasLymphNodeDissection;
    private String purpose;
    @Setter
    private Duration duration;
}
