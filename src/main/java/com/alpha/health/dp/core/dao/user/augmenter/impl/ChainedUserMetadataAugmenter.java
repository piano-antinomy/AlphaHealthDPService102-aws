package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.dao.user.augmenter.api.UserMetadataAugmenter;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;

import java.util.Arrays;
import java.util.List;

public class ChainedUserMetadataAugmenter implements UserMetadataAugmenter {
    List<UserMetadataAugmenter> registeredAugmenters;
    public ChainedUserMetadataAugmenter(final UserMetadataAugmenter... augmenters) {
        registeredAugmenters = Arrays.asList(augmenters);
    }
    @Override
    public UserProfileConditionMetadata augment(UserProfileConditionMetadata originalMetadata) {
        UserProfileConditionMetadata newMetadata = originalMetadata;

        for (UserMetadataAugmenter augmenter : registeredAugmenters) {
            newMetadata = augmenter.augment(newMetadata);
        }

        return newMetadata;
    }
}
