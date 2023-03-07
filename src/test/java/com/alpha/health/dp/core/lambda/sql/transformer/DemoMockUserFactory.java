package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserBiopsy;
import com.alpha.health.dp.core.lambda.model.user.UserDemographics;
import com.alpha.health.dp.core.lambda.model.user.UserDrug;
import com.alpha.health.dp.core.lambda.model.user.UserLab;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.model.user.UserSurgery;
import com.alpha.health.dp.core.lambda.model.user.UserTNM;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;

public class DemoMockUserFactory {
    protected UserProfileConditionMetadata getMockUser_1() {
        return UserProfileConditionMetadata.builder()
            .userDemographics(UserDemographics.builder()
                .dateOfBirth(DateTime.parse("1960-09-01"))
                .maritalStatus("married")
                .race("white")
                .gender("male")
                .build())
            .userTNMs(Collections.singletonList(UserTNM.builder()
                .primaryCancer("prostate")
                .t_StagePrimary("T0")
                .t_StageSecondary("a")
                .t_Method("0")
                .n_StagePrimary("0").
                n_StageSecondary("").
                n_Method("0").
                m_StagePrimary("0").
                m_StageSecondary("").
                m_Method("c")
                .build()))
            .userLabs(Arrays.asList(
                UserLab.builder().specimen("blood").test("TPSA").dateTime(DateTime.parse("2022-06-10")).level(32).positive("").build(),
                UserLab.builder().specimen("blood").test("TPSA").dateTime(DateTime.parse("2022-08-10")).level(5).positive("").build(),
                UserLab.builder().specimen("blood").test("TPSA").dateTime(DateTime.parse("2022-09-10")).level(1).positive("").build(),
                UserLab.builder().specimen("blood").test("TPSA").dateTime(DateTime.parse("2022-11-10")).level(0.5f).positive("").build(),
                UserLab.builder().specimen("blood").test("TPSA").dateTime(DateTime.parse("2022-12-10")).level(0.05f).positive("").build()))
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
                    .cycles(5)
                    .name("BCYY")
                    .startDate(DateTime.now().minusMonths(5))
                    .endDate(DateTime.now().minusMonths(1))
                    .type("EGFR inhibitor; targeted therapy;")
                    .name("XXXX")
                    .purpose("palliative")
                    .build()
            ))
            .build();
    }
}
