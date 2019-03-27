package com.luisgaviria.callcenter.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * Class to test the model Call
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class CallTest {

    private static final Long MIN_DURATION_CALL = 5L;
    private static final Long MAX_DURATION_CALL = 10L;

    /**
     * Test to create a call of the correct way and validate duration set.
     */
    @Test
    public void createCallOkTest() {
        Call call = new Call(MIN_DURATION_CALL, MAX_DURATION_CALL);

        Assert.assertNotNull(call);
        Assert.assertTrue(call.getDuration() <= MAX_DURATION_CALL && call.getDuration() >= MIN_DURATION_CALL);
    }

    /**
     * Test to try to create a call with invalid parameters.
     * Case 1. Both negative values.
     * Case 2. Order of parameters invalid.
     * Case 3. Not null parameters.
     */
    @Test
    public void createCallWithIncorrectValuesInParameters() {
        Call call;
        // Case 1
        try {
            call = new Call(-1L, -1L);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The validated expression is false", e.getMessage());
        }
        // Case 2
        try {
            call = new Call(MAX_DURATION_CALL, MIN_DURATION_CALL);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The validated expression is false", e.getMessage());
        }
        // Case 3
        try {
            call = new Call(null, null);
        } catch (NullPointerException e) {
            Assert.assertEquals("The validated object is null", e.getMessage());
        }
    }
}
