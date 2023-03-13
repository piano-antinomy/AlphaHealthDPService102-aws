package com.alpha.health.dp.core.lambda.sql.transformer;

import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joo.libra.PredicateContext;
import org.joo.libra.sql.SqlPredicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LibraBackedSqlTransformerTest {
    final DemoMockUserFactory userFactory = new DemoMockUserFactory();
    final String userDataPath = "data/users/";
    ArrayList<UserProfileConditionMetadata> users;

    @BeforeEach
    protected void setup() {
        // load user data from json files
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        users = new ArrayList();
        File directory = new File(userDataPath);
        if (directory.isDirectory()) {
            for (int i = 1; i <= directory.listFiles().length; i ++) {
                try {
                    String json = new String(Files.readAllBytes(Paths.get(userDataPath + Integer.toString(i) + ".json")));
                    users.add(objectMapper.readValue(json, UserProfileConditionMetadata.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // preprocessing to populate
        for (UserProfileConditionMetadata user : users) {
            user.populateDerivedFields();
        }
    }

    /*
    @Test
    protected void test () {
        userFactory.getMockUser();
    }
    */

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
            "(ANY $userRadiotherapy IN userRadiotherapies satisfies $userRadiotherapy.target contains 'prostate')";
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
         * NCT04472338
         */
        ic = new ArrayList();
        ic.add("userDemographics.age >= 40");
        inclusionCriteria.add(ic);
        ec = new ArrayList();
        ec.add("any $userCondition in userConditions satisfies $userCondition.name == 'prostate cancer'");
        exclusionCriteria.add(ec);

        /**
         * NCT03503097
         */
        ic = new ArrayList();
        ic.add("any $userTNM in userTNMs satisfies $userTNM.primaryCancer == 'prostate cancer' and $userTNM.n_StagePrimary == 'N1' and $userTNM.m_StagePrimary == 'M1'");
        inclusionCriteria.add(ic);
        ec = new ArrayList();
        exclusionCriteria.add(ec);

        /**
         * NCT04336943
         */
        ic = new ArrayList();
        ic.add("any $userCondition in userConditions satisfies $userCondition.name == 'prostate cancer'");
        ic.add("(any $userSurgery in userSurgeries satisfies $userSurgery.name == 'prostatectomy' and $userSurgery.purpose == 'radical')" +
                " OR (any $userRadiotherapy in userRadiotherapies satisfies $userRadiotherapy.name == 'EBRT' and  $userRadiotherapy.target contains 'prostate' and $userRadiotherapy.purpose == 'radical')");
        ic.add("(" +
                "(any $userRadiotherapy in userRadiotherapies satisfies $userRadiotherapy.name == 'EBRT' and $userRadiotherapy.target contains 'prostate' and $userRadiotherapy.purpose == 'radical')" +
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
        ec.add("any $userDrug in userDrugs satisfies" +
                " ($userDrug.disease == 'prostate cancer' and $userDrug.type contains 'chemotherapy')" +
                " and (not ($userDrug.purpose contains 'neoadjuvant' and $userDrug.durationWithdrawal.months > 6))");
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


