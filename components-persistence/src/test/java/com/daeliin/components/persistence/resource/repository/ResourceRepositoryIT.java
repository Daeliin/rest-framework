package com.daeliin.components.persistence.resource.repository;

import com.daeliin.components.persistence.fake.UuidPersistentResourceRepository;
import com.daeliin.components.persistence.fixtures.JavaFixtures;
import com.daeliin.components.persistence.fixtures.UuidPersistentResourceRows;
import com.daeliin.components.persistence.sql.BUuidPersistentResource;
import com.daeliin.components.persistence.sql.QUuidPersistentResource;
import com.daeliin.components.test.rule.DbFixture;
import com.daeliin.components.test.rule.DbMemory;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ResourceRepositoryIT {

    @Inject
    private UuidPersistentResourceRepository repository;

    @ClassRule
    public static DbMemory dbMemory = new DbMemory();

    @Rule
    public DbFixture dbFixture = new DbFixture(dbMemory, JavaFixtures.uuidPersistentResources());

    @Test
    public void shouldProvideTheIdPath() {
        dbFixture.noRollback();

        assertThat(repository.idPath()).isEqualTo(QUuidPersistentResource.uuidPersistentResource.uuid);
    }

    @Test
    public void shouldProvideTheIdMapping() {
        dbFixture.noRollback();

        assertThat(repository.idMapping()).isNotNull();
    }

    @Test(expected = Exception.class)
    public void shouldThrowException_whenPersistingNull() {
        dbFixture.noRollback();

        BUuidPersistentResource nullUuidEntity = null;

        repository.save(nullUuidEntity);
    }

    @Test
    public void shouldPersistAResource() throws Exception {
        BUuidPersistentResource newUuuidPersistentResource = new BUuidPersistentResource(Instant.now(), "label100", UUID.randomUUID().toString());
        int uuidPersistentResourceCountBeforeCreate = countRows();

        BUuidPersistentResource persistedUuidEntity = repository.findOne(repository.save(newUuuidPersistentResource).getUuid()).get();

        int uuidPersistentResourceCountAfterCreate = countRows();

        assertThat(uuidPersistentResourceCountAfterCreate).isEqualTo(uuidPersistentResourceCountBeforeCreate + 1);
        assertThat(persistedUuidEntity).isEqualToComparingFieldByField(newUuuidPersistentResource);
    }

    @Test
    public void shouldReturnThePersistedResource() {
        BUuidPersistentResource newUuuidPersistentResource = new BUuidPersistentResource(Instant.now(), "label100", UUID.randomUUID().toString());

        BUuidPersistentResource returnedUuidEntity = repository.save(newUuuidPersistentResource);

        assertThat(returnedUuidEntity).isEqualToComparingFieldByField(newUuuidPersistentResource);
    }

    @Test
    public void shouldPersistResources() throws Exception {
        BUuidPersistentResource newUuuidPersistentResource1 = new BUuidPersistentResource(Instant.now(), "label101", UUID.randomUUID().toString());
        BUuidPersistentResource newUuuidPersistentResource2 = new BUuidPersistentResource(Instant.now(), "label102", UUID.randomUUID().toString());
        List<BUuidPersistentResource> newUuidEntities = Arrays.asList(newUuuidPersistentResource1, newUuuidPersistentResource2);

        int uuidPersistentResourceCountBeforeCreate = countRows();

        repository.save(newUuidEntities);

        int uuidPersistentResourceCountAterCreate = countRows();

        assertThat(uuidPersistentResourceCountAterCreate).isEqualTo(uuidPersistentResourceCountBeforeCreate + newUuidEntities.size());
    }

    @Test
    public void shouldReturnThePersistedResources() {
        BUuidPersistentResource newUuuidPersistentResource1 = new BUuidPersistentResource(Instant.now(), "label101", UUID.randomUUID().toString());
        BUuidPersistentResource newUuuidPersistentResource2 = new BUuidPersistentResource(Instant.now(), "label102", UUID.randomUUID().toString());
        List<BUuidPersistentResource> newUuidEntities = Arrays.asList(newUuuidPersistentResource1, newUuuidPersistentResource2);

        Collection<BUuidPersistentResource> returnedUuidEntities = repository.save(newUuidEntities);

        assertThat(returnedUuidEntities).containsOnly(newUuuidPersistentResource1, newUuuidPersistentResource2);
    }

    @Test
    public void shouldCheckIfResourceExists() {
        dbFixture.noRollback();

        assertThat(repository.exists(UuidPersistentResourceRows.uuidPersistentResource1().getUuid())).isTrue();
    }

    @Test
    public void shouldCheckIfResourceDoesntExist_whenIdDoesntExist() {
        dbFixture.noRollback();

        assertThat(repository.exists("894984-984")).isFalse();
    }

    @Test
    public void shouldCheckIfResourceDoesntExist_whenIdIsNull() {
        dbFixture.noRollback();

        assertThat(repository.exists(null)).isFalse();
    }

    @Test
    public void shouldFindResource() {
        dbFixture.noRollback();

        BUuidPersistentResource uuidPersistentResource1 = UuidPersistentResourceRows.uuidPersistentResource1();

        BUuidPersistentResource foundUuidEntity = repository.findOne(uuidPersistentResource1.getUuid()).get();

        assertThat(foundUuidEntity).isEqualToComparingFieldByField(UuidPersistentResourceRows.uuidPersistentResource1());
    }

    @Test
    public void shouldReturnEmpty_whenFindingNonExistingResource() {
        dbFixture.noRollback();

        assertThat(repository.findOne("6846984-864684").isPresent()).isFalse();
    }

    @Test
    public void shouldReturnEmpty_whenFindingNull() {
        dbFixture.noRollback();

        String nullId = null;

        assertThat(repository.findOne(nullId).isPresent()).isFalse();
    }

    @Test
    public void shouldFindResources() {
        dbFixture.noRollback();

        List<String> uuidPersistentResourceIds = Arrays.asList(UuidPersistentResourceRows.uuidPersistentResource1().getUuid(), UuidPersistentResourceRows.uuidPersistentResource2().getUuid());
        Collection<BUuidPersistentResource> uuidEntities = repository.findAll(uuidPersistentResourceIds);

        assertThat(uuidEntities)
                .usingFieldByFieldElementComparator()
                .containsOnly(UuidPersistentResourceRows.uuidPersistentResource1(), UuidPersistentResourceRows.uuidPersistentResource2());
    }

    @Test
    public void shouldReturnNoResources_whenFindingNonExistingResources() {
        dbFixture.noRollback();

        Collection<BUuidPersistentResource> uuidEntities = repository.findAll(Arrays.asList("68464-684", "684684-444"));

        assertThat(uuidEntities).isEmpty();
    }

    @Test
    public void shouldReturnNoResources_whenFindingZeroResources() {
        dbFixture.noRollback();

        Collection<BUuidPersistentResource> uuidEntities = repository.findAll(Arrays.asList());

        assertThat(uuidEntities).isEmpty();
    }

    @Test
    public void shouldReturnNoResources_whenFindingNulls() {
        dbFixture.noRollback();

        Collection<BUuidPersistentResource> uuidEntities = repository.findAll(Arrays.asList(null, null));

        assertThat(uuidEntities).isEmpty();
    }

    @Test
    public void shouldReturnOnlyExistingResources_whenFindingEexistingAndNonExistingResources() {
        dbFixture.noRollback();

        List<String> uuidPersistentResourceIds = Arrays.asList(UuidPersistentResourceRows.uuidPersistentResource1().getUuid(), UuidPersistentResourceRows.uuidPersistentResource2().getUuid(), "646444-218");
        Collection<BUuidPersistentResource> uuidEntities = repository.findAll(uuidPersistentResourceIds);

        assertThat(uuidEntities)
                .usingFieldByFieldElementComparator()
                .containsOnly(UuidPersistentResourceRows.uuidPersistentResource1(), UuidPersistentResourceRows.uuidPersistentResource2());
    }

    @Test
    public void shouldReturnFalse_whenDeletingNonExistingResource() {
        assertThat(repository.delete("96846846-4465")).isFalse();
    }

    @Test
    public void shouldReturnFalse_whenDeletingNullId() {
        String nullId = null;

        assertThat(repository.delete(nullId)).isFalse();
    }

    @Test
    public void shouldNotDeleteAnyResources_whenDeletingNonExistingResource() throws Exception {
        int uuidPersistentResourceCountBeforeDelete = countRows();

        repository.delete("6984684-685648");

        int uuidPersistentResourceCountAfterDelete = countRows();

        assertThat(uuidPersistentResourceCountBeforeDelete).isEqualTo(uuidPersistentResourceCountAfterDelete);
    }

    @Test
    public void shouldNotDeleteAnyResources_whenDeletingNullId() throws Exception {
        String nullId = null;
        int uuidPersistentResourceCountBeforeDelete = countRows();

        repository.delete(nullId);

        int uuidPersistentResourceCountAfterDelete = countRows();

        assertThat(uuidPersistentResourceCountBeforeDelete).isEqualTo(uuidPersistentResourceCountAfterDelete);
    }

    @Test
    public void shouldDeleteAResource() throws Exception {
        int uuidPersistentResourceCountBeforeCreate = countRows();

        repository.delete(UuidPersistentResourceRows.uuidPersistentResource1().getUuid());

        int uuidPersistentResourceCountAfterCreate = countRows();

        assertThat(uuidPersistentResourceCountAfterCreate).isEqualTo(uuidPersistentResourceCountBeforeCreate - 1);
    }

    @Test
    public void shouldReturnTrue_whenDeletingResource() {
        boolean delete = repository.delete(UuidPersistentResourceRows.uuidPersistentResource1().getUuid());

        assertThat(delete).isTrue();
    }

    @Test
    public void shouldDeleteMultipleResources() throws Exception {
        List<String> uuidPersistentResourceIds = Arrays.asList(UuidPersistentResourceRows.uuidPersistentResource1().getUuid(), UuidPersistentResourceRows.uuidPersistentResource2().getUuid());
        int uuidPersistentResourceCountBeforeDelete = countRows();

        repository.delete(uuidPersistentResourceIds);

        int uuidPersistentResourceCountAfterDelete = countRows();

        assertThat(uuidPersistentResourceCountAfterDelete).isEqualTo(uuidPersistentResourceCountBeforeDelete - uuidPersistentResourceIds.size());
    }

    @Test
    public void shouldReturnTrue_whenDeletingMultipleResources() {
        List<String> uuidPersistentResourceIds = Arrays.asList(UuidPersistentResourceRows.uuidPersistentResource1().getUuid(), UuidPersistentResourceRows.uuidPersistentResource2().getUuid());
        boolean delete = repository.delete(uuidPersistentResourceIds);

        assertThat(delete).isTrue();
    }


    @Test
    public void shouldNotDeleteAnyResources_whenDeletingNonExistingResources() throws Exception {
        List<String> uuidPersistentResourceIds = Arrays.asList("1111-222", "313213-321321");
        int uuidPersistentResourceCountBeforeDelete = countRows();

        repository.delete(uuidPersistentResourceIds);

        int uuidPersistentResourceCountAfterDelete = countRows();

        assertThat(uuidPersistentResourceCountAfterDelete).isEqualTo(uuidPersistentResourceCountBeforeDelete);
    }

    @Test
    public void shouldReturnFalse_whenNotDeletingAnyResources() {
        List<String> uuidPersistentResourceIds = Arrays.asList("1111-222", "313213-321321");
        boolean delete = repository.delete(uuidPersistentResourceIds);

        assertThat(delete).isFalse();
    }

    @Test
    public void shouldDeleteOnlyExistingResources_whenDeletingExistingAndNonExistingResources() throws Exception {
        List<String> uuidPersistentResourceIds = Arrays.asList(UuidPersistentResourceRows.uuidPersistentResource1().getUuid(), UuidPersistentResourceRows.uuidPersistentResource2().getUuid(), "665321-111");

        int uuidPersistentResourceCountBeforeDelete = countRows();

        repository.delete(uuidPersistentResourceIds);

        int uuidPersistentResourceCountAfterDelete = countRows();

        assertThat(uuidPersistentResourceCountAfterDelete).isEqualTo(uuidPersistentResourceCountBeforeDelete - 2);
    }

    private int countRows() throws Exception {
        return dbMemory.countRows(QUuidPersistentResource.uuidPersistentResource.getTableName());
    }
}
