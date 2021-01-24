package com.findthebusiness.backend.utils;

public class ScheduleDelayTimes {

    private static final long ONE_DAY = 1000*60*60*24L;
    public static final long UNPUBLISH_SHOPS = ONE_DAY;
    public static final long DELETE_OLD_AUTHS = ONE_DAY *7L;
    public static final long MAKE_SHOPS_NOT_PROMOTED = ONE_DAY;
    public static final long GENERATE_SITEMAPS = ONE_DAY;

}
