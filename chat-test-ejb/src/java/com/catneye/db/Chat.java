/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catneye.db;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "chat", catalog = "chat", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Chat.findAll", query = "SELECT c FROM Chat c"),
    @NamedQuery(name = "Chat.findAllOrder", query = "SELECT c FROM Chat c order by c.adddate"),
    @NamedQuery(name = "Chat.findById", query = "SELECT c FROM Chat c WHERE c.id = :id"),
    @NamedQuery(name = "Chat.findByAdddate", query = "SELECT c FROM Chat c WHERE c.adddate = :adddate"),
    @NamedQuery(name = "Chat.findByUsers", query = "SELECT c FROM Chat c WHERE c.users = :users"),
    @NamedQuery(name = "Chat.findByTexts", query = "SELECT c FROM Chat c WHERE c.texts = :texts")})
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "adddate", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime adddate;
    @Size(max = 256)
    @Column(name = "users", nullable = false, length = 256)
    private String users;
    @Size(max = 2147483647)
    @Column(name = "texts", nullable = false, length = 2147483647)
    private String texts;

    public Chat() {
    }

    public Chat(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getAdddate() {
        return adddate;
    }

    public void setAdddate(LocalDateTime adddate) {
        this.adddate = adddate;
    }

    public String getUser() {
        return users;
    }

    public void setUser(String users) {
        this.users = users;
    }

    public String getTexts() {
        return texts;
    }

    public void setTexts(String texts) {
        this.texts = texts;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chat)) {
            return false;
        }
        Chat other = (Chat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(id).append("; ")
                .append(adddate).append("; ")
                .append(users).append("; ")
                .append(texts).append("; ")
                .toString();
    }
    
}
