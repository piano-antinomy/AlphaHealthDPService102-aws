package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.dao.user.augmenter.api.UserMetadataAugmenter;
import com.alpha.health.dp.core.dao.user.augmenter.impl.ChainedUserMetadataAugmenter;
import com.alpha.health.dp.core.dao.user.augmenter.impl.DateToDurationAugmenter;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import org.joo.libra.PredicateContext;
import org.joo.libra.sql.SqlPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraBackedSqlTransformerTest {
    final DemoMockUserFactory userFactory = new DemoMockUserFactory();
    final UserMetadataAugmenter userMetadataAugmenter = new ChainedUserMetadataAugmenter(new DateToDurationAugmenter());
    List<UserProfileConditionMetadata> users;

    @BeforeEach
    protected void setup() {
        users = new ArrayList();

        // TODO load test data from file
        for (int i = 0; i < 5; i ++) {
            users.add(userFactory.getMockUser());
        }

        users = users.stream().map(userMetadataAugmenter::augment).collect(Collectors.toList());
    }

    @Test
    protected void simpleSqlHappyCase() {
        final String demoSimpleFilter = "userDemographics.gender = 'male' and userDemographics.race == 'white' ";
        final String demoAgeYoungerThan30 = "userDemographics.age < 30";
        final String demoAgeYoungerThan70 = "userDemographics.age < 70";

        assertFilter(demoSimpleFilter, users.get(1), true);
        assertFilter(demoAgeYoungerThan30, users.get(1), false);
        assertFilter(demoAgeYoungerThan70, users.get(1), true);
    }

    @Test
    protected void ableToFindOneInListType_HappyCase() {
        final String userHasTakenADTFor6MoreMonths =
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'afatinib' AND $userDrug.durationWithdrawal.months > 6";

        assertFilter(userHasTakenADTFor6MoreMonths, users.get(1), true);
    }

    @Test
    protected void ableToFindOneInListType_FailCase() {
        final String userHasTakenADTFor10MoreMonths =
            "ANY $userDrug IN userDrugs satisfies $userDrug.name == 'afatinib' AND $userDrug.durationWithdrawal.days > 300";

        assertFilter(userHasTakenADTFor10MoreMonths, users.get(1), false);
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
        assertFilter(criteria_1, users.get(1), true);

        /**
         * The patient must have received definitive local therapy for prostate cancer,
         * consisting of either radiation therapy
         * and/or prostatectomy (salvage or adjuvant radiation post-prostatectomy is not exclusionary)
         */
        final String criteria_2 =
            "(ANY $userSurgery IN userSurgeries satisfies $userSurgery.name = 'prostatectomy' AND $userSurgery.purpose = 'radical' ) " +
            " OR " +
            "(ANY $userEBRT IN userEBRTs satisfies $userEBRT.target contains 'prostatectomy' AND $userSurgery.purpose = 'radical' )";
        assertFilter(criteria_2, users.get(1), true);
    }

    /**
     * NCT test cases
     */
    @Test
    protected void nctTestCases() {
        ArrayList<ArrayList> inclusionCriteria = new ArrayList();
        ArrayList<String> ic;
        ArrayList<ArrayList> exclusionCriteria = new ArrayList();
        ArrayList<String> ec;

        /**
         * NCT03503097
         */
        ic = new ArrayList();
        ic.add("any $userTNM in userTNMs satisfies $userTNM.primaryCancer == 'prostate cancer' and $userTNM.n_StagePrimary == 'N1' and $userTNM.m_StagePrimary == 'M1'");
        inclusionCriteria.add(ic);
        ec = new ArrayList();
        exclusionCriteria.add(ec);

        /**
         * NCT04472338
         */
        ic = new ArrayList();
        ic.add("userDemographics.age >= 40");
        inclusionCriteria.add(ic);
        ec = new ArrayList();
        //ec.add("any $userCondition in userConditions satisfies $userCondition.name == 'prostate cancer'");
        exclusionCriteria.add(ec);

        /**
         * NCT04336943
         */
        ic = new ArrayList();
        ic.add("any $userCondition in userConditions satisfies $userCondition.name == 'prostate cancer'");
        ic.add("(any $userSurgery in userSurgeries satisfies $userSurgery.name == 'prostatectomy' and $userSurgery.purpose == 'radical')" +
                " OR (any $userEBRT in userEBRTs satisfies $userEBRT.target contains 'prostate' and $userEBRT.purpose == 'radical')");
        ic.add("(" +
                "(any $userEBRT in userEBRTs satisfies $userEBRT.target contains 'prostate' and $userEBRT.purpose == 'radical')" +
                " AND (any $userLab in userLabs satisfies $userLab.test == 'TPSA' and $userLab.level >= 2 and $userLab.isLatest == true)" +
                " AND (userPSADoublingTime.months <= 10)" +
                ")" +
                " OR (any $userSurgery in userSurgeries satisfies $userSurgery.name == 'prostatectomy')");
        ic.add("(none $userDrug in userDrugs satisfies $userDrug.type contains 'ADT' and $userDrug.purpose contains 'adjuvant_ebrt')" +
                " OR (none $userDrug in userDrugs satisfies $userDrug.type contains 'ADT' and $userDrug.purpose contains 'adjuvant_ebrt' and $userDrug.durationWithdrawal.months < 6)");
        ic.add("userDemographics.age > 18");
        ic.add("userLifeExpectancy.weeks >= 16");
        inclusionCriteria.add(ic);

        ec = new ArrayList();
        /**
         * TODO "and not *any*" logic is wrong
         * Prior chemotherapy for prostate cancer, unless done in the neoadjuvant setting, and if the last dose was > 6 months prior to enrollment
         */
        ec.add("any $userDrug in userDrugs satisfies $userDrug.disease == 'prostate cancer' and $userDrug.type contains 'chemotherapy'" +
                " AND (NOT (any $userDrug in userDrugs satisfies $userDrug.type contains 'chemotherapy' and $userDrug.purpose contains 'neoadjuvant' and $userDrug.durationWithdrawal.months > 6))");
        ec.add("any $userDrug in userDrugs satisfies $userDrug.type contains 'PD1' or $userDrug.type contains 'PD-L1'");
        ec.add("any $userDrug in userDrugs satisfies $userDrug.type contains 'PARP'");
        exclusionCriteria.add(ec);

        for (int i = 0; i < inclusionCriteria.size(); i ++) {
            UserProfileConditionMetadata user = users.get(i);
            ic = inclusionCriteria.get(i);
            ec = exclusionCriteria.get(i);

            System.out.println("Testing inclusion criteria");
            for (int j = 0; j < ic.size(); j ++) {
                System.out.println(ic.get(j));
                assertFilter(ic.get(j), user, true);
            }

            System.out.println("Testing exclusion criteria");
            for (int j = 0; j < ec.size(); j ++) {
                System.out.println(ec.get(j));
                assertFilter(ec.get(j), user, false);
            }
        }
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


