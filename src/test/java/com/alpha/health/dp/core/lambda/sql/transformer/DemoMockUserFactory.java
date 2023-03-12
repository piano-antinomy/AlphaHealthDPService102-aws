package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserBiopsy;
import com.alpha.health.dp.core.lambda.model.user.UserCondition;
import com.alpha.health.dp.core.lambda.model.user.UserDemographics;
import com.alpha.health.dp.core.lambda.model.user.UserDrug;
import com.alpha.health.dp.core.lambda.model.user.UserEBRT;
import com.alpha.health.dp.core.lambda.model.user.UserLab;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.model.user.UserSurgery;
import com.alpha.health.dp.core.lambda.model.user.UserTNM;
import com.alpha.health.dp.core.lambda.util.Duration;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;

public class DemoMockUserFactory {
    protected UserProfileConditionMetadata getMockUser() {
        return UserProfileConditionMetadata.builder()
            .userDemographics(UserDemographics.builder()
                .dateOfBirth(DateTime.parse("1960-09-01"))
                .maritalStatus("married")
                .race("white")
                .gender("male")
                .build())
            .userConditions(Arrays.asList(UserCondition.builder().name("prostate cancer").build()))
            .userTNMs(Collections.singletonList(UserTNM.builder()
                .primaryCancer("prostate cancer")
                .dateOfStaging(DateTime.parse("2021-09-01"))
                .t_StagePrimary("T0")
                .t_StageSecondary("a")
                .t_Method("c")
                .n_StagePrimary("N1").
                n_StageSecondary("").
                n_Method("c").
                m_StagePrimary("M1").
                m_StageSecondary("").
                m_Method("c")
                    .isLatest(true)
                .build()))
            .userLabs(Arrays.asList(
                UserLab.builder().specimen("blood").test("TPSA").date(DateTime.parse("2022-06-10")).level(32).build(),
                UserLab.builder().specimen("blood").test("TPSA").date(DateTime.parse("2022-08-10")).level(5).build(),
                UserLab.builder().specimen("blood").test("TPSA").date(DateTime.parse("2022-09-10")).level(1).build(),
                UserLab.builder().specimen("blood").test("TPSA").date(DateTime.parse("2022-11-10")).level(0.5f).build(),
                UserLab.builder().specimen("blood").test("TPSA").date(DateTime.parse("2022-12-10")).level(2).isLatest(true).build()))
            .userBiopsies(Collections.singletonList(UserBiopsy.builder()
                .gleasonScore(7)
                .gleasonPrimary(3)
                .gleasonSecondary(4)
                .gleasonGrade(2)
                .date(DateTime.parse("2022-06-15"))
                .build()))
            .userSurgeries(Arrays.asList(UserSurgery.builder()
                .name("prostatectomy")
                .date(DateTime.parse("2022-07-01"))
                .type("robot-assisted")
                .lymphNodes(true)
                .purpose("radical")
                .build()))
            .userEBRTs(Arrays.asList(UserEBRT.builder()
                .target("prostate")
                .purpose("radical")
                .build()))
            .userDrugs(Arrays.asList(
                UserDrug.builder()
                    .cycles(2)
                    .type("EGFR inhibitor; targeted therapy;")
                    .name("afatinib")
                    .purpose("palliative")
                    .startDate(DateTime.now().minusMonths(10))
                    .endDate(DateTime.now().minusMonths(1))
                    .build(),
                    UserDrug.builder()
                            .cycles(2)
                            .type("ADT;")
                            .name("afatinib")
                            .purpose("adjuvant_ebrt")
                            .startDate(DateTime.now().minusMonths(50))
                            .endDate(DateTime.now().minusMonths(7))
                            .build(),
                    UserDrug.builder()
                            .cycles(2)
                            .type("chemotherapy;")
                            .disease("prostate cancer")
                            .name("drug1")
                            .purpose("neoadjuvant")
                            .startDate(DateTime.now().minusMonths(50))
                            .endDate(DateTime.now().minusMonths(40))
                            .build(),
                    UserDrug.builder()
                            .cycles(2)
                            .disease("prostate cancer")
                            .type("chemotherapy;")
                            .name("drug2")
                            .purpose("neoadjuvant")
                            .startDate(DateTime.now().minusMonths(15))
                            .endDate(DateTime.now().minusMonths(5))
                            .build(),
                UserDrug.builder()
                    .cycles(5)
                    .name("BCYY")
                    .startDate(DateTime.now().minusMonths(5))
                    .endDate(DateTime.now().minusMonths(1))
                    .type("EGFR inhibitor; targeted therapy;")
                    .name("XXXX")
                    .purpose("palliative")
                    .build()))
                /**
                 * TODO update methods for PSA doubling time and life expectancy
                 */
                .userPSADoublingTime(Duration.builder().months(3).build())
                .userLifeExpectancy(Duration.builder().weeks(20).build())

            .build();
    }
}
