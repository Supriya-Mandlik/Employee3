package com.Employees.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.Employees.entity.Employee;
import com.Employees.repository.EmployeeRepository;
import com.Employees.response.AddressResponse;
import com.Employees.response.EmployeeResponse;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository EmployeeRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WebClient webClient;
	
 //	@Autowired
 //	private DiscoveryClient discoveryClient;  // for setting dynamic uri
	
 //	@Autowired
 //	private LoadBalancerClient loadBalancerClient;
	
	@Autowired      // we can autowire it by creating Bean in config class
	private RestTemplate restTemplate;
	// or we can autowire it through constructor
	
	//@Value("${addressservice.base.url}")
	//private String addressBaseURL;
	
/*	public EmployeeService(@Value("${addressservice.base.url}") String addressBaseURL , RestTemplateBuilder builder) {
		this.restTemplate = builder
				           .rootUri(addressBaseURL)
				           .build();
	}*/
	
	public EmployeeResponse getEmployeeById(int id) {
		
		// db call -> fetch employee 1
		
		// employee -> EmployeeResponse
		// we can not expose our actual employee class to outer world
		// therefore we create EmployeeResponse class
		// now we are exposing employeeResponse object instead of employee object.
		
		Employee employee = EmployeeRepo.findById(id).get();  // DB call
		
		 EmployeeResponse employeeResponse = modelMapper.map(employee, EmployeeResponse.class);
		
		 
		//addressResponse -> set data by making a rest api call
		
		 // by using rest template
	//	AddressResponse addressResponse = restTemplate.getForObject("/address/{id}", AddressResponse.class, id);  // external rest api call
		
     AddressResponse addressResponse = callingAddressServiceUsingRestTemplate(id);
     
     employeeResponse.setAddressResponse(addressResponse); 
	 
	 return employeeResponse ;
	}
		 
		 // using web client or web flux
		 // reactive programming
		 // non blocking in nature
		 // dynamic call
		 
		 private AddressResponse callToAddressServiceUsingWebClient(int id) {
			 return webClient
                     .get()
                     .uri("/address/"+id)
                     .retrieve()
                     .bodyToMono(AddressResponse.class)
                     .block(); 
		 }
		
		 /*AddressResponse addressResponse = webClient
				                          .get()
				                          .uri("/address/"+id)
				                          .retrieve()
				                          .bodyToMono(AddressResponse.class)
				                          .block(); */
		 
		
		
		         // or
		 
//		EmployeeResponse employeeResponse = new EmployeeResponse();
//		employeeResponse.setId(employee.getId());
//		employeeResponse.setName(employee.getName());
//		employeeResponse.setEmail(employee.getEmail());
//		employeeResponse.setBloodgroup(employee.getBloodgroup());
// instead of writing all this we use ModelMapper 
		 
		 private AddressResponse callingAddressServiceUsingRestTemplate(int id) {
		
			 // get me the details for the ip and a port number for address service
			
		// it will always gives us only one instance	 
	//		 List<ServiceInstance> instances = discoveryClient.getInstances("Address");
			 
	//		 ServiceInstance serviceInstance = instances.get(0);
			 
	//		 String uri = serviceInstance.getUri().toString();
			 
			 // multiple instance + load balance
			 
		/*	 ServiceInstance serviceInstance = loadBalancerClient.choose("Address");
			 
			 String uri = serviceInstance.getUri().toString();
			 
			 String contextRoot = serviceInstance.getMetadata().get("configPath");
			 
			 System.out.println("uri >>>>>>>>>>>>>>>>>>>>>"+uri+contextRoot); 
			 
			 return restTemplate.getForObject(uri+contextRoot+"/address/{id}", AddressResponse.class, id);*/
			 
			 return restTemplate.getForObject("http://Address/Address/api/address/{id}", AddressResponse.class, id);
		 }
		
				
	}
        
	

