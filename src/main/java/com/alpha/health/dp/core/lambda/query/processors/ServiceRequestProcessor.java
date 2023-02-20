package com.alpha.health.dp.core.lambda.query.processors;

import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorResponse;

public interface ServiceRequestProcessor {
    ProcessorResponse process(ProcessorRequest request);
}
