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
public class Sandwich {
    
    @Id
    private String id;
    private String nom;
    private String description;
    private String pain;
    private double tarifHT;
    
    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    @JsonIgnore
    private Categorie categorie;
    
    Sandwich() {
        // pour JPA
    }

    public Sandwich(String nom, String description, String pain, double tarifHT) {
        this.nom = nom;
        this.description = description;
        this.pain = pain;
        this.tarifHT = tarifHT;
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
    
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPain() {
        return this.pain;
    }

    public void setPain(String pain) {
        this.pain = pain;
    }
    
    public double getTarifHT() {
    	return this.tarifHT;
    }
    
    public  void setTarifHT(double tarifHT) {
    	this.tarifHT = tarifHT;
    }

    public Categorie getCategorie() {
        return this.categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    } 
    
}
