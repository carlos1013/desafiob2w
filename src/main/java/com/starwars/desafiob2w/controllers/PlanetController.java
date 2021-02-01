package com.starwars.desafiob2w.controllers;

import com.starwars.desafiob2w.model.Planet;
import com.starwars.desafiob2w.utils.PlanetUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Vector;

@RestController
@RequestMapping("/planets")
public class PlanetController {

    /** Method to get a single planet, based on id or name
     *
     * example url:
     * .../planets?name=Mandalore
     *
     * @param  id  id of the requested planet
     * @param  name name of the requested planet
     * @return      a planet, if found
     */
    @GetMapping()
    public ResponseEntity<Object> getPlanet(
            @Param("id") String id,
            @Param("name") String name) {
        //we don't have any valid parameter, return this as a bad request
        if ((id == null || id.length() < 1) && (name == null || name.length() < 1)){
            return ResponseEntity.badRequest().build();
        }

        try {
            Planet planet =  PlanetUtils.getPlanetFromParameters(id, name);
            if (planet != null) {
                return ResponseEntity.ok(planet);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception exp){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp.getMessage());
        }
    }

    /** Method to get a list of planets, in this case we don't use parameters yet
     *
     * example url:
     * .../planets/getplanets
     *
     * @return      a list of all planets in the collection
     */
    @GetMapping("getplanets")
    public ResponseEntity<Object> getPlanets() {
        try {
            Vector<Planet> lstPlanets = PlanetUtils.getPlanets();
            if (lstPlanets != null){
                return ResponseEntity.ok(lstPlanets);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception exp){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp.getMessage());
        }
    }

    /** Method to insert a new planet
     *
     * @param  planet  an instance of planet with name, terrain and climate
     * @return      uri of the new object and the complete object with new id
     */
    @PostMapping()
    public ResponseEntity<Object> addPlanet(@RequestBody Planet planet) {
        if (planet == null){
            return ResponseEntity.badRequest().build();
        }

        try {
            Planet newPlanet = PlanetUtils.insertPlanet(planet);
            if (newPlanet != null) {
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newPlanet.getId())
                        .toUri();

                return ResponseEntity.created(uri).body(newPlanet);
            }

            return ResponseEntity.unprocessableEntity().build();
        }
        catch (Exception exp){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp.getMessage());
        }
    }

    /** Method to delete a planet based on id
     *
     * example url:
     * .../planets/{id}
     *
     * @param  id  id of the planet
     * @return      none, only an accepted if ok
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlanet(@PathVariable String id) {
        try {
            PlanetUtils.deletePlanetById(id);
            return ResponseEntity.accepted().build();
        }
        catch (Exception exp){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exp.getMessage());
        }
    }
}
