package org.group72.server.model;

import javax.persistence.*;
import java.io.Serializable;

/**
*
*
*
*@Author Magnus P.
* */

@Entity // This tells Hibernate to make a table out of this class
public class Route {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private Integer ownedBy;

    public Route(){
    }

    public Integer getId() {
        return id;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
    }

    @Override
    public String toString(){
        return "ID:"+getId() + ","+"OwnedBy:"+ getOwnedBy();
    }


}