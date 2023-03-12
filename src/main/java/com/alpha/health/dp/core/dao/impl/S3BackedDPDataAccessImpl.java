package com.alpha.health.dp.core.dao.impl;

import com.alpha.health.dp.core.dao.api.DPDataAccess;
import com.alpha.health.dp.core.dao.user.augmenter.impl.UserMetadataAugmenterFactory;
import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.amazonaws.services.s3.AmazonS3;

import java.util.List;

/**
 * S3 backed impls.
 */
public class S3BackedDPDataAccessImpl implements DPDataAccess {

    final private UserMetadataAugmenterFactory userMetadataAugmenterFactory = new UserMetadataAugmenterFactory();
    final private AmazonS3 amazonS3 = null;

    @Override
    public UserProfileConditionMetadata getUser(final String userProfileId) {
        return null;
    }

    @Override
    public List<ClinicalTrial> getAllClinicalTrials() {
        return null;
    }
}
