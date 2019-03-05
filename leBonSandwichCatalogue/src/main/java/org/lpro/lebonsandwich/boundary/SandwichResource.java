package org.lpro.lebonsandwich.boundary;

import java.util.List;
import org.lpro.lebonsandwich.entity.Sandwich;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author dufour76u
 */
public interface SandwichResource extends CrudRepository<Sandwich, String> {

    List<Sandwich> findByCategorieId(String categorieId);

}
