package org.lpro.lebonsandwich.boundary;

import org.lpro.lebonsandwich.entity.Commande;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author dufour76u
 */
public interface CommandeResource extends CrudRepository<Commande, String> {
    
}
