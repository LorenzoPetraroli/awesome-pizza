package com.awesomepizza.application.service;

import java.util.Locale;
import java.util.UUID;

final class OrderCodeGenerator {

    private OrderCodeGenerator() {
    }

    static String nextCode() {
        return "ORD-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase(Locale.ROOT);
    }
}
