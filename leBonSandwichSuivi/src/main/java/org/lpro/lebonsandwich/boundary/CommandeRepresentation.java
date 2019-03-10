package org.lpro.lebonsandwich.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.lpro.lebonsandwich.entity.Commande;
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
@RequestMapping(value = "/commandes", produces = MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Commande.class)
/**
 *
 * @author dufour76u
 */
public class CommandeRepresentation {

    private final CommandeResource cr;

    public CommandeRepresentation(CommandeResource cr) {
        this.cr = cr;
    }

    @GetMapping
    public ResponseEntity<?> getAllCommandes() {
        Iterable<Commande> allCommandes = cr.findAll();
        return new ResponseEntity<>(commande2Resource(allCommandes), HttpStatus.OK);
    }
    
    private Resources<Resource<Commande>> commande2Resource(Iterable<Commande> commandes) {
        Link selfLink = linkTo(CommandeRepresentation.class).withSelfRel();
        List<Resource<Commande>> commandeResources = new ArrayList();
        commandes.forEach(commande
                -> commandeResources.add(commandeToResource(commande, false)));
        return new Resources<>(commandeResources, selfLink);
    }
    
    private Resource<Commande> commandeToResource(Commande commande, Boolean collection) {
        Link selfLink = linkTo(CommandeRepresentation.class)
                .slash(commande.getId())
                .withSelfRel();
        if (collection) {
        Link collectionLink = linkTo(CommandeRepresentation.class).withRel("collection");
        return new Resource<>(commande, selfLink, collectionLink);
    } else {
            return new Resource<>(commande, selfLink);
        }
    }
    
    
    @GetMapping(value = "/{commandeId}")
    public ResponseEntity<?> getOne(@PathVariable("commandeId") String id)
            throws NotFound {
        return Optional.ofNullable(cr.findById(id))
                .filter(Optional::isPresent)
                .map(commande -> new ResponseEntity<>(commandeToResource(commande.get(),false), HttpStatus.OK))
                .orElseThrow(() -> new NotFound("Commande inexsitante"));
    }


    @PostMapping
    public ResponseEntity<?> postMethod(@RequestBody Commande commande) {
    	commande.setId(UUID.randomUUID().toString());
        Commande saved = cr.save(commande);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(linkTo(CommandeRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{commandeId}")
    public ResponseEntity<?> putMethod(@PathVariable("commandeId") String id,
            @RequestBody Commande commandeUpdated) throws NotFound {
        return cr.findById(id)
                .map(commande -> {
                	commande.setId(commandeUpdated.getId());
                    cr.save(commande);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("commande inexistante"));
    }

    @DeleteMapping(value = "/{commandeId}")
    public ResponseEntity<?> deleteMethod(@PathVariable("commandeId") String id) throws NotFound {
        return cr.findById(id)
                .map(commande -> {
                    cr.delete(commande);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }).orElseThrow(() -> new NotFound("commande inexistante"));
    }
}
