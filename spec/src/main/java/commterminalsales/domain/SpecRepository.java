package commterminalsales.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "specs", path = "specs")
public interface SpecRepository
        extends PagingAndSortingRepository<Spec, Long> {

    @Query(value = "select spec " +
            "from Spec spec " +
            "where(:color is null or spec.phoneColor like %:color%) and (:manufacturer is null or spec.manufacturer = :manufacturer)")
    List<Spec> FindByColorsAndManufacturer(String color, Manufacturer manufacturer);
}
