package com.daeliin.components.cms.library;

import com.daeliin.components.cms.news.News;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class NewsLibrary {

    public static News publishedNews() {
        return new News(
                "NEWS1",
                LocalDateTime.of(2016, 5, 20, 14, 30, 0).toInstant(ZoneOffset.UTC),
                "ACCOUNT1",
                "Welcome to sample",
                "welcome-to-sample",
                "Today is the day we start sample.com",
                "We open our door today, you'll find content very soon.",
                "https://google.fr",
                LocalDateTime.of(2016, 5, 20, 15, 30, 0).toInstant(ZoneOffset.UTC),
                true);
    }

    public static News notPublishedNews() {
        return new News(
                "NEWS2",
                LocalDateTime.of(2016, 5, 21, 14, 30, 0).toInstant(ZoneOffset.UTC),
                "ACCOUNT2",
                "Sample is live",
                "sample-is-live",
                "Today is the day we go live at sample.com",
                "We go live today, here''s our first content.",
                null,
                null,
                false);
    }
}