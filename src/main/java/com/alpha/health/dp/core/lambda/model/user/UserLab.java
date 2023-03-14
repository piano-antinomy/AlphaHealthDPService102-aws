package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLab extends AbstractLatestBackedType {
    private String specimen;
    private String test;
    private float level;
    private Boolean isPositive;
    @Setter
    private Number durationDays;
    @Override
    public String getSortKey() {
        return specimen + test;
    }
}
