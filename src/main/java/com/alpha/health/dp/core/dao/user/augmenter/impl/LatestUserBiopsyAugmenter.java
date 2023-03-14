package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.lambda.model.user.UserBiopsy;

/**
 * figures out the latest UserBiopsy based on the biopsy.tissue
 */
public class LatestUserBiopsyAugmenter extends AbstractLatestBackedTypeAugmenter {
    public LatestUserBiopsyAugmenter() {
        super(UserBiopsy.class);
    }
}
