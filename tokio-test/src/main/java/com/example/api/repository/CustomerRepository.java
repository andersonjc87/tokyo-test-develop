package com.example.api.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.api.domain.Address;
import com.example.api.domain.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

    List<Customer> findAllByOrderByNameAsc();

    Set<Address> saveAll(Set<Address> add);

}
