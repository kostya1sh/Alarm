package com.test.alarm.db;

/**
 * Created by kostya on 26.09.2016.
 */

public class DBSchema {
    public static final class AlarmTable {
        public static final String NAME = "alarm_table";

        public static final class Cols {
            public static final String ID = "id";
            public static final String DATE = "date";
            public static final String MSG = "msg";
            public static final String ACTIVATED = "activated";
            public static final String TYPE = "type";
            public static final String DIFFICULT= "difficult";
        }
    }

}
