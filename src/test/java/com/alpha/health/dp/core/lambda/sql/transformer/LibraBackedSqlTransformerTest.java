package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserBiopsies;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.model.user.UserTNM;
import org.joo.libra.PredicateContext;
import org.joo.libra.sql.SqlPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
            .build();

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

        Assertions.assertTrue(sqlPredicateToPass_1.satisfiedBy(context));
        Assertions.assertTrue(sqlPredicateToPass_2.satisfiedBy(context));
        Assertions.assertTrue(sqlPredicateToPass_3.satisfiedBy(context));
        Assertions.assertFalse(sqlPredicateToFail_1.satisfiedBy(context));
        Assertions.assertFalse(sqlPredicateToFail_2.satisfiedBy(context));
    }
}
