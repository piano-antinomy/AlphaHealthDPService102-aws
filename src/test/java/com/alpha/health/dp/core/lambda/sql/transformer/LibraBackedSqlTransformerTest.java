package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserBiopsies;
import com.alpha.health.dp.core.lambda.model.user.UserDrug;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.model.user.UserTNM;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joo.libra.PredicateContext;
import org.joo.libra.sql.SqlPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class LibraBackedSqlTransformerTest {
    @Test
    protected void simpleSqlHappyCase() {

        final UserProfileConditionMetadata user = UserProfileConditionMetadata.builder()
            .userTNM(UserTNM.builder()
                .t_StagePrimary("T0")
                .t_StageSecondary("a")
                .n_StagePrimary("N0")
                .build())
            .userBiopsies(UserBiopsies.builder()
                .gleasonScore(5)
                .build())
            .userDrugs(Arrays.asList(
                UserDrug.builder()
                    .cycles(2)
                    .name("ADT")
                    .startDate(DateTime.now().minusMonths(10))
                    .endDate(DateTime.now().minusMonths(1))
                    .build(),
                UserDrug.builder()
                    .cycles(5)
                    .name("BCYY")
                    .startDate(DateTime.now().minusMonths(5))
                    .endDate(DateTime.now().minusMonths(1))
                    .build()
            ))
            .build();

        // preprocessing to populate
        user.userDrugs.forEach(userDrug -> {
            userDrug.setDurationDays(Days.daysBetween(userDrug.getStartDate(), userDrug.getEndDate()).getDays());});

        final PredicateContext context = new PredicateContext(user);

        SqlPredicate sqlPredicateToPass_1 =
            new SqlPredicate("userTNM.t_StagePrimary = 'T0' and userBiopsies.gleasonScore >= 5");

        SqlPredicate sqlPredicateToPass_2 =
            new SqlPredicate("userTNM.t_StagePrimary in {'T0', 'T1'} and userBiopsies.gleasonScore >= 4");

        SqlPredicate sqlPredicateToPass_3 =
            new SqlPredicate(
                "(userTNM.n_StagePrimary in {'N0', 'N1'} or userTNM.t_StagePrimary in {'T1', 'T2'}) " +
                    "or userBiopsies.gleasonScore < 5");

        SqlPredicate sqlPredicateToFail_1 =
            new SqlPredicate("userTNM.t_StagePrimary in {'T0', 'T1'} and userBiopsies.gleasonScore > 6");

        SqlPredicate sqlPredicateToFail_2 =
            new SqlPredicate(
                "(userTNM.n_StagePrimary in {'N0', 'N1'} or userTNM.t_StagePrimary in {'T1', 'T2'}) " +
                "and userBiopsies.gleasonScore < 5");

        SqlPredicate userHasTakenADTFor6MoreMonths = new SqlPredicate(
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'ADT' AND $userDrug.durationDays > 180");
        SqlPredicate userHasTakenADTFor10MoreMonths = new SqlPredicate(
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'ADT' AND $userDrug.durationDays > 300");

        Assertions.assertTrue(sqlPredicateToPass_1.satisfiedBy(context));
        Assertions.assertTrue(sqlPredicateToPass_2.satisfiedBy(context));
        Assertions.assertTrue(sqlPredicateToPass_3.satisfiedBy(context));
        Assertions.assertFalse(sqlPredicateToFail_1.satisfiedBy(context));
        Assertions.assertFalse(sqlPredicateToFail_2.satisfiedBy(context));

        Assertions.assertTrue(userHasTakenADTFor6MoreMonths.satisfiedBy(context));
        Assertions.assertFalse(userHasTakenADTFor10MoreMonths.satisfiedBy(context));
    }
}


