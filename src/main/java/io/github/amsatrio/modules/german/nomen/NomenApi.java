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
@Path("/v1/nomen")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NomenApi {

    private static final Set<String> VALID_GENDERS =
            Set.of("masculine", "feminine", "neuter", "plural_only");
    private static final Set<String> VALID_LEVELS =
            Set.of("A1", "A2", "B1", "B2", "C1", "C2");

    @Inject
    private NomenRepository nomenRepository;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<Nomen> getById(@PathParam("id") Long id) {
        Nomen entity;
        try {
            entity = nomenRepository.findById(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        return AppResponse.ok(entity);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<Nomen> deleteById(@PathParam("id") Long id) {
        try {
            nomenRepository.findById(id);
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        nomenRepository.softDelete(id);
        return AppResponse.ok(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<Nomen> create(Nomen data) {
        validate(data);
        data.setId(null);
        data.setCreatedAt(new Date());
        data.setDeletedAt(null);
        Long newId = nomenRepository.insert(data);
        return AppResponse.ok(nomenRepository.findById(newId));
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<Nomen> update(Nomen data) {
        validate(data);
        try {
            nomenRepository.findById(data.getId());
        } catch (NoResultException e) {
            throw new NotFoundException("data not found");
        }
        nomenRepository.update(data);
        return AppResponse.ok(nomenRepository.findById(data.getId()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<PaginationResponse<Nomen>> getPagination(@QueryParam("page") Integer pageIndex,
            @QueryParam("size") Integer pageSize, @QueryParam("sort") String sortRequestString,
            @QueryParam("filter") String filterRequestString, @QueryParam("search") String search) {

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
            sortColumn = NomenRepository.toColumnName(sortRequest.getId());
            sortAsc = !sortRequest.isDesc();
        }

        List<Nomen> data = new ArrayList<>();
        long totalData = 0L;

        if (search != null && !search.isBlank()) {
            data = nomenRepository.search(search.trim(), pageIndex, pageSize, sortColumn, sortAsc);
            totalData = nomenRepository.countBySearch(search.trim());
        } else {
            List<FilterRequest> filterRequests = new ArrayList<>();
            if (filterRequestString != null) {
                filterRequests = FilterRequest.from(filterRequestString);
            }

            if (filterRequests.isEmpty()) {
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

        return AppResponse.ok(paginationResponse);
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