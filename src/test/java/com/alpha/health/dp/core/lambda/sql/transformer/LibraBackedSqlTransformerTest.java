package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Years;
import org.joo.libra.PredicateContext;
import org.joo.libra.sql.SqlPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LibraBackedSqlTransformerTest {
    final DemoMockUserFactory userFactory = new DemoMockUserFactory();
    final UserProfileConditionMetadata user_1 = userFactory.getMockUser_1();

    @BeforeEach
    protected void setup() {
        // preprocessing to populate
        user_1.getUserDemographics()
            .setAge(Years.yearsBetween(user_1.getUserDemographics().getDateOfBirth(), DateTime.now()).getYears());

        user_1.getUserBiopsies().forEach(userBiopsy -> {
            userBiopsy.setDurationDays(Days.daysBetween(userBiopsy.getDate(), DateTime.now()).getDays());
        });

        user_1.getUserLabs().forEach(userLab -> {
            userLab.setDurationDays(Days.daysBetween(userLab.getDate(), DateTime.now()).getDays());
        });

        user_1.getUserSurgeries().forEach(userSurgery -> {
            userSurgery.setDurationDays(Days.daysBetween(userSurgery.getDate(), DateTime.now()).getDays());
        });

        user_1.getUserTNMs().forEach(userTNM -> {
            userTNM.setDurationDays(Days.daysBetween(userTNM.getDateOfStaging(), DateTime.now()).getDays());
        });

        user_1.getUserDrugs().forEach(userDrug -> {
            userDrug.setDurationDays(Days.daysBetween(userDrug.getStartDate(), userDrug.getEndDate()).getDays());});
    }

    @Test
    protected void simpleSqlHappyCase() {
        final String demoSimpleFilter = "userDemographics.gender = 'male' and userDemographics.race == 'white' ";
        final String demoAgeYoungerThan30 = "userDemographics.age < 30";
        final String demoAgeOlderThan70 = "userDemographics.age < 70";

        assertFilter(demoSimpleFilter, user_1, true);
        assertFilter(demoAgeYoungerThan30, user_1, false);
        assertFilter(demoAgeOlderThan70, user_1, true);
    }

    @Test
    protected void ableToFindOneInListType_HappyCase() {
        final String userHasTakenADTFor6MoreMonths =
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'afatinib' AND $userDrug.durationDays > 180";

        assertFilter(userHasTakenADTFor6MoreMonths, user_1, true);
    }

    @Test
    protected void ableToFindOneInListType_FailCase() {
        final String userHasTakenADTFor10MoreMonths =
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'afatinib' AND $userDrug.durationDays > 300";

        assertFilter(userHasTakenADTFor10MoreMonths, user_1, false);
    }

    /**
     * For use case:  https://docs.google.com/spreadsheets/d/13cpi59DO3TVnV3i2MgVc2EQD6iR6O0JoWh7bhrepHfs/edit#gid=0
     */
    @Test
    protected void complexQueryCombination() {

        /**
         * Histologic confirmation of adenocarcinoma of the prostate
         */
        final String criteria_1 = "ANY $userCondition IN userConditions satisfies $userCondition.name contains 'prostate' ";
        assertFilter(criteria_1, user_1, true);

        /**
         * The patient must have received definitive local therapy for prostate cancer,
         * consisting of either radiation therapy
         * and/or prostatectomy (salvage or adjuvant radiation post-prostatectomy is not exclusionary)
         */
        final String criteria_2 =
            "(ANY $userSurgery IN userSurgeries satisfies $userSurgery.name = 'prostatectomy' AND $userSurgery.purpose = 'radical' ) " +
            " OR " +
            "(ANY $userEBRT IN userEBRTs satisfies $userEBRT.target contains 'prostatectomy' AND $userSurgery.purpose = 'radical' )";
        assertFilter(criteria_2, user_1, true);
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


