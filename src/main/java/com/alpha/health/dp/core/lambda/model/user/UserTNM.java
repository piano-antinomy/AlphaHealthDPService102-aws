package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTNM {
    public String primaryCancer;
    public DateTime dateOfStaging;
    public String t_StagePrimary;
    public String t_StageSecondary;
    public String t_Method;
    public String n_StagePrimary;
    public String n_StageSecondary;
    public String n_Method;
    public String m_StagePrimary;
    public String m_StageSecondary;
    public String m_Method;
}
