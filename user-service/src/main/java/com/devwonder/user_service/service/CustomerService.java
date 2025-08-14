package com.devwonder.user_service.service;

import com.devwonder.user_service.dto.CustomerCreateRequest;
import com.devwonder.user_service.dto.CustomerResponse;
import com.devwonder.user_service.model.Customer;
import com.devwonder.user_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Transactional
    public Long createCustomer(CustomerCreateRequest request) {
        log.info("Creating customer for account ID: {}", request.getAccountId());
        
        Customer customer = new Customer();
        customer.setAccountId(request.getAccountId());
        
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created with accountId: {} for account: {}", savedCustomer.getAccountId(), request.getAccountId());
        
        return savedCustomer.getAccountId();
    }
    
    public List<CustomerResponse> getAllCustomers() {
        log.info("Retrieving all customers");
        
        return customerRepository.findAll().stream()
            .map(customer -> new CustomerResponse(
                customer.getAccountId(), // Use accountId as the id
                customer.getAccountId(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
            ))
            .toList();
    }
}