package com.blebail.components.cms.credentials.account;

import com.blebail.components.cms.credentials.permission.Permission;
import com.blebail.components.cms.credentials.permission.PermissionService;
import com.blebail.components.cms.fixtures.AccountRows;
import com.blebail.components.cms.sql.BAccount;
import com.blebail.components.cms.sql.QAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public final class AccountServiceTest {

    private AccountRepository accountRepositoryMock;
    private PermissionService permissionServiceMock;
    private AccountService tested;

    @BeforeEach
    public void setUp() {
        accountRepositoryMock = mock(AccountRepository.class);
        permissionServiceMock = mock(PermissionService.class);

        tested = new AccountService(accountRepositoryMock, permissionServiceMock);
    }

    @Test
    public void shouldHitTheDatabase_whenLoadingAUserForTheFirstTime() {
        BAccount account = AccountRows.admin();;
        Permission permission = new Permission("admin", Instant.now(), "ADMIN");

        doReturn(List.of(account)).when(accountRepositoryMock).find(QAccount.account.username.equalsIgnoreCase(account.getUsername())
                .and(QAccount.account.enabled.isTrue()));

        doReturn(Set.of(permission)).when(permissionServiceMock).findForAccount(account.getId());

        UserDetails tomsearle = tested.loadUserByUsername(account.getUsername());

        assertThat(tomsearle).isNotNull();
        assertThat(tomsearle.getUsername()).isEqualTo(account.getUsername());
        assertThat(tomsearle.getAuthorities()).extracting(GrantedAuthority::getAuthority).containsOnly("ROLE_admin");

        verify(accountRepositoryMock).find(QAccount.account.username.equalsIgnoreCase(account.getUsername())
                .and(QAccount.account.enabled.isTrue()));
        verify(permissionServiceMock).findForAccount(account.getId());
    }

    @Test
    public void shouldNotHitTheDatabaseMoreThanOnce_whenUserAlreadyLoaded() {
        BAccount account = AccountRows.admin();;
        Permission permission = new Permission("admin", Instant.now(), "ADMIN");

        doReturn(List.of(account)).when(accountRepositoryMock).find(QAccount.account.username.equalsIgnoreCase(account.getUsername())
                .and(QAccount.account.enabled.isTrue()));

        doReturn(Set.of(permission)).when(permissionServiceMock).findForAccount(account.getId());

        tested.loadUserByUsername(account.getUsername());
        UserDetails tomsearle = tested.loadUserByUsername(account.getUsername());

        assertThat(tomsearle).isNotNull();
        assertThat(tomsearle.getUsername()).isEqualTo(account.getUsername());
        assertThat(tomsearle.getAuthorities()).extracting(GrantedAuthority::getAuthority).containsOnly("ROLE_admin");

        verify(accountRepositoryMock, times(1)).find(QAccount.account.username.equalsIgnoreCase(account.getUsername())
                .and(QAccount.account.enabled.isTrue()));
        verify(permissionServiceMock, times(1)).findForAccount(account.getId());
    }

    @Test
    public void shouldHitTheDatabaseAgain_whenLoadingAUserThatWasEvictedFromCache() {
        BAccount account = AccountRows.admin();;
        Permission permission = new Permission("admin", Instant.now(), "ADMIN");

        doReturn(List.of(account)).when(accountRepositoryMock).find(QAccount.account.username.equalsIgnoreCase(account.getUsername())
                .and(QAccount.account.enabled.isTrue()));

        doReturn(Set.of(permission)).when(permissionServiceMock).findForAccount(account.getId());

        tested.loadUserByUsername(account.getUsername());
        tested.invalidateCache();
        UserDetails tomsearle = tested.loadUserByUsername(account.getUsername());

        assertThat(tomsearle).isNotNull();
        assertThat(tomsearle.getUsername()).isEqualTo(account.getUsername());
        assertThat(tomsearle.getAuthorities()).extracting(GrantedAuthority::getAuthority).containsOnly("ROLE_admin");

        verify(accountRepositoryMock, times(2)).find(QAccount.account.username.equalsIgnoreCase(account.getUsername())
                .and(QAccount.account.enabled.isTrue()));
        verify(permissionServiceMock, times(2)).findForAccount(account.getId());
    }
}