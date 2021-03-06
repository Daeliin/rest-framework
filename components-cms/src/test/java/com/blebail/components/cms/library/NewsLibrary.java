package com.blebail.components.cms.library;

import com.blebail.components.cms.news.News;
import com.blebail.components.cms.publication.PublicationStatus;
import com.blebail.components.cms.sql.BNews;

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
                "We open our door today, you'll find content very soon.",
                "https://google.fr",
                LocalDateTime.of(2016, 5, 20, 15, 30, 0).toInstant(ZoneOffset.UTC),
                PublicationStatus.PUBLISHED);
    }

    public static BNews publishedNewsRow() {
        News publishedNews = publishedNews();

        return new BNews(
                publishedNews.authorId,
                publishedNews.content,
                publishedNews.creationDate(),
                publishedNews.description,
                publishedNews.id(),
                publishedNews.publicationDate,
                publishedNews.renderedContent,
                publishedNews.source,
                publishedNews.status.name(),
                publishedNews.title,
                publishedNews.urlFriendlyTitle
        );
    }

    public static News validatedNews() {
        return new News(
                "NEWS2",
                LocalDateTime.of(2016, 5, 21, 14, 30, 0).toInstant(ZoneOffset.UTC),
                "ACCOUNT2",
                "Sample is live",
                "sample-is-live",
                "Today is the day we go live at sample.com",
                "We go live today, here''s our first content.",
                "We go live today, here''s our first content.",
                null,
                null,
                PublicationStatus.VALIDATED);
    }

    public static BNews validatedNewsRow() {
        News validatedNews = validatedNews();

        return new BNews(
                validatedNews.authorId,
                validatedNews.content,
                validatedNews.creationDate(),
                validatedNews.description,
                validatedNews.id(),
                validatedNews.publicationDate,
                validatedNews.renderedContent,
                validatedNews.source,
                validatedNews.status.name(),
                validatedNews.title,
                validatedNews.urlFriendlyTitle
        );
    }

    public static News draftNews() {
        return new News(
            "NEWS3",
            LocalDateTime.of(2016, 5, 21, 14, 30, 0).toInstant(ZoneOffset.UTC),
            "ACCOUNT2",
            "Sample.com is in beta",
            "sample-is-in-beta",
            "Today is the day we go beta at sample.com",
            "We go beta today.",
            "We go beta today.",
            null,
            null,
            PublicationStatus.DRAFT);
    }

    public static BNews draftNewsRow() {
        News draftNews = draftNews();

        return new BNews(
                draftNews.authorId,
                draftNews.content,
                draftNews.creationDate(),
                draftNews.description,
                draftNews.id(),
                draftNews.publicationDate,
                draftNews.renderedContent,
                draftNews.source,
                draftNews.status.name(),
                draftNews.title,
                draftNews.urlFriendlyTitle
        );
    }
}
