package io.github.amsatrio.modules.mbiodata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.amsatrio.dto.enumerator.FilterMatchMode;
import io.github.amsatrio.dto.exception.DataExistException;
import io.github.amsatrio.dto.exception.NotFoundException;
import io.github.amsatrio.dto.request.FilterRequest;
import io.github.amsatrio.dto.request.SortRequest;
import io.github.amsatrio.dto.response.AppResponse;
import io.github.amsatrio.dto.response.PaginationResponse;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
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
@Path("/v1/m-biodata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MBiodataApi {
    @Inject
    private MBiodataRepository mBiodataRepository;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<MBiodata> getById(@PathParam("id") Long id) {
        MBiodata mBiodata = mBiodataRepository.findById(id);
        if (mBiodata == null) {
            throw new NotFoundException();
        }
        return AppResponse.ok(mBiodata);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<MBiodata> deleteById(@PathParam("id") Long id) {
        mBiodataRepository.deleteById(id);
        return AppResponse.ok(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<MBiodata> create(MBiodata data) {
        MBiodata entity = mBiodataRepository.findById(data.getId());
        if (entity != null) {
            throw new DataExistException("data exists");
        }

        Long accessUserId = 0L;
        data.setCreatedBy(accessUserId);
        data.setCreatedOn(new Date());
        data.setModifiedBy(null);
        data.setModifiedOn(null);
        data.setDeletedBy(null);
        data.setDeletedOn(null);
        data.setIsDelete(false);

        mBiodataRepository.persist(data);
        return AppResponse.ok(null);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public AppResponse<MBiodata> update(MBiodata data) {
        MBiodata entity = mBiodataRepository.findById(data.getId());
        if (entity == null) {
            throw new NotFoundException("data not found");
        }

        Long accessUserId = 0L;
        entity.setModifiedBy(accessUserId);
        entity.setModifiedOn(new Date());
        entity.setDeletedBy(null);
        entity.setDeletedOn(null);
        entity.setIsDelete(data.getIsDelete());
        if (entity.getIsDelete()) {
            entity.setDeletedBy(accessUserId);
            entity.setDeletedOn(new Date());
        }

        entity.setImage(data.getImage());
        entity.setImagePath(data.getImagePath());
        entity.setFullname(data.getFullname());
        entity.setMobilePhone(data.getMobilePhone());

        return AppResponse.ok(null);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<PaginationResponse<MBiodata>> getPagination(@QueryParam("page") Integer pageIndex,
            @QueryParam("size") Integer pageSize, @QueryParam("sort") String sortRequestString,
            @QueryParam("filter") String filterRequestString) {

        // PAGINATION
        if (pageIndex == null) {
            pageIndex = 0;
        }
        if (pageSize == null) {
            pageSize = 5;
        }
        Page page = Page.of(pageIndex, pageSize);

        // SORT
        Sort sort = Sort.by("id").ascending();
        if (sortRequestString != null) {
            SortRequest sortRequest = SortRequest.from(sortRequestString).getFirst();
            sort = Sort.by(sortRequest.getId()).ascending();
            if (sortRequest.isDesc()) {
                sort.descending();
            }
        }

        // FILTER
        List<FilterRequest> filterRequests = new ArrayList<>();
        if (filterRequestString != null) {
            filterRequests = FilterRequest.from(filterRequestString);
        }

        List<MBiodata> data = new ArrayList<>();
        long totalData = 0L;
        if (filterRequests.size() == 0) {
            data = mBiodataRepository.findAll(sort).page(page).list();
            totalData = mBiodataRepository.count();
        } else {
            StringBuilder queryBuilder = new StringBuilder();
            Parameters params = new Parameters();
            for (int i = 0; i < filterRequests.size(); i++) {
                FilterRequest filterRequest = filterRequests.get(i);
                if (i != 0) {
                    queryBuilder.append(" AND ");
                }

                queryBuilder.append(filterRequest.getId());
                switch (filterRequest.getMatchMode()) {
                    case FilterMatchMode.CONTAINS:
                        queryBuilder.append(" LIKE ");
                        params.and(filterRequest.getId(), "%" + filterRequest.getValue() + "%");
                        break;
                    case FilterMatchMode.EQUALS:
                        queryBuilder.append(" = ");
                        params.and(filterRequest.getId(), filterRequest.getValue());
                        break;
                    case FilterMatchMode.NOT:
                        queryBuilder.append(" <> ");
                        params.and(filterRequest.getId(), filterRequest.getValue());
                        break;
                    case FilterMatchMode.LESS_THAN:
                        queryBuilder.append(" < ");
                        params.and(filterRequest.getId(), filterRequest.getValue());
                        break;
                    case FilterMatchMode.GREATER_THAN:
                        queryBuilder.append(" > ");
                        params.and(filterRequest.getId(), filterRequest.getValue());
                    default:
                        queryBuilder.append(" LIKE ");
                        params.and(filterRequest.getId(), "%" + filterRequest.getValue() + "%");
                        break;
                }
                queryBuilder.append(":" + filterRequest.getId());
            }

            data = mBiodataRepository.find(queryBuilder.toString(), sort, params).page(page).list();
            totalData = mBiodataRepository.count(queryBuilder.toString(), params);
        }
        long totalPages = totalData / pageSize;
        if (totalData % pageSize > 0) {
            totalPages++;
        }

        PaginationResponse<MBiodata> paginationResponse = new PaginationResponse<>();
        paginationResponse.setContent(data);
        paginationResponse.setTotalElements(totalData);
        paginationResponse.setTotalPages(totalPages);
        paginationResponse.setFirst(pageIndex == 0);
        paginationResponse.setLast(pageIndex == totalPages - 1);

        return AppResponse.ok(paginationResponse);
    }
}