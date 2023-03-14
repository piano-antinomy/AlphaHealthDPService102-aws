package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.lambda.model.user.UserTNM;

public class LatestTNMLabAugmenter extends AbstractLatestBackedTypeAugmenter {
    public LatestTNMLabAugmenter() {
        super(UserTNM.class);
    }
}
