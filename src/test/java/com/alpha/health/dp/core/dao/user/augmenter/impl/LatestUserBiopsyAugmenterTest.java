package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.dao.user.augmenter.api.UserMetadataAugmenter;
import com.alpha.health.dp.core.lambda.model.user.UserBiopsy;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.sql.transformer.DemoMockUserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

public class LatestUserBiopsyAugmenterTest {
    private final UserMetadataAugmenter target = new LatestUserBiopsyAugmenter();
    @Test
    protected void testHappyCase() {

        // Arrange
        UserProfileConditionMetadata user = new DemoMockUserFactory().getMockUser();

        // Act
        UserProfileConditionMetadata newUser = target.augment(user);

        // Assert
        Assertions.assertEquals(user.getUserBiopsies().size(), newUser.getUserBiopsies().size());

        final String proState = "prostate";

        UserBiopsy latest = newUser.getUserBiopsies()
            .stream()
            .filter(b -> proState.equals(b.getTissue()))
            .filter(b -> b.getIsLatest())
            .collect(Collectors.toList())
            .iterator()
            .next();

        UserBiopsy early = newUser.getUserBiopsies()
            .stream()
            .filter(b -> proState.equals(b.getTissue()))
            .filter(b -> !b.getIsLatest())
            .collect(Collectors.toList())
            .iterator()
            .next();

        Assertions.assertTrue(latest.getDate().isAfter(early.getDate()));
    }
}
