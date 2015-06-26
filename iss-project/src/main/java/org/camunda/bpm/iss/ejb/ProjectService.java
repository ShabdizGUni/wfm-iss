package org.camunda.bpm.iss.ejb;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.camunda.bpm.engine.cdi.jsf.TaskForm;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.iss.entity.Customer;
import org.camunda.bpm.iss.entity.Project;

@Named
@Stateless
public class ProjectService {
	 @PersistenceContext
	  private EntityManager entityManager;
	  
	  // Inject task form available through the camunda cdi artifact
	  @Inject
	  private TaskForm taskForm;	  
	  	  
	  private static Logger LOGGER = Logger.getLogger(ProjectService.class.getName());
	 
	  public void persistProject(DelegateExecution delegateExecution) {
	   
		LOGGER.log(Level.INFO, "Create new project instance");  
		  // Create new customer instance
		Project projectEntity = new Project();
	 
	    LOGGER.log(Level.INFO, "Get all process variables");
	    // Get all process variables
	    Map<String, Object> variables = delegateExecution.getVariables();
	 
	    LOGGER.log(Level.INFO, "Set order attributes");
	    // Set order attributes
	    projectEntity.setTitle((String) variables.get("title"));
	    projectEntity.setEmployee((String) variables.get("employee"));
	    projectEntity.setindividualTime((Integer) variables.get("individualTime"));
	    projectEntity.setStatus((Boolean) variables.get("status"));
	    projectEntity.setDesign((Boolean) variables.get("design"));
	    projectEntity.setcostEstimate((Integer) variables.get("costEstimate"));
	    projectEntity.setProjectStart((Date) variables.get("projectStart"));
	    projectEntity.setProjectEnd((Date) variables.get("projectEnd"));
	    projectEntity.setProjectStatusreport((String) variables.get("projectStatusreport"));
	    
	    try{	    	    	
	    	projectEntity.setCustomer(customerService.getCustomer((Long) variables.get("customerId")));
		    }catch(EJBException e){
		    	Throwable cause = e.getCause();
		    	LOGGER.log(Level.SEVERE, cause.getMessage());
		    }
	    /*
	      Persist customer instance and flush. After the flush the
	      id of the customer instance is set.
	    */
	    LOGGER.log(Level.INFO, " Persist project instance and flush.");
	    
	    entityManager.persist(projectEntity);
	    entityManager.flush();
	 
	    // Remove no longer needed process variables
	    // delegateExecution.removeVariables(variables.keySet());
	 
	    LOGGER.log(Level.INFO, "Add newly created project id as process variable. It is:" + projectEntity.getId());
	    // Add newly created customer id as process variable
	    delegateExecution.setVariable("projectId", projectEntity.getId());
	  }

	  public Project getProject(Long projectId) {
		  // Load entity from database
		  return entityManager.find(Project.class, projectId);
	  }
	  
}
