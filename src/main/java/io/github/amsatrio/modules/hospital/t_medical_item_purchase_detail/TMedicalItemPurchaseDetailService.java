package io.github.amsatrio.modules.hospital.t_medical_item_purchase_detail;

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
public class TMedicalItemPurchaseDetailService {
    @Inject
    private TMedicalItemPurchaseDetailRepository tMedicalItemPurchaseDetailRepository;

    @CacheResult(cacheName = "hospital/t-medical-item-purchase-detail")
    public TMedicalItemPurchaseDetail getById(@CacheKey Long id) {
        TMedicalItemPurchaseDetail entity = null;
        try {
            entity = tMedicalItemPurchaseDetailRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-medical-item-purchase-detail")
    @CacheInvalidateAll(cacheName = "hospital/t-medical-item-purchase-detail/pagination")
    public void deleteById(Long id) {
        try {
            tMedicalItemPurchaseDetailRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        tMedicalItemPurchaseDetailRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-medical-item-purchase-detail")
    @CacheInvalidateAll(cacheName = "hospital/t-medical-item-purchase-detail/pagination")
    public void create(TMedicalItemPurchaseDetail data) {
        if (data.getId() != null) {
            try {
                TMedicalItemPurchaseDetail existing = tMedicalItemPurchaseDetailRepository.findByIdCached(data.getId());
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

        tMedicalItemPurchaseDetailRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-medical-item-purchase-detail")
    @CacheInvalidateAll(cacheName = "hospital/t-medical-item-purchase-detail/pagination")
    public void update(TMedicalItemPurchaseDetail data) {
        TMedicalItemPurchaseDetail entity = null;
        try {
            entity = tMedicalItemPurchaseDetailRepository.findByIdCached(data.getId());
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

        entity.setMedicalItemPurchaseId(data.getMedicalItemPurchaseId());
        entity.setMedicalItemId(data.getMedicalItemId());
        entity.setQty(data.getQty());
        entity.setMedicalFacilityId(data.getMedicalFacilityId());
        entity.setCourirId(data.getCourirId());
        entity.setSubTotal(data.getSubTotal());

        tMedicalItemPurchaseDetailRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/t-medical-item-purchase-detail/pagination")
    public PaginationResponse<TMedicalItemPurchaseDetail> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<TMedicalItemPurchaseDetail> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = tMedicalItemPurchaseDetailRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tMedicalItemPurchaseDetailRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = TMedicalItemPurchaseDetailRepository.toColumnName(filterRequest.getId());
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

            data = tMedicalItemPurchaseDetailRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tMedicalItemPurchaseDetailRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<TMedicalItemPurchaseDetail> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
