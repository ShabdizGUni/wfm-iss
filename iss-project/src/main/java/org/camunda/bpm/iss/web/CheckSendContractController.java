package org.camunda.bpm.iss.web;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.engine.cdi.BusinessProcess;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.iss.ejb.CustomerService;
import org.camunda.bpm.iss.entity.Contract;
import org.camunda.bpm.iss.entity.Customer;
import org.camunda.bpm.iss.entity.CustomerRequest;

@Named
@ConversationScoped
public class CheckSendContractController implements Serializable{

	  private static  final long serialVersionUID = 1L;
	  
	  
	  private static Logger LOGGER = Logger.getLogger(CheckSendContractController.class.getName());
	  // Inject the BusinessProcess to access the process variables
	  @Inject
	  private BusinessProcess businessProcess;
	 
	  // Inject the EntityManager to access the persisted order
	  @PersistenceContext
	  private EntityManager entityManager;
	 
	  // Inject the Service 

	  @Inject
	  private CustomerService customerService;
	 
	 
	  // Caches the Entities during the conversation
	  private Customer customerEntity;
	  private Contract contractEntity;
	  
	  private DelegateExecution delegateExecution;
	  
	  
	  public Customer getCustomerEntity() {
		    if (customerEntity == null) {
		      // Load the entity from the database if not already cached
		    LOGGER.log(Level.INFO, "This is customerId from businessProcess: " + businessProcess.getVariable("customerId")); 
		    LOGGER.log(Level.INFO, "This is the same casted as Long: " + (Long) businessProcess.getVariable("customerId"));
		    LOGGER.log(Level.INFO, "This is getCustomer from the Service invoked with it " + customerService.getCustomer((Long) businessProcess.getVariable("customerId")));
		    customerEntity = customerService.getCustomer((Long) businessProcess.getVariable("customerId"));
		    }
		    return customerEntity;
	  }
	  
	  public Contract getContractEntity() {
		  if (contractEntity == null){
			  LOGGER.log(Level.INFO, "This is contractId from businessProcess: " + businessProcess.getVariable("contractId")); 
			  LOGGER.log(Level.INFO, "This is the same casted as Long: " + (Long) businessProcess.getVariable("contractId"));
			  LOGGER.log(Level.INFO, "This is getContract from the Service invoked with it " + contractService.getContract((Long) businessProcess.getVariable("contractId")));
			    contractEntity = contractService.getContract((Long) businessProcess.getVariable("contractId"));
			    }
			    return contractEntity;  
	  }
}	 
	