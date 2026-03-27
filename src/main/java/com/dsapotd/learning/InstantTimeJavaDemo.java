package com.dsapotd.learning;

import java.time.Instant;
import java.time.ZonedDateTime;

public class InstantTimeJavaDemo {
    public static void main(String[] args) {
        Instant now = Instant.now();

        ZonedDateTime zonedDateTime = now.atZone(java.time.ZoneId.systemDefault());
        System.out.println("Current time: " + now);
        System.out.println("Current time in system default zone: " + zonedDateTime.getHour() + ":" + zonedDateTime.getMinute() + ":" + zonedDateTime.getSecond());
    }
}