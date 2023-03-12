package com.alpha.health.dp.core.dao.api;

import com.alpha.health.dp.core.lambda.model.trials.ClinicalTrial;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;

import java.util.List;

/**
 * DAO layer interface for data like user info and trials.
 */
public interface DPDataAccess {
    /**
     * Get UserProfileConditionMetadata by querying userProfileId.
     * @param userProfileId
     * @return
     */
    UserProfileConditionMetadata getUser(String userProfileId);

    /**
     *
     * @return all ClinicalTrials
     */
    List<ClinicalTrial> getAllClinicalTrials();
}
