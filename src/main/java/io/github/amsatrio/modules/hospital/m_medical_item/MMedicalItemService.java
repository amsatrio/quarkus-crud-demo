package io.github.amsatrio.modules.hospital.m_medical_item;

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
public class MMedicalItemService {
    @Inject
    private MMedicalItemRepository mMedicalItemRepository;

    @CacheResult(cacheName = "hospital/m-medical-item")
    public MMedicalItem getById(@CacheKey Long id) {
        MMedicalItem entity = null;
        try {
            entity = mMedicalItemRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-medical-item")
    @CacheInvalidateAll(cacheName = "hospital/m-medical-item/pagination")
    public void deleteById(Long id) {
        try {
            mMedicalItemRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        mMedicalItemRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-medical-item")
    @CacheInvalidateAll(cacheName = "hospital/m-medical-item/pagination")
    public void create(MMedicalItem data) {
        if (data.getId() != null) {
            try {
                MMedicalItem existing = mMedicalItemRepository.findByIdCached(data.getId());
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

        mMedicalItemRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-medical-item")
    @CacheInvalidateAll(cacheName = "hospital/m-medical-item/pagination")
    public void update(MMedicalItem data) {
        MMedicalItem entity = null;
        try {
            entity = mMedicalItemRepository.findByIdCached(data.getId());
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

        entity.setName(data.getName());
        entity.setMedicalItemCategoryId(data.getMedicalItemCategoryId());
        entity.setComposition(data.getComposition());
        entity.setMedicalItemSegmentationId(data.getMedicalItemSegmentationId());
        entity.setManufacturer(data.getManufacturer());
        entity.setIndication(data.getIndication());
        entity.setDosage(data.getDosage());
        entity.setDirections(data.getDirections());
        entity.setContraindication(data.getContraindication());
        entity.setCaution(data.getCaution());
        entity.setPackaging(data.getPackaging());
        entity.setPriceMax(data.getPriceMax());
        entity.setPriceMin(data.getPriceMin());
        entity.setImage(data.getImage());
        entity.setImagePath(data.getImagePath());

        mMedicalItemRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/m-medical-item/pagination")
    public PaginationResponse<MMedicalItem> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<MMedicalItem> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = mMedicalItemRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mMedicalItemRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = MMedicalItemRepository.toColumnName(filterRequest.getId());
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

            data = mMedicalItemRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mMedicalItemRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<MMedicalItem> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
