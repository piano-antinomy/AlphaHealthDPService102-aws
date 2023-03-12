package com.alpha.health.dp.core.dao.user.augmenter.api;

import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;

public interface UserMetadataAugmenter {
    UserProfileConditionMetadata augment(UserProfileConditionMetadata originalMetadata);
}
