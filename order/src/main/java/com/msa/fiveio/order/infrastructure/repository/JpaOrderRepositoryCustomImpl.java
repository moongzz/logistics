package com.msa.fiveio.order.infrastructure.repository;

import com.msa.fiveio.common.config.QueryDslConfig;
import com.msa.fiveio.order.model.entity.Order;
import com.msa.fiveio.order.model.entity.QOrder;
import com.msa.fiveio.order.application.dto.request.OrderSearchRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class JpaOrderRepositoryCustomImpl implements JpaOrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> readOrders(OrderSearchRequestDto requestDto, Pageable pageable) {
        QOrder order = QOrder.order;

        OrderSpecifier<?>[] orderSpecifiers = QueryDslConfig.getAllOrderSpecifierArr(pageable,
            order);
        BooleanBuilder builder = createBooleanBuilder(requestDto);

        JPAQuery<Order> baseQuery = queryFactory
            .selectFrom(order)
            .where(builder);

        List<Order> fetch = baseQuery
            .orderBy(orderSpecifiers)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = getTotalCount(builder);

        return new PageImpl<>(fetch, pageable, totalCount);
    }

    private BooleanBuilder createBooleanBuilder(OrderSearchRequestDto requestDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (requestDto.getRequesterCompanyId() != null) {
            builder.and(QOrder.order.requesterCompanyId.eq(requestDto.getRequesterCompanyId()));
        }
        if (requestDto.getReceiverCompanyId() != null) {
            builder.and(QOrder.order.receiverCompanyId.eq(requestDto.getReceiverCompanyId()));
        }
        if (requestDto.getProductId() != null) {
            builder.and(QOrder.order.productId.eq(requestDto.getProductId()));
        }
        return builder;
    }

    private Long getTotalCount(BooleanBuilder builder) {
        Long count = queryFactory
            .select(Wildcard.count)
            .from(QOrder.order)
            .where(builder)
            .fetchOne();
        return count != null ? count : 0L;
    }
}
