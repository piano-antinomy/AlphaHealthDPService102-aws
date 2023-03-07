package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import org.joda.time.Days;
import org.joo.libra.PredicateContext;
import org.joo.libra.sql.SqlPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LibraBackedSqlTransformerTest {
    final DemoMockUserFactory userFactory = new DemoMockUserFactory();
    final UserProfileConditionMetadata user = userFactory.getMockUser_1();

    @BeforeEach
    protected void setup() {
        // preprocessing to populate
        user.userDrugs.forEach(userDrug -> {
            userDrug.setDurationDays(Days.daysBetween(userDrug.getStartDate(), userDrug.getEndDate()).getDays());});
    }
    @Test
    protected void simpleSqlHappyCase() {
        final PredicateContext context = new PredicateContext(user);

        final String demoSimpleFilter = "userDemographics.gender = 'male' and userDemographics.race == 'white' ";

        final String userHasTakenADTFor6MoreMonths =
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'afatinib' AND $userDrug.durationDays > 180";

        final String userHasTakenADTFor10MoreMonths =
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'afatinib' AND $userDrug.durationDays > 300";

        assertFilter(demoSimpleFilter, user, true);
        assertFilter(userHasTakenADTFor6MoreMonths, user, true);
        assertFilter(userHasTakenADTFor10MoreMonths, user, false);
    }

    private void assertFilter(final String sqlFilter, final UserProfileConditionMetadata user, final boolean expected) {
        final PredicateContext context = new PredicateContext(user);
        final SqlPredicate sqlPredicate = new SqlPredicate(sqlFilter);

        if (expected) {
            Assertions.assertTrue(sqlPredicate.satisfiedBy(context));
        } else {
            Assertions.assertFalse(sqlPredicate.satisfiedBy(context));
        }
    }
}


