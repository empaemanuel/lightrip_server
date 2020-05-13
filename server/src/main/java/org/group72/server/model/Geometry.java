//package org.group72.server.model;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import org.group72.server.model.LightNode;
//import javax.persistence.*;
//
//import java.util.List;
//
//
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "coordinates" })
//@Entity
//public class Geometry {
//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private int id;
//	
//	@JsonProperty("coordinates")
//    @ElementCollection( targetClass = Double.class )
//	private List<Double> coordinates = null;
//
//	@JsonProperty("coordinates")
//	public List<Double> getCoordinates() {
//		return coordinates;
//	}
//
//	@JsonProperty("coordinates")
//	public void setCoordinates(List<Double> coordinates) {
//		this.coordinates = coordinates;
//	}
//
//}
