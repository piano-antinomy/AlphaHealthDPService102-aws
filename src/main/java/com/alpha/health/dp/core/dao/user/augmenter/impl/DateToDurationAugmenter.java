package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.dao.user.augmenter.api.UserMetadataAugmenter;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.Years;

class DateToDurationAugmenter implements UserMetadataAugmenter {

    private static Logger LOGGER = LogManager.getLogger(DateToDurationAugmenter.class);
    @Override
    public UserProfileConditionMetadata augment(final UserProfileConditionMetadata originalMetadata) {

        LOGGER.info("DateToDurationAugmenter starts augmenting userProfileData");

        final UserProfileConditionMetadata user = originalMetadata;
        user.populateDerivedFields();

        /*
        user.getUserDemographics()
            .setAge(Years.yearsBetween(user.getUserDemographics().getDateOfBirth(), DateTime.now()).getYears());

        user.getUserBiopsies().forEach(userBiopsy -> {
            userBiopsy.setDurationDays(Days.daysBetween(userBiopsy.getDate(), DateTime.now()).getDays());
        });

        user.getUserLabs().forEach(userLab -> {
            userLab.setDurationDays(Days.daysBetween(userLab.getDate(), DateTime.now()).getDays());
        });

        user.getUserSurgeries().forEach(userSurgery -> {
            userSurgery.setDurationDays(Days.daysBetween(userSurgery.getDate(), DateTime.now()).getDays());
        });

        user.getUserTNMs().forEach(userTNM -> {
            userTNM.setDurationDays(Days.daysBetween(userTNM.getDateOfStaging(), DateTime.now()).getDays());
        });

        user.getUserDrugs().forEach(userDrug -> {
            userDrug.setDurationWithdrawal(Duration.builder()
                .years(Years.yearsBetween(userDrug.getEndDate(), DateTime.now()).getYears())
                .months(Months.monthsBetween(userDrug.getEndDate(), DateTime.now()).getMonths())
                .days(Days.daysBetween(userDrug.getEndDate(), DateTime.now()).getDays()).build());
        });
        */

        return user;
    }
}
