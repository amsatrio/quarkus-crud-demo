package io.github.amsatrio.modules.mbiodata;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MBiodataRepository implements PanacheRepository<MBiodata>{
}
