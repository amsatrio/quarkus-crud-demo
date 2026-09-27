package io.github.amsatrio.modules.hospital.t_customer_wallet_withdraw;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.amsatrio.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.dto.exception.DataExistException;
import io.github.amsatrio.dto.exception.NotFoundException;
import io.github.amsatrio.dto.request.FilterRequest;
import io.github.amsatrio.dto.response.PaginationResponse;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TCustomerWalletWithdrawService {
    @Inject
    private TCustomerWalletWithdrawRepository tCustomerWalletWithdrawRepository;

    @CacheResult(cacheName = "hospital/t-customer-wallet-withdraw")
    public TCustomerWalletWithdraw getById(@CacheKey Long id) {
        TCustomerWalletWithdraw entity = null;
        try {
            entity = tCustomerWalletWithdrawRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-customer-wallet-withdraw")
    @CacheInvalidateAll(cacheName = "hospital/t-customer-wallet-withdraw/pagination")
    public void deleteById(Long id) {
        try {
            tCustomerWalletWithdrawRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        tCustomerWalletWithdrawRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-customer-wallet-withdraw")
    @CacheInvalidateAll(cacheName = "hospital/t-customer-wallet-withdraw/pagination")
    public void create(TCustomerWalletWithdraw data) {
        if (data.getId() != null) {
            try {
                TCustomerWalletWithdraw existing = tCustomerWalletWithdrawRepository.findByIdCached(data.getId());
                if (existing != null) {
                    throw new DataExistException("data exists");
                }
            } catch (NoResultException e) {
                // expected - data does not exist
            }
        }

        Long accessUserId = 0L;
        data.setCreatedBy(accessUserId);
        data.setCreatedOn(new Date());
        data.setModifiedBy(null);
        data.setModifiedOn(null);
        data.setDeletedBy(null);
        data.setDeletedOn(null);
        data.setIsDelete(false);

        tCustomerWalletWithdrawRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-customer-wallet-withdraw")
    @CacheInvalidateAll(cacheName = "hospital/t-customer-wallet-withdraw/pagination")
    public void update(TCustomerWalletWithdraw data) {
        TCustomerWalletWithdraw entity = null;
        try {
            entity = tCustomerWalletWithdrawRepository.findByIdCached(data.getId());
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }

        Long accessUserId = 0L;
        entity.setModifiedBy(accessUserId);
        entity.setModifiedOn(new Date());
        entity.setDeletedBy(null);
        entity.setDeletedOn(null);
        entity.setIsDelete(data.getIsDelete());
        if (Boolean.TRUE.equals(entity.getIsDelete())) {
            entity.setDeletedBy(accessUserId);
            entity.setDeletedOn(new Date());
        }

        entity.setCustomerId(data.getCustomerId());
        entity.setWalletDefaultNominalId(data.getWalletDefaultNominalId());
        entity.setAmount(data.getAmount());
        entity.setBankName(data.getBankName());
        entity.setAccountNumber(data.getAccountNumber());
        entity.setAccountName(data.getAccountName());
        entity.setOtp(data.getOtp());

        tCustomerWalletWithdrawRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/t-customer-wallet-withdraw/pagination")
    public PaginationResponse<TCustomerWalletWithdraw> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<TCustomerWalletWithdraw> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = tCustomerWalletWithdrawRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tCustomerWalletWithdrawRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = TCustomerWalletWithdrawRepository.toColumnName(filterRequest.getId());
                String paramName = "p" + i;

                if (i != 0) {
                    whereClause.append(" AND ");
                }

                whereClause.append(column);
                switch (filterRequest.getMatchMode()) {
                    case FilterMatchMode.CONTAINS:
                        whereClause.append(" LIKE :").append(paramName);
                        params.put(paramName, "%" + filterRequest.getValue() + "%");
                        break;
                    case FilterMatchMode.EQUALS:
                        whereClause.append(" = :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    case FilterMatchMode.NOT:
                        whereClause.append(" <> :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    case FilterMatchMode.LESS_THAN:
                        whereClause.append(" < :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    case FilterMatchMode.GREATER_THAN:
                        whereClause.append(" > :").append(paramName);
                        params.put(paramName, filterRequest.getValue());
                        break;
                    default:
                        whereClause.append(" LIKE :").append(paramName);
                        params.put(paramName, "%" + filterRequest.getValue() + "%");
                        break;
                }
            }

            data = tCustomerWalletWithdrawRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tCustomerWalletWithdrawRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<TCustomerWalletWithdraw> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
