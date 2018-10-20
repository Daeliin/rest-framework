package com.blebail.components.persistence.resource.repository;

import com.blebail.components.core.pagination.Page;
import com.blebail.components.core.pagination.PageRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class BaseRepository<R> implements PagingRepository<R> {

    @Inject
    protected SQLQueryFactory queryFactory;

    protected final RowOrder rowOrder;
    protected final RelationalPathBase<R> rowPath;

    public BaseRepository(RelationalPathBase<R> rowPath) {
        this.rowPath = Objects.requireNonNull(rowPath);
        this.rowOrder = new RowOrder(rowPath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationalPathBase<R> rowPath() {
        return rowPath;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<R> findOne(Predicate predicate) {
        if (predicate == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(queryFactory.select(rowPath)
            .from(rowPath)
            .where(predicate)
            .fetchOne());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<R> findAll(Predicate predicate) {
        SQLQuery<R> query = queryFactory.select(rowPath)
            .from(rowPath);

        if (predicate != null) {
            query = query.where(predicate);
        }

        return query.fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<R> findAll(PageRequest pageRequest) {
        return findAll(null, pageRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Page<R> findAll(Predicate predicate, PageRequest pageRequest) {
        long totalItems = count(predicate);
        long totalPages = rowOrder.computeTotalPages(totalItems, pageRequest.size);
        OrderSpecifier[] orders = rowOrder.computeOrders(pageRequest);

        SQLQuery<R> query = queryFactory.select(rowPath)
            .from(rowPath)
            .limit(pageRequest.size)
            .offset(pageRequest.offset)
            .orderBy(orders);

        if (predicate != null) {
            query = query.where(predicate);
        }

        return new Page<>(query.fetch(), totalItems, totalPages);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public Collection<R> findAll() {
        return queryFactory.select(rowPath)
            .from(rowPath)
            .fetch();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public long count() {
        return queryFactory.select(rowPath)
            .from(rowPath)
            .fetchCount();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    @Override
    public long count(Predicate predicate) {
        SQLQuery<R> query = queryFactory.select(rowPath)
            .from(rowPath);

        if (predicate != null) {
            query = query.where(predicate);
        }

        return query.fetchCount();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean delete(Predicate predicate) {
        if (predicate == null) {
            throw new IllegalArgumentException("Predicate should not be null on a delete, otherwise whole table will be deleted, " +
                    "call deleteAll() instead if it's the desired operation");
        }

        return queryFactory.delete(rowPath).where(predicate).execute() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public boolean deleteAll() {
        return queryFactory.delete(rowPath).execute() > 0;
    }
}
