package com.alpha.health.dp.core.dao.impl;

import com.alpha.health.dp.core.dao.api.DPDataAccess;
import com.alpha.health.dp.core.dao.user.augmenter.impl.UserMetadataAugmenterFactory;
import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.util.AppConfig;
import com.alpha.health.dp.core.lambda.util.SingletonInstanceFactory;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * S3 backed impls.
 */
public class S3BackedDPDataAccessImpl implements DPDataAccess {

    final private static Logger LOGGER = LogManager.getLogger(S3BackedDPDataAccessImpl.class);
    final private UserMetadataAugmenterFactory userMetadataAugmenterFactory = new UserMetadataAugmenterFactory();
    final private AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    final private ObjectMapper objectMapper = SingletonInstanceFactory.getObjectMapperInstance();

    @Override
    public UserProfileConditionMetadata getUser(final String userProfileId) {
        final String userProfilePath = AppConfig.USER_PROFILE_PATH + "/" + userProfileId + ".json";

        LOGGER.info("querying S3: " + userProfilePath);

        try {
            final S3Object s3Object = s3Client.getObject(AppConfig.USER_BUCKET, userProfilePath);
            return userMetadataAugmenterFactory
                .getChainedAugmenter()
                .augment(
                    objectMapper.readValue(s3Object.getObjectContent(), UserProfileConditionMetadata.class));
        } catch (IOException e) {
            throw new RuntimeException(userProfileId + " format issue!", e);
        } catch (final AmazonS3Exception e) {
            LOGGER.error(e);

            if ("NoSuchKey".equals(e.getErrorCode())) {
                throw new IllegalArgumentException(userProfileId + " doesn't exist");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public List<ClinicalTrial> getAllClinicalTrials() {
        return null;
    }
}
