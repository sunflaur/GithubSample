package com.yeongsoo.githubsample.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by yeongsookim on 2018-05-13.
 */

public class CollectionUtils {
    public static boolean isEmpty(Collection<?> list) {
        return size(list) == 0;
    }

    public static int size(Collection<?> list) {
        int size = 0;

        if (list != null) {
            size = list.size();
        }

        return size;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return size(map) == 0;
    }

    public static int size(Map<?, ?> map) {
        int size = 0;

        if (map != null) {
            size = map.size();
        }

        return size;
    }
}
