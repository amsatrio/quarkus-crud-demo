package io.github.amsatrio.modules.hospital.t_doctor_office;

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
public class TDoctorOfficeService {
    @Inject
    private TDoctorOfficeRepository tDoctorOfficeRepository;

    @CacheResult(cacheName = "hospital/t-doctor-office")
    public TDoctorOffice getById(@CacheKey Long id) {
        TDoctorOffice entity = null;
        try {
            entity = tDoctorOfficeRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office")
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office/pagination")
    public void deleteById(Long id) {
        try {
            tDoctorOfficeRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        tDoctorOfficeRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office")
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office/pagination")
    public void create(TDoctorOffice data) {
        if (data.getId() != null) {
            try {
                TDoctorOffice existing = tDoctorOfficeRepository.findByIdCached(data.getId());
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

        tDoctorOfficeRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office")
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office/pagination")
    public void update(TDoctorOffice data) {
        TDoctorOffice entity = null;
        try {
            entity = tDoctorOfficeRepository.findByIdCached(data.getId());
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

        entity.setDoctorId(data.getDoctorId());
        entity.setMedicalFacilityId(data.getMedicalFacilityId());
        entity.setSpecialization(data.getSpecialization());

        tDoctorOfficeRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/t-doctor-office/pagination")
    public PaginationResponse<TDoctorOffice> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<TDoctorOffice> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = tDoctorOfficeRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tDoctorOfficeRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = TDoctorOfficeRepository.toColumnName(filterRequest.getId());
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

            data = tDoctorOfficeRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tDoctorOfficeRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<TDoctorOffice> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
