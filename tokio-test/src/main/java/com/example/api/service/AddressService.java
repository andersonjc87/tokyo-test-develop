package com.example.api.service;

import java.util.Set;

import com.example.api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;

@Service
public class AddressService {

    private CustomerRepository repository;

    public Set<Address> findByCustomer(Customer cust) {
        return null;
    }

    public void save(Address item) {

    }
}
