package org.jbpm.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.jbpm.test.util.ExecutorTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutorService;
import org.kie.api.executor.RequestInfo;
import org.kie.api.executor.STATUS;
import org.kie.api.runtime.query.QueryContext;

/**
 * Tests using database storage.
 */
public class DatabaseExecutorTest {

    private PoolingDataSource pds;
    private EntityManagerFactory emf;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        pds = ExecutorTestUtil.setupPoolingDataSource();
        emf = Persistence.createEntityManagerFactory("org.jbpm.executor");

        // Configuration of database executor service.
        executorService = ExecutorServiceFactory.newExecutorService(emf);
        // Optional configuration is skipped.
        executorService.init();
    }

    @After
    public void tearDown() {
        executorService.destroy();
        if (emf != null) {
            emf.close();
        }
        pds.close();
    }

    @Test
    public void executeSimpleTask() throws Exception {
        CommandContext ctxCMD = new CommandContext();
        Long jobId = executorService.scheduleRequest("org.jbpm.executor.commands.PrintOutCommand", ctxCMD);
        assertNotNull(jobId);

        Thread.sleep(4000);

        // Retrieve info about completed jobs.
        List<RequestInfo> completedJobList = executorService.getCompletedRequests(new QueryContext());
        assertEquals(1, completedJobList.size());

        RequestInfo completedJob = completedJobList.get(0);
        assertEquals(STATUS.DONE, completedJob.getStatus());
        assertEquals("org.jbpm.executor.commands.PrintOutCommand", completedJob.getCommandName());
    }
}
