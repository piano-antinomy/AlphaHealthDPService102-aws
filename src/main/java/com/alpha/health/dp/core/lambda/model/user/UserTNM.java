package com.alpha.health.dp.core.lambda.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTNM {
    private String primaryCancer;
    private DateTime date;
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
    private Boolean isLatest;
}
