package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.*;
import com.alpha.health.dp.core.lambda.util.Duration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.DateTime;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;

public class DemoMockUserFactory {
    protected UserProfileConditionMetadata getMockUser() {
        UserProfileConditionMetadata user = UserProfileConditionMetadata.builder()
                .userDemographics(UserDemographics.builder()
                        .dateOfBirth(DateTime.parse("1960-09-01"))
                        .maritalStatus("married")
                        .race("white")
                        .gender("male")
                        .build())
                .userConditions(Arrays.asList(UserCondition.builder().name("prostate cancer").build()))
                .userTNMs(Collections.singletonList(UserTNM.builder()
                        .primaryCancer("prostate cancer")
                        .date(DateTime.parse("2021-09-01"))
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
                        .hasLymphNodeDissection(true)
                        .purpose("radical")
                        .build()))
                .userRadiotherapies(Arrays.asList(UserRadiotherapy.builder()
                        .name("EBRT")
                        .target("prostate")
                        .purpose("radical")
                        .endDate(DateTime.parse("2022-09-01"))
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
                            .startDate(DateTime.now().minusMonths(20))
                            .endDate(DateTime.now().minusMonths(10))
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
                .userPSADoublingTime(Duration.builder().months(3).build())
                .userLifeExpectancy(Duration.builder().weeks(20).build())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        try {
            String json = objectMapper.writeValueAsString(user);
            FileWriter fileWriter = new FileWriter("data/users/mock.json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }
}
