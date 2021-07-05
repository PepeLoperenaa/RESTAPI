package com.restful.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restful.api.domain.Message;
import com.restful.api.domain.amazon.AmazonMovie;
import com.restful.api.domain.disney.DisneyMovie;
import com.restful.api.domain.netflix.NetflixMovie;
import com.restful.api.repository.amazon.AmazonMovieRepository;
import com.restful.api.repository.disney.DisneyMovieRepository;
import com.restful.api.repository.netflix.NetflixMovieRepository;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:80")
@RestController
@RequestMapping("movies")
public class MovieController {

    @Value("classpath:AmazonJSONSchema.json") //schemas for validation.
    private Resource jsonSchemaAmazon;
    @Value("classpath:JSONSchemaNetflix.json")
    private Resource jsonSchemaNetflix;
    @Value("classpath:DisneyJSONSchema.json")
    private Resource jsonSchemaDisney;

    //
    @Value("classpath:NetflixXSDSchema.xsd")
    private Resource xsdSchemaNetflix;

    @Autowired
    private AmazonMovieRepository amazonMovieRepository;

    @Autowired
    private NetflixMovieRepository netflixMovieRepository;

    @Autowired
    private DisneyMovieRepository disneyMovieRepository;


    /*
        GET
        http://127.0.0.1:8080/movies/amazon?format=json
        http://127.0.0.1:8080/movies/amazon?format=xml
        return all amazon movies without the visualisation
        http://127.0.0.1:8080/movies/disney?format=json
        http://127.0.0.1:8080/movies/disney?format=xml
        return all disney movies without the visualisation
        http://127.0.0.1:8080/movies/netflix?format=json
        http://127.0.0.1:8080/movies/netflix?format=xml
        return all netflix movies without the visualisation
     */

    @GetMapping(value = "/amazon/histogram", produces = {"application/json"})
    public Map<String, Long> histogramGenresAmazon() {
        List<AmazonMovie> movies = amazonMovieRepository.findAll();
        //Map<String,Long> map = movies.stream().collect(Collectors.groupingBy(AmazonMovie::getGenre,Collectors.counting()));
        Map<String, Long> map = new HashMap<>();
        for (AmazonMovie a : movies) {
            String[] genres = a.getGenre().split(",");
            for (String g : genres) {
                if (!map.containsKey(g.trim())) {
                    map.put(g.trim(), 1L);
                } else {
                    Long count = map.get(g.trim()) + 1;
                    map.put(g.trim(), count);
                }
            }
        }
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    @GetMapping(value = "/disney/histogram", produces = {"application/json"})
    public Map<String, Long> histogramGenresDisney() {
        List<DisneyMovie> movies = disneyMovieRepository.findAll();
        //Map<String,Long> map = movies.stream().collect(Collectors.groupingBy(AmazonMovie::getGenre,Collectors.counting()));
        Map<String, Long> map = new HashMap<>();
        for (DisneyMovie d : movies) {
            String[] genres = d.getGenre().split(",");
            for (String g : genres) {
                if (!map.containsKey(g.trim())) {
                    map.put(g.trim(), 1L);
                } else {
                    Long count = map.get(g.trim()) + 1;
                    map.put(g.trim(), count);
                }
            }
        }
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @GetMapping(value = "/netflix/histogram", produces = {"application/json"})
    public Map<String, Long> histogramGenresNetflix() {
        List<NetflixMovie> movies = netflixMovieRepository.findAll();
        //Map<String,Long> map = movies.stream().collect(Collectors.groupingBy(AmazonMovie::getGenre,Collectors.counting()));
        Map<String, Long> map = new HashMap<>();
        for (NetflixMovie n : movies) {
            String[] genres = n.getGenre().split(",");
            for (String g : genres) {
                if (!map.containsKey(g.trim())) {
                    map.put(g.trim(), 1L);
                } else {
                    Long count = map.get(g.trim()) + 1;
                    map.put(g.trim(), count);
                }
            }
        }
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    @GetMapping(value = "/piechart", produces = {"application/json"})
    public Map<String, Integer> countAllFilms() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Amazon",amazonMovieRepository.findAll().size());
        map.put("Disney",disneyMovieRepository.findAll().size());
        map.put("Netflix",netflixMovieRepository.findAll().size());

        return map;
    }

    /**
     * Show all of the Amazon movies
     *
     * @return All of the amazon movies on both XML and JSON format.
     */
    @GetMapping(value = "/amazon", produces = {"application/json", "application/xml"})
    public List<AmazonMovie> findAmazonMovies() {
        return amazonMovieRepository.findAll();
    }


    /**
     * Show all of the Disney movies
     *
     * @return All of the disney movies on both XML and JSON format.
     */
    @GetMapping(value = "/disney", produces = {"application/json", "application/xml"})
    public List<DisneyMovie> findDisneyMovies() {
        return disneyMovieRepository.findAll();
    }

    /**
     * Show all of the Netflix movies
     *
     * @return All of the netflix movies on both XML and JSON format.
     */
    @GetMapping(value = "/netflix", produces = {"application/json", "application/xml"})
    public List<NetflixMovie> findNetflixMovies() {
        return netflixMovieRepository.findAll();
    }

    /**
     * Returning a specific Amazon film found by ID
     *
     * @param id
     * @return A specific Amazon film found by ID
     */
    @GetMapping(value = "/amazon/{id}")
    public AmazonMovie getAmazonMovie(@PathVariable("id") int id) {
        return amazonMovieRepository.findById(id).orElse(null);
    }

    /**
     * Returning a specific Disney film found by ID
     *
     * @param id
     * @return A specific Disney film found by ID
     */
    @GetMapping(value = "/disney/{id}")
    public DisneyMovie getDisneyMovie(@PathVariable("id") int id) {
        return disneyMovieRepository.findById(id).orElse(null);
    }

    /**
     * Returning a specific Netflix film found by ID
     *
     * @param id
     * @return A specific Netflix film found by ID
     */
    @GetMapping(value = "/netflix/{id}")
    public NetflixMovie getNetflixMovie(@PathVariable("id") int id) {
        return netflixMovieRepository.findById(id).orElse(null);
    }

    /**
     * Inserting an Amazon movie into the Database via the API
     *
     * @param movie
     * @return
     */
    @PostMapping("/amazon")
    public ResponseEntity<Message> insertAmazon(@RequestBody String movie) {

        boolean okSchema = movieValidator(movie, jsonSchemaAmazon);
        if (okSchema) {

            AmazonMovie m = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                m = objectMapper.readValue(movie, AmazonMovie.class);

            } catch (JsonProcessingException ex) {

                return new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }


            m = amazonMovieRepository.save(m); //adding the movie into the database

            if (m != null)
                return new ResponseEntity<>(new Message("OK"), HttpStatus.OK);
            else
                return new ResponseEntity<>(new Message("Could not save movie"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Message("Validation error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Inserting an Disney movie into the Database via the API
     *
     * @param movie
     * @return
     */
    @PostMapping("/disney")
    public ResponseEntity<Message> insertDisney(@RequestBody String movie) {

        boolean okSchema = movieValidator(movie, jsonSchemaDisney);
        if (okSchema) {

            DisneyMovie m = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                m = objectMapper.readValue(movie, DisneyMovie.class);

            } catch (JsonProcessingException ex) {

                return new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }


            m = disneyMovieRepository.save(m); //adding the movie into the database

            if (m != null)
                return new ResponseEntity<>(new Message("OK"), HttpStatus.OK);
            else
                return new ResponseEntity<>(new Message("Could not save movie"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Message("Validation error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Inserting an Netflix movie into the Database via the API
     *
     * @param movie
     * @return
     */
    @PostMapping("/netflix")
    public ResponseEntity<Message> insertNetflix(@RequestBody String movie) {

        //
        boolean okSchemaXSD = movieValidatorXML(movie, xsdSchemaNetflix);
        //

        boolean okSchema = movieValidator(movie, jsonSchemaNetflix);
        if (okSchema) {

            NetflixMovie m = null;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                m = objectMapper.readValue(movie, NetflixMovie.class);

            } catch (JsonProcessingException ex) {

                return new ResponseEntity<>(new Message(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }

            m = netflixMovieRepository.save(m); //adding the movie into the database

            if (m != null)
                return new ResponseEntity<>(new Message("OK"), HttpStatus.OK);
            else
                return new ResponseEntity<>(new Message("Could not save movie"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new Message("Validation error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Editing a Amazon movie which is already inside the database.
     *
     * @param movie
     * @return Boolean true if it worked or boolean false if the edition has not worked
     */
    @PutMapping("/amazon")
    public ResponseEntity<String> editAmazon(@RequestBody AmazonMovie movie) {

        Boolean status = Boolean.FALSE;
        AmazonMovie m = amazonMovieRepository.findById(movie.getId()).orElse(null); //adding the movie into the database

        if (m != null) {
            m = amazonMovieRepository.save(movie);
            if (m != null)
                status = Boolean.TRUE;
        }

        if (status) {
            return new ResponseEntity<>("Edit correctly", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not edit correctly", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /**
     * Editing a Disney movie which is already inside the database.
     *
     * @param movie
     * @return Boolean true if it worked or boolean false if the edition has not worked
     */
    @PutMapping("/disney")
    public ResponseEntity<String> editDisney(@RequestBody DisneyMovie movie) {

        Boolean status = Boolean.FALSE;
        DisneyMovie m = disneyMovieRepository.findById(movie.getId()).orElse(null); //adding the movie into the database

        if (m != null) {
            m = disneyMovieRepository.save(movie);
            if (m != null)
                status = Boolean.TRUE;
        }

        if (status) {
            return new ResponseEntity<>("Edit correctly", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not edit correctly", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Editing a Netflix movie which is already inside the database.
     *
     * @param movie
     * @return Boolean true if it worked or boolean false if the edition has not worked
     */
    @PutMapping("/netflix")
    public ResponseEntity<String> editNetflix(@RequestBody NetflixMovie movie) {

        Boolean status = Boolean.FALSE;
        NetflixMovie m = netflixMovieRepository.findById(movie.getId()).orElse(null); //adding the movie into the database

        if (m != null) {
            m = netflixMovieRepository.save(movie);
            if (m != null)
                status = Boolean.TRUE;
        }

        if (status) {
            return new ResponseEntity<>("Edit correctly", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not edit correctly", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deleting Amazon film from the Database
     *
     * @param id
     * @return Boolean true if it worked or boolean false if the edition has not worked
     */
    @DeleteMapping("/amazon/{id}")
    public ResponseEntity<String> deleteAmazon(@PathVariable("id") int id) {
        AmazonMovie m = amazonMovieRepository.findById(id).orElse(null);
        if (m != null) {
            amazonMovieRepository.delete(m);
            return new ResponseEntity<>("Deleted correctly", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Could not delete correctly", HttpStatus.INTERNAL_SERVER_ERROR);


    }

    /**
     * Deleting Disney film from the Database
     *
     * @param id
     * @return Boolean true if it worked or boolean false if the edition has not worked
     */
    @DeleteMapping("/disney/{id}")
    public ResponseEntity<String> deleteDisney(@PathVariable("id") int id) {
        DisneyMovie m = disneyMovieRepository.findById(id).orElse(null);
        if (m != null) {
            disneyMovieRepository.delete(m);
            return new ResponseEntity<>("Deleted correctly", HttpStatus.OK);
        }

        return new ResponseEntity<>("Could not delete correctly", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deleting Netflix film from the Database
     *
     * @param id
     * @return Boolean true if it worked or boolean false if the edition has not worked
     */
    @DeleteMapping("/netflix/{id}")
    public ResponseEntity<String> deleteNetflix(@PathVariable("id") int id) {
        NetflixMovie m = netflixMovieRepository.findById(id).orElse(null);
        if (m != null) {
            netflixMovieRepository.delete(m);
            return new ResponseEntity<>("Deleted correctly", HttpStatus.OK);
        }

        return new ResponseEntity<>("Could not delete correctly", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private boolean movieValidator(String data, Resource r) {
        try {
            JSONObject jsonSchema = new JSONObject(
                    new JSONTokener(r.getInputStream()));


            JSONObject jsonSubject = new JSONObject(data);

            Schema schema = SchemaLoader.load(jsonSchema);


            schema.validate(jsonSubject);

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private boolean movieValidatorXML(String data, Resource r)
    {
        try
        {
            InputStream xml = new ByteArrayInputStream(data.getBytes());
            InputStream xsd = r.getInputStream();
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            javax.xml.validation.Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }

}
