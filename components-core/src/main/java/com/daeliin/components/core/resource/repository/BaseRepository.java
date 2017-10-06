package com.daeliin.components.core.resource.repository;

import com.daeliin.components.domain.pagination.Page;
import com.daeliin.components.domain.pagination.PageRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;

/**
 * @param <R> row type
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

    @Override
    public RelationalPathBase<R> rowPath() {
        return rowPath;
    }

    @Transactional(readOnly = true)
    @Override
    public R findOne(Predicate predicate) {
        if (predicate == null) {
            return null;
        }

        return queryFactory.select(rowPath)
            .from(rowPath)
            .where(predicate)
            .fetchOne();
    }

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

    @Transactional(readOnly = true)
    @Override
    public Page<R> findAll(PageRequest pageRequest) {
        return findAll(null, pageRequest);
    }

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

    @Transactional(readOnly = true)
    @Override
    public Collection<R> findAll() {
        return queryFactory.select(rowPath)
            .from(rowPath)
            .fetch();
    }

    @Transactional(readOnly = true)
    @Override
    public long count() {
        return queryFactory.select(rowPath)
            .from(rowPath)
            .fetchCount();
    }

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

    @Transactional
    @Override
    public boolean deleteAll() {
        return queryFactory.delete(rowPath).execute() > 0;
    }
}
