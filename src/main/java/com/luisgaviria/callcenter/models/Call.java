package com.luisgaviria.callcenter.models;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to model a Call
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class Call {

    // Duration for this call in seconds
    private Long duration;

    /**
     * Builds a new random call
     *
     * @param minDuration minimum duration in seconds for call
     * @param maxDuration max duration in seconds for call
     * @return A call with a duration random
     */
    public Call(Long minDuration, Long maxDuration) {
        Validate.notNull(minDuration);
        Validate.notNull(maxDuration);
        Validate.isTrue(maxDuration >= minDuration && minDuration >= 0);
        this.duration = getRandomDuration(minDuration, maxDuration);
    }

    /**
     * Create a new call list with random duration
     *
     * @param listSize    size of new call list
     * @param minDuration minimum duration in seconds for call
     * @param maxDuration max duration in seconds for call
     * @return List<Call>, list of calls
     */
    public static List<Call> buildListOfRandomCalls(Long listSize, Long minDuration, Long maxDuration) {
        Validate.isTrue(listSize > 0);
        List<Call> callList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            callList.add(new Call(minDuration, maxDuration));
        }
        return callList;
    }

    /**
     * Set duration time of call
     *
     * @param minDuration Long, min duration of the call in seconds
     * @param maxDuration Long, max duration of the call in seconds
     */
    private Long getRandomDuration(Long minDuration, Long maxDuration) {
        return ThreadLocalRandom.current().nextLong(minDuration, maxDuration);
    }

    public Long getDuration() {
        return duration;
    }
}
