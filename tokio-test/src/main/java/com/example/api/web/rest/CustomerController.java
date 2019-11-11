package com.example.api.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.service.AddressService;
import com.example.api.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerService service;
	private AddressService addrService;

	@Autowired
	public CustomerController(CustomerService service, AddressService addrService) {
		this.service = service;
		this.addrService = addrService;
	}

	@GetMapping
	public Page<Customer> findAll(Pageable pg) {
		Page<Customer> res = service.findAllPage(pg);
		res.forEach(cust -> {
			cust.setAddresses(addrService.findByCustomer(cust));
		});
		return res;
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
	}
	
	@PostMapping
	public Customer save(@RequestBody @Valid Customer customer) {
		Customer c = service.save(customer);
		if (c.getAddresses() != null) {
			c.getAddresses().forEach(item -> {
				addrService.save(item);
			});
		}
		return c;
	}
	
	@DeleteMapping("/{id}")
	public Customer delete(@PathVariable Long id) {
		Customer c = service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
		service.deleteById(id);
		return c;
	}
	
}
