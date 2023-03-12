package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.dao.user.augmenter.api.UserMetadataAugmenter;

import java.util.Arrays;
import java.util.List;

public class UserMetadataAugmenterFactory {

    List<UserMetadataAugmenter> registry = Arrays.asList(
        new DateToDurationAugmenter()
    );
    public UserMetadataAugmenter getChainedAugmenter() {
        return new ChainedUserMetadataAugmenter(registry);
    }
}
