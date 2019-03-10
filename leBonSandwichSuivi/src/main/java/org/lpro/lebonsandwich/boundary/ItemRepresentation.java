package org.lpro.lebonsandwich.boundary;

import java.util.UUID;
import org.lpro.lebonsandwich.entity.Item;
import org.lpro.lebonsandwich.exception.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 *
 * @author dufour76u
 */
public class ItemRepresentation {
    
    private final ItemResource ir;
    private final CommandeResource cr;

    public ItemRepresentation(ItemResource ir, CommandeResource cr) {
        this.ir = ir;
        this.cr = cr;
    }
    
    @GetMapping("/commandes/{id}/items")
    public ResponseEntity<?> getItemsByCommandeId(@PathVariable("id") String id)
            throws NotFound {
        
        if (!cr.existsById(id)) {
            throw new NotFound("commande inexistante");
        }
        return new ResponseEntity<>(ir.findByCommandeId(id), HttpStatus.OK);
    }
  
    @PostMapping("/commandes/{id}/items")
    public ResponseEntity<?> ajoutItem(@PathVariable("id") String id,
            @RequestBody Item item) throws NotFound {
        return cr.findById(id)
              .map(commande -> {
                    item.setId(UUID.randomUUID().toString());
                    item.setCommande(commande);
                    ir.save(item);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }).orElseThrow ( () -> new NotFound("commande inexistante"));
    }
    
    @PutMapping("/commandes/{commandeId}/items/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable("commandeId") String commandeId,
            @PathVariable("itemId") String itemId,
            @RequestBody Item itemUpdated) {
        
        if (!cr.existsById(commandeId)) {
            throw new NotFound("commande inexistante");
        }
        return ir.findById(itemId)
                .map(item -> {
                    itemUpdated.setId(item.getId());
                    ir.save(itemUpdated);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Item inexistant"));
    }
     
    @DeleteMapping("/commandes/{commandeId}/items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("commandeId") String commandeId,
            @PathVariable("itemId") String itemId) {
        
        if (!cr.existsById(commandeId)) {
            throw new NotFound("Commande inexistante");
        }
        return ir.findById(itemId)
                .map(item -> {
                    ir.delete(item);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Item inexistant"));
    }
    
}
