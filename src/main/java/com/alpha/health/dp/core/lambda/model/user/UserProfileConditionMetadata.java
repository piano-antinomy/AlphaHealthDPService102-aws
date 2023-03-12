package com.alpha.health.dp.core.lambda.model.user;

import com.alpha.health.dp.core.lambda.util.Duration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * should be part of user metadata. This should only contain condition related metadata.
 *
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileConditionMetadata {
    private UserDemographics userDemographics;
    private List<UserCondition> userConditions;
    private List<UserSurgery> userSurgeries;
    private List<UserEBRT> userEBRTs;
    private List<UserDrug> userDrugs;
    private List<UserLab> userLabs;
    private List<UserBiopsy> userBiopsies;
    private List<UserTNM> userTNMs;
    private Duration userPSADoublingTime;
    private Duration userLifeExpectancy;
}
