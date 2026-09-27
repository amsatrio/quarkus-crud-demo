package io.github.amsatrio.modules.hospital.t_doctor_office_treatment;

import java.util.ArrayList;
import java.util.List;

import io.github.amsatrio.dto.request.FilterRequest;
import io.github.amsatrio.dto.request.SortRequest;
import io.github.amsatrio.dto.response.AppResponse;
import io.github.amsatrio.dto.response.PaginationResponse;
import jakarta.inject.Inject;
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
@Path("/v1/hospital/t-doctor-office-treatment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TDoctorOfficeTreatmentApi {
    @Inject
    private TDoctorOfficeTreatmentService tDoctorOfficeTreatmentService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<TDoctorOfficeTreatment> getById(@PathParam("id") Long id) {
        return AppResponse.ok(tDoctorOfficeTreatmentService.getById(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<TDoctorOfficeTreatment> deleteById(@PathParam("id") Long id) {
        tDoctorOfficeTreatmentService.deleteById(id);
        return AppResponse.ok(null);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AppResponse<TDoctorOfficeTreatment> create(TDoctorOfficeTreatment data) {
        tDoctorOfficeTreatmentService.create(data);
        return AppResponse.ok(null);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AppResponse<TDoctorOfficeTreatment> update(TDoctorOfficeTreatment data) {
        tDoctorOfficeTreatmentService.update(data);
        return AppResponse.ok(null);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<PaginationResponse<TDoctorOfficeTreatment>> getPagination(@QueryParam("page") Integer pageIndex,
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
            sortColumn = TDoctorOfficeTreatmentRepository.toColumnName(sortRequest.getId());
            sortAsc = !sortRequest.isDesc();
        }

        List<FilterRequest> filterRequests = new ArrayList<>();
        if (filterRequestString != null) {
            filterRequests = FilterRequest.from(filterRequestString);
        }

        return AppResponse.ok(tDoctorOfficeTreatmentService.getPagination(pageIndex, pageSize, filterRequests, sortColumn, sortAsc));
    }
}
