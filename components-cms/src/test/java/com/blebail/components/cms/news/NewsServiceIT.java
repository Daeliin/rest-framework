package com.blebail.components.cms.news;

import com.blebail.components.cms.credentials.account.Account;
import com.blebail.components.cms.fixtures.JavaFixtures;
import com.blebail.components.cms.library.AccountLibrary;
import com.blebail.components.cms.library.NewsLibrary;
import com.blebail.components.core.string.UrlFriendlyString;
import com.blebail.components.test.rule.DbFixture;
import com.blebail.components.test.rule.DbMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class NewsServiceIT {

    @Inject
    private NewsService tested;

    @RegisterExtension
    public static DbMemory dbMemory = new DbMemory();

    @RegisterExtension
    public DbFixture dbFixture = new DbFixture(dbMemory,
        sequenceOf(
            JavaFixtures.account(),
            JavaFixtures.news()
        )
    );

    @Test
    public void shouldFindNews() {
        dbFixture.noRollback();

        News news = NewsLibrary.publishedNews();
        News foundNews = tested.findOne(news.id());

        assertThat(foundNews).isEqualTo(foundNews);
    }

    @Test
    public void shouldThrowException_whenfindingANewsThatDoesntExist() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.findOne("nonExistingId"));
    }

    @Test
    public void shouldCheckThatNewsDoesntExist() {
        dbFixture.noRollback();

        assertThat(tested.exists("ZFZEF-ZEF")).isFalse();
    }

    @Test
    public void shouldCheckThatNewsExists() {
        dbFixture.noRollback();

        assertThat(tested.exists(NewsLibrary.publishedNews().id())).isTrue();
    }

    @Test
    public void shouldThrowException_whenCreatingNewsWithNonExistingAuthor() {
        dbFixture.noRollback();

        News news = new News(
                "ARTICLE1",
                LocalDateTime.of(2016, 5, 20, 14, 30, 0).toInstant(ZoneOffset.UTC),
                "AANWN",
                "Welcome to sample",
                "welcome-to-sample",
                "Today is the day we start sample.com",
                "Today is the day we start sample.com",
                "We open our door today, you'll find content very soon.",
                "https://google.fr",
                LocalDateTime.of(2016, 5, 20, 15, 30, 0).toInstant(ZoneOffset.UTC),
                NewsStatus.DRAFT);

        assertThrows(NoSuchElementException.class, () -> tested.create(news));
    }

    @Test
    public void shouldCreateNews() {
        News news = new News(
                " ",
                LocalDateTime.of(2016, 5, 20, 14, 30, 0).toInstant(ZoneOffset.UTC),
                AccountLibrary.admin().id(),
                "Welcome to sample",
                "welcome-to-sample",
                "Today is the day we start sample.com",
                "Today is the day we start sample.com",
                "We open our door today, you'll find content very soon.",
                "https://google.fr",
                LocalDateTime.of(2016, 5, 20, 15, 30, 0).toInstant(ZoneOffset.UTC),
                NewsStatus.DRAFT);

        News createdNews = tested.create(news);

        assertThat(createdNews.id()).isNotBlank();
        assertThat(createdNews.title).isEqualTo(news.title);
        assertThat(createdNews.urlFriendlyTitle).isEqualTo(new UrlFriendlyString(news.title).value);
        assertThat(createdNews.description).isEqualTo(news.description);
        assertThat(createdNews.content).isEqualTo(news.content);
        assertThat(createdNews.source).isEqualTo(news.source);
        assertThat(createdNews.authorId).isEqualTo(news.authorId);
        assertThat(createdNews.publicationDate).isNull();
        assertThat(createdNews.status).isEqualTo(NewsStatus.DRAFT);
    }

    @Test
    public void shouldThrowException_whenUpdatingNonExistingNews() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.update((News) null));
    }

    @Test
    public void shouldThrowException_whenUpdatingValidatedNews() {
        dbFixture.noRollback();

        assertThrows(IllegalStateException.class, () -> tested.update(NewsLibrary.validatedNews()));
    }

    @Test
    public void shouldThrowException_whenUpdatingPublishedNews() {
        dbFixture.noRollback();

        assertThrows(IllegalStateException.class, () -> tested.update(NewsLibrary.publishedNews()));
    }

    @Test
    public void shouldUpdateANewsTitleDescriptionContentAndSource() {
        News newsToUpdate = NewsLibrary.draftNews();
        News news = new News(newsToUpdate.id(), Instant.now(), "", "New title", "", "New desc", "New content", "New content rendered",
                "https://google.fr", null, NewsStatus.PUBLISHED);

        News updatedArtice = tested.update(news);

        assertThat(updatedArtice.status).isEqualTo(NewsStatus.DRAFT);
        assertThat(updatedArtice.title).isEqualTo(news.title);
        assertThat(updatedArtice.urlFriendlyTitle).isEqualTo(new UrlFriendlyString(news.title).value);
        assertThat(updatedArtice.description).isEqualTo(news.description);
        assertThat(updatedArtice.content).isEqualTo(news.content);
        assertThat(updatedArtice.renderedContent).isEqualTo(news.renderedContent);
        assertThat(updatedArtice.source).isEqualTo(news.source);
        assertThat(updatedArtice.id()).isEqualTo(newsToUpdate.id());
        assertThat(updatedArtice.creationDate()).isEqualTo(newsToUpdate.creationDate());
        assertThat(updatedArtice.authorId).isEqualTo(newsToUpdate.authorId);
    }

    @Test
    public void shouldThrowException_whenMarkingANonExistingNewsAsDraft() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.markAs("nonExistingId", NewsStatus.DRAFT));
    }

    @Test
    public void shouldMarkANewsAsDraft() {
        News news = tested.markAs(NewsLibrary.validatedNews().id(), NewsStatus.DRAFT);

        assertThat(news.status).isEqualTo(NewsStatus.DRAFT);
        assertThat(news.publicationDate).isNull();
    }

    @Test
    public void shouldThrowException_whenMArkingANonExistingNewsAsValidated() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.markAs("nonExistingId", NewsStatus.VALIDATED));
    }

    @Test
    public void shouldMarkANewsAsValidated() {
        News news = tested.markAs(NewsLibrary.draftNews().id(), NewsStatus.VALIDATED);

        assertThat(news.status).isEqualTo(NewsStatus.VALIDATED);
        assertThat(news.publicationDate).isNull();
    }

    @Test
    public void shouldThrowException_whenMarkingANonExistingNewsAsPublished() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.markAs("nonExistingId", NewsStatus.PUBLISHED));
    }

    @Test
    public void shouldMarkANewsAsPublished() {
        News news = tested.markAs(NewsLibrary.validatedNews().id(), NewsStatus.PUBLISHED);

        assertThat(news.status).isEqualTo(NewsStatus.PUBLISHED);
        assertThat(news.publicationDate).isNotNull();
    }

    @Test
    public void shouldFindAuthorByNews() {
        dbFixture.noRollback();

        List<String> news = Arrays.asList(NewsLibrary.publishedNews().id(), NewsLibrary.validatedNews().id());

        Map<News, Account> authorByNews = tested.authorByNews(news);

        assertThat(authorByNews.get(NewsLibrary.publishedNews())).isEqualTo(AccountLibrary.admin());
        assertThat(authorByNews.get(NewsLibrary.validatedNews())).isEqualTo(AccountLibrary.john());
    }
}
