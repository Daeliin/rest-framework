package com.daeliin.components.cms.membership;

import com.daeliin.components.cms.credentials.account.Account;
import com.daeliin.components.cms.credentials.account.AccountService;
import com.daeliin.components.cms.fixtures.JavaFixtures;
import com.daeliin.components.cms.library.AccountLibrary;
import com.daeliin.components.cms.sql.QAccount;
import com.daeliin.components.test.rule.DbFixture;
import com.daeliin.components.test.rule.DbMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MembershipServiceIT {

    @Inject
    private AccountService accountService;

    @Inject
    private MembershipService tested;
    
    @RegisterExtension
    public static DbMemory dbMemory = new DbMemory();

    @RegisterExtension
    public DbFixture dbFixture = new DbFixture(dbMemory, JavaFixtures.account());

    @Test
    public void shouldThrowException_whenSigningUpExistingAccount() {
        dbFixture.noRollback();

        Account existingAccount = AccountLibrary.admin();
        SignUpRequest signUpRequest = new SignUpRequest(existingAccount.username, existingAccount.email, "password");

        assertThrows(IllegalStateException.class, () -> tested.signUp(signUpRequest));
    }

    @Test
    public void shouldNotCreateAccount_whenSigningUpExistingAccount() throws Exception {
        dbFixture.noRollback();

        Account existingAccount = AccountLibrary.admin();
        SignUpRequest signUpRequest = new SignUpRequest(existingAccount.username, existingAccount.email, "password");

        int accountCountBeforeSignUp = countAccountRows();

        try {
            tested.signUp(signUpRequest);
        } catch (IllegalStateException e) {
        }

        int accountCountAfterSignUp = countAccountRows();

        assertThat(accountCountAfterSignUp).isEqualTo(accountCountBeforeSignUp);
    }

    @Test
    public void shouldThrowException_whenActivatingNonExistentAccountId() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.activate("AOADAZD-65454", "ok"));
    }

    @Test
    public void shouldThrowException_whenTokenDoesntMatchWhenActivatingAccount() {
        dbFixture.noRollback();

        Account account = AccountLibrary.inactive();

        assertThrows(IllegalArgumentException.class, () ->  tested.activate(account.id(), "wrongToken"));
    }

    @Test
    public void shouldThrowException_whenRequestingANewPasswordForNonExistingAccount() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.newPassword("AFEZAFEZ-6544"));
    }

    @Test
    public void shouldThrowException_whenResetingPasswordForNonExistingAccount() {
        dbFixture.noRollback();

        assertThrows(NoSuchElementException.class, () -> tested.resetPassword("AFEZAFEZ-6544", "token", "newPassword"));
    }

    @Test
    public void shouldThrowException_whenTokenDoesntMatchWhenResetingPassword() {
        dbFixture.noRollback();

        Account account = AccountLibrary.admin();
        assertThrows(IllegalArgumentException.class, () -> tested.resetPassword(account.id(), "wrongToken", "newPassword"));
    }

    private int countAccountRows() throws Exception {
        return dbMemory.countRows(QAccount.account.getTableName());
    }
}
