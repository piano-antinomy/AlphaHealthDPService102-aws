package com.alpha.health.dp.core.lambda.model.processor;

import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryUserProfilesProcessorResponse extends ProcessorResponse {
    private UserProfileConditionMetadata userProfileConditionMetadata;
}
