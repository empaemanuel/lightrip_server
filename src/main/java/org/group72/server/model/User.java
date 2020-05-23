package org.group72.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String name;

    private String email;

    //Nog inte så bra ide att spara denna här eftersom routes redan finns sparade i Route tabellen
    // ihop med användaren. //Emil
//    private Route[] routes;

    public Integer getId() {
        return id;
    }

//    public Array<Route> getRoutes() {
//        return routes;
//    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public void addRoute(Route route) {
//        routes.append(route);
//    }
}