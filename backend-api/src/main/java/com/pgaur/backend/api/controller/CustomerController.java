package com.pgaur.backend.api.controller;

import com.pgaur.backend.api.model.Customer;
import com.pgaur.backend.api.service.CustomerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	private final Logger log = LoggerFactory.getLogger(CustomerController.class);

	@GetMapping("/customers")
	public List<Customer> customerList() {
		return customerService.findAll();
	}

	@GetMapping("/customers/page/{page}")
	public Page<Customer> customerList(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return customerService.findAll(pageable);
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
			/*
			 * List<String> errors = new ArrayList<>();
			 * 
			 * for (FieldError err : result.getFieldErrors()) { errors.add("Field '" +
			 * err.getField() + "' " + err.getDefaultMessage()); }
			 */
			List<String> errors = result.getFieldErrors().stream()
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
	public ResponseEntity<?> update(@Valid @RequestBody Customer customer, @PathVariable Long id,
			BindingResult result) {
		Customer updatedCustomer = null;
		Map<String, Object> response = new HashMap<>();
		Customer currentCustomer = customerService.findById(id);

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "Field '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer == null) {
			response.put("message", "Error: could not be Updated, Customer Id : "
					.concat(id.toString().concat(" not found in database")));
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
			Customer customer = customerService.findById(id);
			String previousPhotoName = customer.getPhoto();

			if (previousPhotoName != null && previousPhotoName.length() > 0) {
				Path newPhotoName = Paths.get("uploads").resolve(previousPhotoName).toAbsolutePath();
				File archievePhotoName = newPhotoName.toFile();
				if (archievePhotoName.exists() && archievePhotoName.canRead()) {
					archievePhotoName.delete();
				}
			}

			customerService.delete(id);
		} catch (DataAccessException e) {
			response.put("message", "Error remove the customer in the database");
			response.put("error", e.getMessage().concat(" : ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "Customer has been removed successfully");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@PostMapping("/customers/upload")
	public ResponseEntity<?> upload(@RequestParam("archive") MultipartFile archive, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();

		Customer customer = customerService.findById(id);

		if (!archive.isEmpty()) {
			String firstNameArchive = UUID.randomUUID().toString() + "_"
					+ archive.getOriginalFilename().replace(" ", "");
			Path routeArchieve = Paths.get("uploads").resolve(firstNameArchive).toAbsolutePath();
			log.info(routeArchieve.toString());
			try {
				Files.copy(archive.getInputStream(), routeArchieve);
			} catch (IOException e) {
				response.put("message", "Error uploading client image : " + firstNameArchive);
				response.put("error", e.getMessage().concat(" : ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			String previousPhotoName = customer.getPhoto();

			if (previousPhotoName != null && previousPhotoName.length() > 0) {
				Path newPhotoName = Paths.get("uploads").resolve(previousPhotoName).toAbsolutePath();
				File archievePhotoName = newPhotoName.toFile();
				if (archievePhotoName.exists() && archievePhotoName.canRead()) {
					archievePhotoName.delete();
				}
			}

			customer.setPhoto(firstNameArchive);
			customerService.save(customer);

			response.put("customer", customer);
			response.put("message", "You have successfully uploaded the image : " + firstNameArchive);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@GetMapping("uploads/img/{photoName:.+}")
	public ResponseEntity<Resource> showPhoto(@PathVariable String photoName) {

		Path routeArchieve = Paths.get("uploads").resolve(photoName).toAbsolutePath();
		log.info(routeArchieve.toString());
		Resource resource = null;

		try {
			resource = new UrlResource(routeArchieve.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		if (!resource.exists() && !resource.isReadable()) {
			throw new RuntimeException("Error failed to load image");
		}
		HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

		return new ResponseEntity<Resource>(resource, HttpStatus.OK);
	}

}
