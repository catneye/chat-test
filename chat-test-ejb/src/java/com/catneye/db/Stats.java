/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author plintus
 */
@Entity
@Table(name = "stats", catalog = "chat", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stats.findAll", query = "SELECT s FROM Stats s"),
    @NamedQuery(name = "Stats.findById", query = "SELECT s FROM Stats s WHERE s.id = :id"),
    @NamedQuery(name = "Stats.findByWord", query = "SELECT s FROM Stats s WHERE s.word = :word"),
    @NamedQuery(name = "Stats.findByNdoc", query = "SELECT s FROM Stats s WHERE s.ndoc = :ndoc"),
    @NamedQuery(name = "Stats.findByNentry", query = "SELECT s FROM Stats s WHERE s.nentry = :nentry")})
public class Stats implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Column(name = "id", precision = 17, scale = 17)
    private Double id;
    @Size(max = 2147483647)
    @Column(name = "word", length = 2147483647)
    private String word;
    @Column(name = "ndoc")
    private Integer ndoc;
    @Column(name = "nentry")
    private Integer nentry;

    public Stats() {
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getNdoc() {
        return ndoc;
    }

    public void setNdoc(Integer ndoc) {
        this.ndoc = ndoc;
    }

    public Integer getNentry() {
        return nentry;
    }

    public void setNentry(Integer nentry) {
        this.nentry = nentry;
    }
    
    @Override
    public String toString() {
        return new StringBuilder()
                .append(id).append("; ")
                .append(word).append("; ")
                .append(ndoc).append("; ")
                .append(nentry).append("; ")
                .toString();
    }
}
