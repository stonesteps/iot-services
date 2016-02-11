package com.bwg.iot;

import io.swagger.annotations.*;
import com.bwg.iot.model.Spa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/spas", produces = {APPLICATION_JSON_VALUE})
@Api(value = "/spas", description = "the spas API")
public class SpasApi {

    @Autowired
    private SpaRepository spaRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Spa> createSpa(@RequestBody Map<String, Object> spaMap){
        Spa spa = new Spa(spaMap.get("serialNumber").toString(),
                spaMap.get("productName").toString(),
                spaMap.get("model").toString());
        spa.setDealerId(spaMap.get("dealerId").toString());
        spaRepository.save(spa);

        ResponseEntity<Spa> response = new ResponseEntity<Spa>(spa, HttpStatus.OK);
        return response;
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{spaId}")
    public ResponseEntity<Spa> updateSpa(@PathVariable ("spaId") String spaId,
                                         @RequestBody Map<String, Object> spaMap){
        Spa spa = new Spa(spaMap.get("serialNumber").toString(),
                spaMap.get("productName").toString(),
                spaMap.get("model").toString());
        spa.setId(spaId);
        spa = spaRepository.save(spa);

        ResponseEntity<Spa> response = new ResponseEntity<Spa>(spa, HttpStatus.OK);
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value="/{spaId}")
    public Spa getSpaDetails(@PathVariable("spaId") String spaId){
        return spaRepository.findOne(spaId);
    }

  @ApiOperation(value = "Finds Spas assigned to a specific dealer", notes = "Retrieves list of spas associated with the given dealer", response = Spa.class, responseContainer = "List")
  @io.swagger.annotations.ApiResponses(value = {
    @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation"),
    @io.swagger.annotations.ApiResponse(code = 400, message = "Invalid dealer ID") })
  @RequestMapping(value = "/data/findByDealer",
    produces = { "application/json", "application/xml" },
    method = RequestMethod.GET)
  public ResponseEntity<List<Spa>> findSpasByDealer(@ApiParam(value = "ID of the dealer to filter results", required = true) @RequestParam(value = "dealerId", required = true) Long dealerId)
      throws ChangeSetPersister.NotFoundException {
      // do some magic!
      System.out.println(" **** find by Dealer ****");
      List<Spa> spasFound = spaRepository.findAll();
      return new ResponseEntity<List<Spa>>(spasFound, HttpStatus.OK);
  }

}
