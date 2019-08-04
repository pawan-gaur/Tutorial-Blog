package com.pgaur.backend.api.controller;

import com.pgaur.backend.api.model.Customer;
import com.pgaur.backend.api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public List<Customer> customerList() {
        return customerService.findAll();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Customer customer = null;
        Map<String, Object> response = new HashMap<>();
        try {
            customer = customerService.findById(id);
        } catch (DataAccessException e) {

            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            response.put("message", "Error in querying database");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (customer == null) {
            response.put("message", "Customer Id : ".concat(id.toString().concat(" not found in database")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<?> create(@Valid @RequestBody Customer customer, BindingResult result) {
        Customer newCustomer = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            /*List<String> errors = new ArrayList<>();

            for (FieldError err : result.getFieldErrors()) {
                errors.add("Field '" + err.getField() + "' " + err.getDefaultMessage());
            }*/
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            newCustomer = customerService.save(customer);
        } catch (DataAccessException e) {
            response.put("message", "Error in inserting data");
            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("customer", newCustomer);
        response.put("message", "Customer has been created successfully");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Customer customer, @PathVariable Long id, BindingResult result) {
        Customer updatedCustomer = null;
        Map<String, Object> response = new HashMap<>();
        Customer currentCustomer = customerService.findById(id);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (customer == null) {
            response.put("message", "Error: could not be Updated, Customer Id : ".concat(id.toString().concat(" not found in database")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            currentCustomer.setFirstName(customer.getFirstName());
            currentCustomer.setLastName(customer.getLastName());
            currentCustomer.setEmail(customer.getEmail());
            currentCustomer.setCreateAt(customer.getCreateAt());

            updatedCustomer = customerService.save(currentCustomer);
        } catch (DataAccessException e) {
            response.put("message", "Error updating the customer in the database");
            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("customer", updatedCustomer);
        response.put("message", "Customer has been updated successfully");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            customerService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error remove the customer in the database");
            response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "Customer has been removed successfully");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
