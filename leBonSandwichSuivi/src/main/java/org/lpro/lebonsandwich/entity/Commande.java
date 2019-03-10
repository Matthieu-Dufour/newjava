package org.lpro.lebonsandwich.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
/**
 *
 * @author dufour76u
 */
public class Commande {

    @Id
    private String id;
    private String nom;
    private String description;

    @OneToMany(mappedBy="commande", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Item> items;
    
    Commande() {
        // necessaire pour JPA !!!!
    }
    
    public Commande (String nom, String description) {
        this.nom = nom;
        this.description = description;
    }
    
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription(){
        return this.description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public Set<Item> getitems() {
        return this.items;
    }

    public void setProjets(Set<Item> items) {
        this.items = items;
    }
    
    
    
    
    
}
