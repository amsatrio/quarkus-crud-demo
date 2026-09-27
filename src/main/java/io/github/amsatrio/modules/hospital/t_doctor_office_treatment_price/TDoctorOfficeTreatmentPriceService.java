package io.github.amsatrio.modules.hospital.t_doctor_office_treatment_price;

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
public class TDoctorOfficeTreatmentPriceService {
    @Inject
    private TDoctorOfficeTreatmentPriceRepository tDoctorOfficeTreatmentPriceRepository;

    @CacheResult(cacheName = "hospital/t-doctor-office-treatment-price")
    public TDoctorOfficeTreatmentPrice getById(@CacheKey Long id) {
        TDoctorOfficeTreatmentPrice entity = null;
        try {
            entity = tDoctorOfficeTreatmentPriceRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return entity;
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office-treatment-price")
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office-treatment-price/pagination")
    public void deleteById(Long id) {
        try {
            tDoctorOfficeTreatmentPriceRepository.findByIdCached(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        tDoctorOfficeTreatmentPriceRepository.hardDelete(id);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office-treatment-price")
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office-treatment-price/pagination")
    public void create(TDoctorOfficeTreatmentPrice data) {
        if (data.getId() != null) {
            try {
                TDoctorOfficeTreatmentPrice existing = tDoctorOfficeTreatmentPriceRepository.findByIdCached(data.getId());
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

        tDoctorOfficeTreatmentPriceRepository.insert(data);
    }

    @Transactional
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office-treatment-price")
    @CacheInvalidateAll(cacheName = "hospital/t-doctor-office-treatment-price/pagination")
    public void update(TDoctorOfficeTreatmentPrice data) {
        TDoctorOfficeTreatmentPrice entity = null;
        try {
            entity = tDoctorOfficeTreatmentPriceRepository.findByIdCached(data.getId());
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

        entity.setDoctorOfficeTreatmentId(data.getDoctorOfficeTreatmentId());
        entity.setPrice(data.getPrice());
        entity.setPriceStartFrom(data.getPriceStartFrom());
        entity.setPriceUntilFrom(data.getPriceUntilFrom());

        tDoctorOfficeTreatmentPriceRepository.update(entity);
    }

    @CacheResult(cacheName = "hospital/t-doctor-office-treatment-price/pagination")
    public PaginationResponse<TDoctorOfficeTreatmentPrice> getPagination(
            @CacheKey Integer pageIndex,
            @CacheKey Integer pageSize,
            @CacheKey List<FilterRequest> filterRequests,
            @CacheKey String sortColumn,
            @CacheKey boolean sortAsc) {

        List<TDoctorOfficeTreatmentPrice> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = tDoctorOfficeTreatmentPriceRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tDoctorOfficeTreatmentPriceRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = TDoctorOfficeTreatmentPriceRepository.toColumnName(filterRequest.getId());
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

            data = tDoctorOfficeTreatmentPriceRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = tDoctorOfficeTreatmentPriceRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<TDoctorOfficeTreatmentPrice> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return paginationResponse;
    }
}
