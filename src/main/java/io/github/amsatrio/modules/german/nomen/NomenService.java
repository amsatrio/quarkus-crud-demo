package io.github.amsatrio.modules.german.nomen;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.amsatrio.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.dto.exception.BadRequestException;
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
public class NomenService {

    private static final Set<String> VALID_GENDERS =
            Set.of("masculine", "feminine", "neuter", "plural_only");
    private static final Set<String> VALID_LEVELS =
            Set.of("A1", "A2", "B1", "B2", "C1", "C2");

    @Inject
    private NomenRepository nomenRepository;

    @CacheResult(cacheName = "german/nomen")
    public Nomen getById(@CacheKey Long id) {
        Nomen entity;
        try {
            entity = nomenRepository.findById(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "german/nomen")
    @CacheInvalidateAll(cacheName = "german/nomen/pagination")
    public void deleteById(Long id) {
        try {
            nomenRepository.findById(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        nomenRepository.softDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "german/nomen")
    @CacheInvalidateAll(cacheName = "german/nomen/pagination")
    public Nomen create(Nomen data) {
        validate(data);
        data.setId(null);
        data.setCreatedAt(new Date());
        data.setDeletedAt(null);
        Long newId = nomenRepository.insert(data);
        return nomenRepository.findById(newId);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "german/nomen")
    @CacheInvalidateAll(cacheName = "german/nomen/pagination")
    public Nomen update(Nomen data) {
        validate(data);
        try {
            nomenRepository.findById(data.getId());
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        nomenRepository.update(data);
        return nomenRepository.findById(data.getId());
    }

    @CacheResult(cacheName = "german/nomen/pagination")
    public PaginationResponse<Nomen> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey String search,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<Nomen> data = new ArrayList<>();
        long totalData = 0L;

        if (search != null && !search.isBlank()) {
            String keyword = search.trim();
            data = nomenRepository.search(keyword, pageIndex, pageSize, sortColumn, sortAsc);
            totalData = nomenRepository.countBySearch(keyword);
        } else if (filterRequests.isEmpty()) {
            data = nomenRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = nomenRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = NomenRepository.toColumnName(filterRequest.getId());
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

            data = nomenRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = nomenRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<Nomen> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }

    private void validate(Nomen data) {
        if (data.getGender() != null && !VALID_GENDERS.contains(data.getGender())) {
            throw new BadRequestException(
                    "invalid gender, must be one of " + VALID_GENDERS);
        }
        if (data.getLevel() != null && !VALID_LEVELS.contains(data.getLevel())) {
            throw new BadRequestException(
                    "invalid level, must be one of " + VALID_LEVELS);
        }
    }
}