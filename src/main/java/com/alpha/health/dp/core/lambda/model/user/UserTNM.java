package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTNM extends AbstractLatestBackedType {
    private String primaryCancer;
    private String t_StagePrimary;
    private String t_StageSecondary;
    private String t_Method;
    private String n_StagePrimary;
    private String n_StageSecondary;
    private String n_Method;
    private String m_StagePrimary;
    private String m_StageSecondary;
    private String m_Method;

    @Setter
    private Number durationDays;

    @Override
    public String getSortKey() {
        return this.primaryCancer;
    }
}
