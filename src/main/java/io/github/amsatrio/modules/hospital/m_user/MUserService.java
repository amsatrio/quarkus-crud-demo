package io.github.amsatrio.modules.hospital.m_user;

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
public class MUserService {
    @Inject
    private MUserRepository mUserRepository;

    @CacheResult(cacheName = "hospital/m-user")
    public MUser getById(@CacheKey Long id) {
        MUser entity = null;
        try {
            entity = mUserRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-user")
    @CacheInvalidateAll(cacheName = "hospital/m-user/pagination")
    public void deleteById(Long id) {
        try {
            mUserRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        mUserRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-user")
    @CacheInvalidateAll(cacheName = "hospital/m-user/pagination")
    public void create(MUser data) {
        if (data.getId() != null) {
            try {
                MUser existing = mUserRepository.findByIdCached(data.getId());
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

        mUserRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/m-user")
    @CacheInvalidateAll(cacheName = "hospital/m-user/pagination")
    public void update(MUser data) {
        MUser entity = null;
        try {
            entity = mUserRepository.findByIdCached(data.getId());
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

        entity.setBiodataId(data.getBiodataId());
        entity.setRoleId(data.getRoleId());
        entity.setEmail(data.getEmail());
        entity.setPassword(data.getPassword());
        entity.setLoginAttempt(data.getLoginAttempt());
        entity.setIsLocked(data.getIsLocked());
        entity.setLastLogin(data.getLastLogin());

        mUserRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/m-user/pagination")
    public PaginationResponse<MUser> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<MUser> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = mUserRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mUserRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = MUserRepository.toColumnName(filterRequest.getId());
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

            data = mUserRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mUserRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<MUser> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
