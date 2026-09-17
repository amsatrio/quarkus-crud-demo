package io.github.amsatrio.modules.hospital.m_menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.amsatrio.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.dto.exception.DataExistException;
import io.github.amsatrio.dto.exception.NotFoundException;
import io.github.amsatrio.dto.request.FilterRequest;
import io.github.amsatrio.dto.request.SortRequest;
import io.github.amsatrio.dto.response.AppResponse;
import io.github.amsatrio.dto.response.PaginationResponse;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/v1/m-menu")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MMenuApi {
    @Inject
    private MMenuRepository mMenuRepository;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<MMenu> getById(@PathParam("id") Long id) {
        MMenu entity = null;
        try {
            entity = mMenuRepository.findById(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return AppResponse.ok(entity);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<MMenu> deleteById(@PathParam("id") Long id) {
        try {
            mMenuRepository.findById(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        mMenuRepository.hardDelete(id);
        return AppResponse.ok(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<MMenu> create(MMenu data) {
        if (data.getId() != null) {
            try {
                MMenu existing = mMenuRepository.findById(data.getId());
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

        mMenuRepository.insert(data);
        return AppResponse.ok(null);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<MMenu> update(MMenu data) {
        MMenu entity = null;
        try {
            entity = mMenuRepository.findById(data.getId());
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
        entity.setUrl(data.getUrl());
        entity.setParentId(data.getParentId());
        entity.setBigIcon(data.getBigIcon());
        entity.setSmallIcon(data.getSmallIcon());

        mMenuRepository.update(entity);
        return AppResponse.ok(null);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<PaginationResponse<MMenu>> getPagination(@QueryParam("page") Integer pageIndex,
            @QueryParam("size") Integer pageSize, @QueryParam("sort") String sortRequestString,
            @QueryParam("filter") String filterRequestString) {

        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 5;
        }

        String sortColumn = "id";
        boolean sortAsc = true;
        if (sortRequestString != null) {
            SortRequest sortRequest = SortRequest.from(sortRequestString).getFirst();
            sortColumn = MMenuRepository.toColumnName(sortRequest.getId());
            sortAsc = !sortRequest.isDesc();
        }

        List<FilterRequest> filterRequests = new ArrayList<>();
        if (filterRequestString != null) {
            filterRequests = FilterRequest.from(filterRequestString);
        }

        List<MMenu> data = new ArrayList<>();
        long totalData = 0L;

        if (filterRequests.isEmpty()) {
            data = mMenuRepository.findAll(pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mMenuRepository.countAll();
        } else {
            StringBuilder whereClause = new StringBuilder();
            Map<String, Object> params = new HashMap<>();

            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                String column = MMenuRepository.toColumnName(filterRequest.getId());
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

            data = mMenuRepository.findByFilter(whereClause.toString(), params,
                    pageIndex, pageSize, sortColumn, sortAsc);
            totalData = mMenuRepository.countByFilter(whereClause.toString(), params);
        }

        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<MMenu> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return AppResponse.ok(paginationResponse);
    }
}
