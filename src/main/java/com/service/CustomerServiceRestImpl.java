package com.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.entity.Customer;

@Service
public class CustomerServiceRestImpl implements CustomerService{
	
	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	private RestTemplate restTemplate;

	@Value("${crm.rest.url}")
	private String restUrl;
	
	@Override
	public List<Customer> getCustomers() {
		logger.info("in getCustomers(): Calling REST API "
				+ restUrl);
		
		//Making REST calls
		ResponseEntity<List<Customer>> responseEntity = 
				restTemplate.exchange(restUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {} );
		//Get list of customers from responseEntity
		List<Customer> customers = responseEntity.getBody();
		
		logger.info("in getCustomers(): customers" + customers);
		return customers;
	}

	@Override
	public void saveCustomer(Customer customer) {
		logger.info("in saveCustomers(): Calling REST API "
				+ restUrl);
		
		int id = customer.getId();
		if(id==0) {
			//add Customer
			restTemplate.postForEntity(restUrl, customer, String.class);
		}
		else {
			//update Customer
			restTemplate.put(restUrl, customer);
		}
		
		logger.info("in saveCustomer(): success");
	}

	@Override
	public Customer getCustomer(int theId) {
		logger.info("in getCustomer(): Calling REST API "
				+ restUrl);
		//Make REST Call
		Customer customer = restTemplate.getForObject(restUrl+"/"+theId,Customer.class );
		
		logger.info("in saveCustomer(): theCustomer="
				 + customer);
		return customer;
	}

	@Override
	public void deleteCustomer(int theId) {
		logger.info("in deleteCustomer(): Calling REST API "
				+ restUrl);
		//Make REST call
		restTemplate.delete(restUrl + "/" +theId);
		
		logger.info("in deleteCustomer(): deleted customer theId="
				+ theId);
	}

}
