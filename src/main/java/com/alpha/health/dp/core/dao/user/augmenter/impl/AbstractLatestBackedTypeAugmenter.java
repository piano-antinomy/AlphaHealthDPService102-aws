package com.alpha.health.dp.core.dao.user.augmenter.impl;

import com.alpha.health.dp.core.dao.user.augmenter.api.UserMetadataAugmenter;
import com.alpha.health.dp.core.lambda.model.user.AbstractLatestBackedType;
import com.alpha.health.dp.core.lambda.model.user.UserBiopsy;
import com.alpha.health.dp.core.lambda.model.user.UserLab;
import com.alpha.health.dp.core.lambda.model.user.UserProfileConditionMetadata;
import com.alpha.health.dp.core.lambda.model.user.UserTNM;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * responsible for all types that extends AbstractLatestBackedType.
 * sort based on getSortKey()
 */
public class AbstractLatestBackedTypeAugmenter implements UserMetadataAugmenter {
    final Class classType;
    public AbstractLatestBackedTypeAugmenter(Class classType) {
        this.classType = classType;
    }
    @Override
    public UserProfileConditionMetadata augment(UserProfileConditionMetadata originalMetadata) {
        UserProfileConditionMetadata newMetadata = originalMetadata;

        Map<String, Queue<DateTime>> map = new HashMap<>();

        List<AbstractLatestBackedType> abstractTypes;

        if (UserBiopsy.class.equals(classType)) {
            abstractTypes =
                new ArrayList<>(originalMetadata.getUserBiopsies());
        } else if (UserLab.class.equals(classType)) {
            abstractTypes =
                new ArrayList<>(originalMetadata.getUserLabs());
        } else if (UserTNM.class.equals(classType)) {
            abstractTypes =
                new ArrayList<>(originalMetadata.getUserTNMs());
        } else {
            throw new IllegalStateException(classType + " not found!");
        }


        abstractTypes.forEach(b -> {
            b.setIsLatest(false);
            Queue<DateTime> heap = map.getOrDefault(b, new PriorityQueue<>(Collections.reverseOrder()));
            heap.add(b.getDate());
            map.put(b.getSortKey(), heap);
        });

        abstractTypes.forEach(b -> {
            if (map.get(b.getSortKey()).peek().equals(b.getDate())) {
                b.setIsLatest(true);
            }
        });

        System.out.println(abstractTypes);

        if (UserBiopsy.class.equals(classType)) {
            List<UserBiopsy> biopsies = abstractTypes
                .stream().map( t -> (UserBiopsy) t).collect(Collectors.toList());
            newMetadata.setUserBiopsies(biopsies);
        } else if (UserLab.class.equals(classType)) {
            List<UserLab> userLabs = abstractTypes
                .stream().map( t -> (UserLab) t).collect(Collectors.toList());
            newMetadata.setUserLabs(userLabs);
        } else if (UserTNM.class.equals(classType)) {
            List<UserTNM> userTNMS = abstractTypes
                .stream().map( t -> (UserTNM) t).collect(Collectors.toList());
            newMetadata.setUserTNMs(userTNMS);
        } else {
            throw new IllegalStateException(classType + " not found!");
        }

        return newMetadata;
    }
}
