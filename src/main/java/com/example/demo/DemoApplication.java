package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class DemoApplication {
	private final CustomerRepository customerRepository;

	public DemoApplication(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping
	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}

	record NewCustomerRequest(Integer age, String email, String name) {
	}

	@PostMapping
	public void addCustomer(@RequestBody NewCustomerRequest request) {
		Customer customer = new Customer();
		customer.setAge(request.age());
		customer.setEmail(request.email());
		customer.setName(request.name());
		customerRepository.save(customer);
	}

	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer id) {
		customerRepository.deleteById(id);
	}

	@PutMapping({ "customerId" })
	public void updateCustomer(@PathVariable("customerid") Integer id, @RequestBody NewCustomerRequest request) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            Customer updateCustomer = customer.get();
            if(request.age > 0){
				updateCustomer.setAge(request.age);
			}
			if(request.name != null){
				updateCustomer.setName(request.name);
			}
			if(request.email != null){
				updateCustomer.setEmail(request.email);
			}
			customerRepository.save(updateCustomer);

		}
		new Error("Customer not found");

	}

}
