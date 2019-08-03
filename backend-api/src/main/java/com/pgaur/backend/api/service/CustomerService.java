package com.pgaur.backend.api.service;

import com.pgaur.backend.api.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> findAll();

    Customer findById(Long id);

    Customer save(Customer customer);

    void delete(Long id);

}
