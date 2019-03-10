package org.lpro.lebonsandwich.boundary;

import java.util.List;

import org.lpro.lebonsandwich.entity.Categorie;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author dufour76u
 */
public interface CategorieResource extends CrudRepository<Categorie, String> {
	List<Categorie> findBySandwichsId(String categorieId);
}
