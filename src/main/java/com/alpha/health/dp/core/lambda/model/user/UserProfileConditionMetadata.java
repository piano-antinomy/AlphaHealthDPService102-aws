package com.alpha.health.dp.core.lambda.model.user;

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
    private UserConditions userConditions;
    private UserSurgeries userSurgeries;
    private UserEBRT userEBRT;
    public List<UserDrug> userDrugs;
    private UserLabs userLabs;
    public UserBiopsies userBiopsies;
    public UserTNM userTNM;
}
