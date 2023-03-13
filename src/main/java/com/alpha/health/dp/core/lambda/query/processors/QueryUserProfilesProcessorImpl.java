package com.alpha.health.dp.core.lambda.query.processors;

import com.alpha.health.dp.core.dao.api.DPDataAccess;
import com.alpha.health.dp.core.dao.impl.S3BackedDPDataAccessImpl;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.QueryUserProfilesProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.QueryUserProfilesProcessorResponse;

public class QueryUserProfilesProcessorImpl implements ServiceRequestProcessor{

    final DPDataAccess dpDataAccess = new S3BackedDPDataAccessImpl();
    @Override
    public QueryUserProfilesProcessorResponse process(ProcessorRequest request) {
        if (!(request instanceof QueryUserProfilesProcessorRequest)) {
            throw new IllegalArgumentException("Processor request has an invalid type " + request.getClass());
        }

        final QueryUserProfilesProcessorRequest queryUserProfilesProcessorRequest =
            (QueryUserProfilesProcessorRequest) request;

        return QueryUserProfilesProcessorResponse
            .builder()
            .userProfileConditionMetadata(
                dpDataAccess.getUser(
                    queryUserProfilesProcessorRequest.getUserProfileId()))
            .build();
    }
}
