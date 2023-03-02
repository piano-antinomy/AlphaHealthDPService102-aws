package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTNM {
    private String primaryCancer;
    private DateTime dateOfStaging;
    /**
     * change to enum later for all TNM stage data
     */
    private String tStagePrimary;
    private String tStageSecondary;
    private String nStagePrimary;
    private String nStageSecondary;
    private String mStagePrimary;
    private String mStageSecondary;
}
