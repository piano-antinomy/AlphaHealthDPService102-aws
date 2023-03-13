package com.alpha.health.dp.core.lambda;

import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.util.SingletonInstanceFactory;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * dangerous test. do not check this in. comment out credentials before check-in !!
 */
public class S3ClientTest {
    final AmazonS3 s3 = AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(
            new BasicAWSCredentials("AKIA4VOXRQCYW7UZJ3HQ", "jdW9Fg/o0Svfke32SUjagfIJcBqteUcoCDysn50U")))
        .withRegion(Regions.US_WEST_2)
        .build();

    final ObjectMapper objectMapper = SingletonInstanceFactory.getObjectMapperInstance();

    @Test
    protected void test() {
        try {
            S3Object s3Object = s3.getObject("alpha-health-user-profiles", "profiles/user-profile-1A35624F-E2F6-4A3E-BDA8-CBEA61AC91DA.json");
            UserProfileConditionMetadata metadata = objectMapper.readValue(s3Object.getObjectContent(), UserProfileConditionMetadata.class);
            System.out.println(metadata.getUserIdentification().getUserProfileId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AmazonS3Exception e) {
            System.out.println(e);
        }
    }
}
