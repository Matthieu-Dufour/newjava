package org.lpro.lebonsandwich.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.lpro.lebonsandwich.entity.Categorie;
import org.lpro.lebonsandwich.exception.NotFound;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Categorie.class)
/**
 *
 * @author dufour76u
 */
public class CategorieRepresentation {

    private final CategorieResource cr;
    private final SandwichResource sr;

    public CategorieRepresentation(CategorieResource cr, SandwichResource sr) {
        this.cr = cr;
        this.sr = sr;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        Iterable<Categorie> allCategories = cr.findAll();
        return new ResponseEntity<>(categorie2Resource(allCategories), HttpStatus.OK);
    }
    
    private Resources<Resource<Categorie>> categorie2Resource(Iterable<Categorie> categories) {
        Link selfLink = linkTo(CategorieRepresentation.class).withSelfRel();
        List<Resource<Categorie>> categorieResources = new ArrayList();
        categories.forEach(categorie
                -> categorieResources.add(categorieToResource(categorie, false)));
        return new Resources<>(categorieResources, selfLink);
    }
    
    private Resource<Categorie> categorieToResource(Categorie categorie, Boolean collection) {
        Link selfLink = linkTo(CategorieRepresentation.class)
                .slash(categorie.getId())
                .withSelfRel();
        if (collection) {
        Link collectionLink = linkTo(CategorieRepresentation.class).withRel("collection");
        Link sandwichsLink = linkTo(CategorieRepresentation.class).slash(categorie.getId()).slash("sandwichs").withRel("sandwichs");
        return new Resource<>(categorie, selfLink, collectionLink,sandwichsLink);
    } else {
            return new Resource<>(categorie, selfLink);
        }
    }
    
    
    @GetMapping(value = "/{categorieId}")
    public ResponseEntity<?> getOne(@PathVariable("categorieId") String id)
            throws NotFound {
        return Optional.ofNullable(cr.findById(id))
                .filter(Optional::isPresent)
                .map(categorie -> new ResponseEntity<>(categorieToResource(categorie.get(),false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Categorie inexsitante"));
    }

    @GetMapping("/{id}/sandwichs")
    public ResponseEntity<?> getSandwichByCategorieId(@PathVariable("id") String id)
            throws NotFound {
        if (!cr.existsById(id)) {
            throw new NotFound("Categorie inexistante");
        }
        return new ResponseEntity<>(sr.findByCategorieId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Categorie categorie) {
        categorie.setId(UUID.randomUUID().toString());
        Categorie saved = cr.save(categorie);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(CategorieRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{categorieId}")
    public ResponseEntity<?> putMethod(@PathVariable("categorieId") String id,
            @RequestBody Categorie categorieUpdated) throws NotFound {
        return cr.findById(id)
                .map(categorie -> {
                    categorie.setId(categorieUpdated.getId());
                    cr.save(categorie);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Categorie inexistante"));
    }

    @DeleteMapping(value = "/{categorieId}")
    public ResponseEntity<?> deleteMethod(@PathVariable("categorieId") String id) throws NotFound {
        return cr.findById(id)
                .map(categorie -> {
                    cr.delete(categorie);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("Categorie inexistante"));
    }
}
