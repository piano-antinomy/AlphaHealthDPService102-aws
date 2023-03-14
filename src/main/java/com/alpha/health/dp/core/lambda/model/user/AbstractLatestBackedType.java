package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.joda.time.DateTime;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractLatestBackedType {
    @Setter
    @Getter
    private Boolean isLatest;

    @Getter
    private DateTime date;

    public String getSortKey() {
        throw new IllegalStateException("This method must be overridden");
    }

}
