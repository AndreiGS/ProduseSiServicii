package com.findthebusiness.backend.utils;

public class ScheduleDelayTimes {

    private static final long oneDay = 1000*60*60*24L;
    public static final long UNPUBLISH_SHOPS = oneDay;
    public static final long DELETE_OLD_AUTHS = oneDay*7L;
    public static final long MAKE_SHOPS_NOT_PROMOTED = oneDay;

}
