package com.awesomepizza.infrastructure.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.Clock;

@ApplicationScoped
public class TimeConfiguration {

    @Produces
    public Clock systemClock() {
        return Clock.systemUTC();
    }
}
