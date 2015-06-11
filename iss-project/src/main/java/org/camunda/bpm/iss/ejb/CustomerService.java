package org.camunda.bpm.iss.ejb;

import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.iss.entity.Customer;

@Stateless
@Named
public class CustomerService {

	 @PersistenceContext
	  private EntityManager entityManager;
	  
	  // Inject task form available through the camunda cdi artifact
	  @Inject
	  private TaskForm taskForm;
	  
	  private static Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
	 
	  public void persistCustomer(DelegateExecution delegateExecution) {
	    // Create new order instance
	    Customer customerEntity = new Customer();
	 
	    // Get all process variables
	    Map<String, Object> variables = delegateExecution.getVariables();
	 
	    // Set order attributes
	    customerEntity.setName((String) variables.get("name"));
	    customerEntity.setAddress((String) variables.get("address"));
	    customerEntity.setEMail((String) variables.get("eMail"));
	 
	    /*
	      Persist order instance and flush. After the flush the
	      id of the order instance is set.
	    */
	    entityManager.persist(customerEntity);
	    entityManager.flush();
	 
	    // Remove no longer needed process variables
	    // delegateExecution.removeVariables(variables.keySet());
	 
	    // Add newly created customer id as process variable
	    delegateExecution.setVariable("customerId", customerEntity.getId());
	  }

	  public Customer getCustomer(Long customerId) {
		  // Load entity from database
		  return entityManager.find(Customer.class, customerId);
	  }
}