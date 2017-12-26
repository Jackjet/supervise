package gov.df.fap.api.workflow.activiti.design;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.activiti.engine.RepositoryService;

public interface IActivitiInit {

  public RepositoryService getRepositoryService();
  
  
}
