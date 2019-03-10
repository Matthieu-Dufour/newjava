package org.lpro.lebonsandwich.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
/**
 * @author dufour76u
 */
public class Item {
    
    @Id
    private String id;
    private String nom;
    private String uri;
    private int quantite;
    
    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "commande_id", nullable = false)
    @JsonIgnore
    private Commande commande;
    
    Item() {
        // pour JPA
    }

    public Item(String nom, String uri, int qte) {
        this.nom = nom;
        this.uri = uri;
        this.quantite = quantite;
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
    
    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    
    public int getQuantite() {
        return this.quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public Commande getCommande() {
        return this.commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    } 
    
}
