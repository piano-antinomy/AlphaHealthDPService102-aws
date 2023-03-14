package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.lambda.model.user.UserLab;

public class LatestUserLabAugmenter extends AbstractLatestBackedTypeAugmenter {
    public LatestUserLabAugmenter() {
        super(UserLab.class);
    }
}
