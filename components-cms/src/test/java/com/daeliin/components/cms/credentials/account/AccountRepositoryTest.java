package com.daeliin.components.cms.credentials.account;

import com.daeliin.components.cms.Application;
import com.daeliin.components.persistence.resource.repository.ResourceRepository;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = Application.class)
public class AccountRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Inject
    private AccountRepository accountRepository;

    @Test
    public void shouldExtendResourceRepository() {
        assertThat(accountRepository.getClass().getSuperclass().getClass()).isEqualTo(ResourceRepository.class.getClass());
    }
}