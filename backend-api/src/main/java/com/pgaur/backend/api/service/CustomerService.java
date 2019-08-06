package com.pgaur.backend.api.service;

import com.pgaur.backend.api.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    Page<Customer> findAll(Pageable pageable);

    Customer findById(Long id);

    Customer save(Customer customer);

    void delete(Long id);

}
