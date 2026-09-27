package io.github.amsatrio.modules.hospital.m_location_level;

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
public class MLocationLevelService {
    @Inject
    private MLocationLevelRepository mLocationLevelRepository;

    @CacheResult(cacheName = "hospital/m-location-level")
    public MLocationLevel getById(@CacheKey Long id) {
        MLocationLevel entity = null;
        try {
            entity = mLocationLevelRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-location-level")
    @CacheInvalidateAll(cacheName = "hospital/m-location-level/pagination")
    public void deleteById(Long id) {
        try {
            mLocationLevelRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        mLocationLevelRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-location-level")
    @CacheInvalidateAll(cacheName = "hospital/m-location-level/pagination")
    public void create(MLocationLevel data) {
        if (data.getId() != null) {
            try {
                MLocationLevel existing = mLocationLevelRepository.findByIdCached(data.getId());
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

        mLocationLevelRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-location-level")
    @CacheInvalidateAll(cacheName = "hospital/m-location-level/pagination")
    public void update(MLocationLevel data) {
        MLocationLevel entity = null;
        try {
            entity = mLocationLevelRepository.findByIdCached(data.getId());
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
        entity.setAbbreviation(data.getAbbreviation());

        mLocationLevelRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/m-location-level/pagination")
    public PaginationResponse<MLocationLevel> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<MLocationLevel> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = mLocationLevelRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mLocationLevelRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = MLocationLevelRepository.toColumnName(filterRequest.getId());
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

            data = mLocationLevelRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mLocationLevelRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<MLocationLevel> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
