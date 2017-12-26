package gov.df.fap.service.workflow.activiti.design.data;

import gov.df.fap.api.workflow.activiti.design.IActivitiInit;
import gov.df.fap.service.util.dao.GeneralDAO;
import gov.df.fap.service.util.sessionmanager.SessionUtil;

import javax.sql.DataSource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IActivitiInitImplBO implements IActivitiInit {

  @Autowired
  @Qualifier("generalDAO")
  private GeneralDAO dao;

  @Override
  public RepositoryService getRepositoryService() {
    ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration
      .createStandaloneProcessEngineConfiguration();

    DataSource dataSource = (DataSource) SessionUtil.getServerBean("multiDataSource");

    processEngineConfiguration.setDataSource(dataSource);

    processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
    processEngineConfiguration.setTransactionsExternallyManaged(true);
    ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

    RepositoryService repositoryService = processEngine.getRepositoryService();
    return repositoryService;
  }
}
