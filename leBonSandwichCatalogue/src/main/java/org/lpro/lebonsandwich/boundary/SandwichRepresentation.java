package org.lpro.lebonsandwich.boundary;

import java.util.UUID;
import org.lpro.lebonsandwich.entity.Sandwich;
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
public class SandwichRepresentation {
    
    private final SandwichResource sr;
    private final CategorieResource cr;

    public SandwichRepresentation(SandwichResource sr, CategorieResource cr) {
        this.sr = sr;
        this.cr = cr;
    }
    
    @GetMapping("/categories/{id}/sandwichs")
    public ResponseEntity<?> getSandwichsByCategorieId(@PathVariable("id") String id)
            throws NotFound {
        
        if (!cr.existsById(id)) {
            throw new NotFound("Catégorie inexistante");
        }
        return new ResponseEntity<>(sr.findByCategorieId(id), HttpStatus.OK);
    }
  
    @PostMapping("/categories/{id}/sandwichs")
    public ResponseEntity<?> ajoutSandwich(@PathVariable("id") String id,
            @RequestBody Sandwich sandwich) throws NotFound {
        return cr.findById(id)
                .map(categorie -> {
                    sandwich.setId(UUID.randomUUID().toString());
                    sandwich.setCategorie(categorie);
                    sr.save(sandwich);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                }).orElseThrow ( () -> new NotFound("Catégorie inexistante"));
    }
    
    @PutMapping("/categories/{categorieId}/sandwichs/{sandwichId}")
    public ResponseEntity<?> updateSandwich(@PathVariable("categorieId") String categorieId,
            @PathVariable("sandwichId") String sandwichId,
            @RequestBody Sandwich sandwichUpdated) {
        
        if (!cr.existsById(categorieId)) {
            throw new NotFound("Catégorie inexistante");
        }
        return sr.findById(sandwichId)
                .map(sandwich -> {
                    sandwichUpdated.setId(sandwich.getId());
                    sr.save(sandwichUpdated);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Sandwich inexistant"));
    }
     
    @DeleteMapping("/categories/{categorieId}/sandwichs/{sandwichId}")
    public ResponseEntity<?> deleteSandwich(@PathVariable("categorieId") String categorieId,
            @PathVariable("sandwichId") String sandwichId) {
        
        if (!cr.existsById(categorieId)) {
            throw new NotFound("Categorie inexistante");
        }
        return sr.findById(sandwichId)
                .map(sandwich -> {
                    sr.delete(sandwich);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Sandwich inexistant"));
    }
    
}
