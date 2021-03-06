package com.blebail.components.cms.credentials.account;

import com.blebail.components.cms.library.AccountLibrary;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AccountTest {

    @Test
    public void shouldThrowExeption_whenUsernameIsNull() {
        assertThrows(Exception.class, () -> new Account("ACCOUNT4", Instant.now(), null, "user@email.org", true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e"));
    }

    @Test
    public void shouldAssignAnUsername() {
        Account account = new Account("ACCOUNT4", Instant.now(), "user", "user@email.org", true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e");

        assertThat(account.username).isEqualTo("user");
    }

    @Test
    public void shouldThrowExeption_whenEmailIsNull() {
        assertThrows(Exception.class, () -> new Account("ACCOUNT4", Instant.now(), "user", null, true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e"));
    }

    @Test
    public void shouldAssignAnEmail() {
        Account account = new Account("ACCOUNT4", Instant.now(), "user", "user@email.org", true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e");

        assertThat(account.email).isEqualTo("user@email.org");
    }

    @Test
    public void shouldThrowExeption_whenPasswordIsNull() {
        assertThrows(Exception.class, () -> new Account("ACCOUNT4", Instant.now(), "user", "user@email.org", true,
                null,
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e"));
    }

    @Test
    public void shouldAssignAPassword() {
        Account account = new Account("ACCOUNT4", Instant.now(), "user", "user@email.org", true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e");

        assertThat(account.password).isEqualTo("$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6");
    }

    @Test
    public void shouldThrowExeption_whenTokenIsNull() {
        assertThrows(Exception.class, () -> new Account("ACCOUNT4", Instant.now(), "user", "user@email.org", true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                null));
    }

    @Test
    public void shouldAssignAToken() {
        Account account = new Account("ACCOUNT4", Instant.now(), "user", "user@email.org", true,
                "$2a$10$ggIHKT/gYkYk0Bt2yP4xvOybahPn7GfSwC0T3fYhCzrZ9ta9LAYt6",
                "b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e");

        assertThat(account.token).isEqualTo("b5e655641f1d05a415d5ea30d4fd25dcd03ea4a187c5d121d221454c03770f9f98101c206878b25697a79c924149da6802af8e97fbed4999c0dd838577590d9e");
    }

    @Test
    public void shouldPrintItsIdEmailUsernameAndEnabled() {
        Account account = AccountLibrary.admin();

        assertThat(account.toString()).contains(
                account.id(),
                account.email,
                account.username,
                String.valueOf(account.enabled));
    }

    @Test
    public void shouldBeComparedOnUsernames() {
        Account account1 = AccountLibrary.admin();
        Account account2 = AccountLibrary.john();

        assertThat(account1.compareTo(account2)).isNegative();
        assertThat(account2.compareTo(account1)).isPositive();
    }
}