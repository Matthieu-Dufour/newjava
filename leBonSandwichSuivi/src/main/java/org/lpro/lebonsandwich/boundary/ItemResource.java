package org.lpro.lebonsandwich.boundary;

import java.util.List;
import org.lpro.lebonsandwich.entity.Item;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author dufour76u
 */
public interface ItemResource extends CrudRepository<Item, String> {

    List<Item> findByCommandeId(String commandeId);

}
