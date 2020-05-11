package org.group72.server.dao;

import org.group72.server.model.LightNode;
import org.group72.server.model.Geometry;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface LightRepository extends CrudRepository<LightNode, List<Double>> {


}