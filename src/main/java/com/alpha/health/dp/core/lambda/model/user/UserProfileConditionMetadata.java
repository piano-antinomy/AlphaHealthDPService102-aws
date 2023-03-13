package com.alpha.health.dp.core.lambda.model.user;

import com.alpha.health.dp.core.lambda.util.Duration;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * should be part of user metadata. This should only contain condition related metadata.
 *
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileConditionMetadata {
    private UserDemographics userDemographics;
    private List<UserCondition> userConditions;
    private List<UserSurgery> userSurgeries;
    private List<UserRadiotherapy> userRadiotherapies;
    private List<UserDrug> userDrugs;
    private List<UserLab> userLabs;
    private List<UserBiopsy> userBiopsies;
    private List<UserTNM> userTNMs;
    private Duration userPSADoublingTime;
    private Duration userLifeExpectancy;

    public void populateDerivedFields () {
        // derive UserDemographics.age
        this.userDemographics
                .setAge(Years.yearsBetween(this.userDemographics.getDateOfBirth(), DateTime.now()).getYears());

        // derive UserBiopsy.gleason score, grade, duration, and isLatest
        Map<String, DateTime> latestDates = new HashMap<>(); // key: tissue
        Map<String, Integer> latestIndices = new HashMap<>();

        for (int i = 0; i < this.userBiopsies.size(); i ++) {
            UserBiopsy userBiopsy = this.userBiopsies.get(i);

            if (userBiopsy.getGleasonScore() == 0) {
                userBiopsy.setGleasonScore(userBiopsy.getGleasonPrimary() + userBiopsy.getGleasonSecondary());
            }
            /**
             * https://www.mountsinai.org/health-library/special-topic/gleason-grading-system
             */
            if (userBiopsy.getGleasonScore() <= 6) {
                userBiopsy.setGleasonGrade(1);
            } else if (userBiopsy.getGleasonPrimary() == 3 && userBiopsy.getGleasonSecondary() == 4) {
                userBiopsy.setGleasonGrade(2);
            } else if (userBiopsy.getGleasonPrimary() == 4 && userBiopsy.getGleasonSecondary() == 3) {
                userBiopsy.setGleasonGrade(3);
            } else if (userBiopsy.getGleasonScore() == 8) {
                userBiopsy.setGleasonGrade(4);
            } else if (userBiopsy.getGleasonScore() == 9 || userBiopsy.getGleasonScore() == 10) {
                userBiopsy.setGleasonGrade(5);
            }

            userBiopsy.setDuration(Duration.builder().
                    years(Years.yearsBetween(userBiopsy.getDate(), DateTime.now()).getYears()).
                    months(Months.monthsBetween(userBiopsy.getDate(), DateTime.now()).getMonths()).
                    weeks(Weeks.weeksBetween(userBiopsy.getDate(), DateTime.now()).getWeeks()).
                    days(Days.daysBetween(userBiopsy.getDate(), DateTime.now()).getDays()).build());

            // for each tissue, find the most recent biopsy
            String key = userBiopsy.getTissue();
            if (latestDates.containsKey(key)) {
                DateTime latestDate = latestDates.get(key);
                if (latestDate.compareTo(userBiopsy.getDate()) < 0) {
                    latestDates.put(key, userBiopsy.getDate());
                    latestIndices.put(key, i);
                }
            }
            else {
                latestDates.put(key, userBiopsy.getDate());
                latestIndices.put(key, i);
            }
            userBiopsy.setIsLatest(false);
        }
        for (String key : latestIndices.keySet()) {
            int latestIndex = latestIndices.get(key);
            this.userBiopsies.get(latestIndex).setIsLatest(true);
        }

        // derive UserDrug.type and durationWithdrawal
        this.userDrugs.forEach(userDrug -> {
            // TODO create a dictionary for drug name-type mapping
            userDrug.setDurationWithdrawal(Duration.builder().
                    years(Years.yearsBetween(userDrug.getEndDate(), DateTime.now()).getYears()).
                    months(Months.monthsBetween(userDrug.getEndDate(), DateTime.now()).getMonths()).
                    weeks(Weeks.weeksBetween(userDrug.getEndDate(), DateTime.now()).getWeeks()).
                    days(Days.daysBetween(userDrug.getEndDate(), DateTime.now()).getDays()).build());
        });

        // derive UserLab.isLatest
        latestDates = new HashMap<>(); // key: specimen-test pair
        latestIndices = new HashMap<>();
        for (int i = 0; i < this.userLabs.size(); i ++) {
            UserLab userLab = this.userLabs.get(i);

            // for each specimen-test pair, find the most recent lab
            String key = userLab.getSpecimen() + userLab.getTest();
            if (latestDates.containsKey(key)) {
                DateTime latestDate = latestDates.get(key);
                if (latestDate.compareTo(userLab.getDate()) < 0) {
                    latestDates.put(key, userLab.getDate());
                    latestIndices.put(key, i);
                }
            }
            else {
                latestDates.put(key, userLab.getDate());
                latestIndices.put(key, i);
            }
            userLab.setIsLatest(false);
        }
        for (String key : latestIndices.keySet()) {
            int latestIndex = latestIndices.get(key);
            this.userLabs.get(latestIndex).setIsLatest(true);
        }

        // derive UserRadiotherapy.durationWithdrawal
        this.userRadiotherapies.forEach(userRadiotherapy -> {
            userRadiotherapy.setDurationWithdrawal(Duration.builder().
                    years(Years.yearsBetween(userRadiotherapy.getEndDate(), DateTime.now()).getYears()).
                    months(Months.monthsBetween(userRadiotherapy.getEndDate(), DateTime.now()).getMonths()).
                    weeks(Weeks.weeksBetween(userRadiotherapy.getEndDate(), DateTime.now()).getWeeks()).
                    days(Days.daysBetween(userRadiotherapy.getEndDate(), DateTime.now()).getDays()).build());
        });

        // derive UserSurgery.duration
        this.userSurgeries.forEach(userSurgery -> {
            userSurgery.setDuration(Duration.builder().
                    years(Years.yearsBetween(userSurgery.getDate(), DateTime.now()).getYears()).
                    months(Months.monthsBetween(userSurgery.getDate(), DateTime.now()).getMonths()).
                    weeks(Weeks.weeksBetween(userSurgery.getDate(), DateTime.now()).getWeeks()).
                    days(Days.daysBetween(userSurgery.getDate(), DateTime.now()).getDays()).build());
        });

        // derive UserTNM.isLatest
        latestDates = new HashMap<>(); // key: primaryCancer
        latestIndices = new HashMap<>();
        for (int i = 0; i < this.userTNMs.size(); i ++) {
            UserTNM userTNM = this.userTNMs.get(i);

            // for each primaryCancer, find the most recent TNM stage
            String key = userTNM.getPrimaryCancer();
            if (latestDates.containsKey(key)) {
                DateTime latestDate = latestDates.get(key);
                if (latestDate.compareTo(userTNM.getDate()) < 0) {
                    latestDates.put(key, userTNM.getDate());
                    latestIndices.put(key, i);
                }
            }
            else {
                latestDates.put(key, userTNM.getDate());
                latestIndices.put(key, i);
            }
            userTNM.setIsLatest(false);
        }
        for (String key : latestIndices.keySet()) {
            int latestIndex = latestIndices.get(key);
            this.userTNMs.get(latestIndex).setIsLatest(true);
        }

        // TODO update methods for PSA doubling time and life expectancy
    }
}
