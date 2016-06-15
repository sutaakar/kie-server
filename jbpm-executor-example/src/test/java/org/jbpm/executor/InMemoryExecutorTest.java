package org.jbpm.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;
import org.kie.api.executor.ExecutorService;
import org.kie.api.executor.RequestInfo;
import org.kie.api.executor.STATUS;
import org.kie.api.runtime.query.QueryContext;

/**
 * Tests using in-memory storage.
 */
public class InMemoryExecutorTest {

    private ExecutorService executorService;

    @Before
    public void setUp() {
        // Configuration of in-memory executor service.
        executorService = ExecutorServiceFactory.newExecutorService();

        // Set number of threads which will be used by executor - default is 1.
        executorService.setThreadPoolSize(1);

        // Sets interval at which executor threads are running in seconds - default is 3.
        executorService.setInterval(1);

        // Sets time unit of interval - default is SECONDS.
        executorService.setTimeunit(TimeUnit.SECONDS);

        // Number of retries in case of excepting during execution of command - default is 3.
        executorService.setRetries(1);

        executorService.init();
    }

    @After
    public void tearDown() {
        executorService.destroy();
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

    @Test
    public void executeConfiguredTask() throws Exception {
        String businessKey = UUID.randomUUID().toString();

        CommandContext ctxCMD = new CommandContext();
        // Set custom business key for job
        ctxCMD.setData("businessKey", businessKey);
        // Number of retries for this specific command.
        ctxCMD.setData("retries", "3");
        // Custom delay between retries. For more info see https://issues.jboss.org/browse/JBPM-4679.
        ctxCMD.setData("retryDelay", "3s, 6s");

        Long jobId = executorService.scheduleRequest("org.jbpm.executor.commands.PrintOutCommand", ctxCMD);
        assertNotNull(jobId);

        Thread.sleep(4000);

        // Retrieve info about completed jobs.
        List<RequestInfo> completedJobList = executorService.getCompletedRequests(new QueryContext());
        assertEquals(1, completedJobList.size());

        RequestInfo completedJob = completedJobList.get(0);
        assertEquals(businessKey, completedJob.getKey());
        assertEquals(STATUS.DONE, completedJob.getStatus());
        assertEquals("org.jbpm.executor.commands.PrintOutCommand", completedJob.getCommandName());
    }

    @Test
    public void executeCustomTask() throws Exception {
        String businessKey = UUID.randomUUID().toString();

        CommandContext ctxCMD = new CommandContext();
        ctxCMD.setData("businessKey", businessKey);
        ctxCMD.setData("request", "Jack");

        Long jobId = executorService.scheduleRequest("org.jbpm.executor.model.SimpleCustomCommand", ctxCMD);
        assertNotNull(jobId);

        Thread.sleep(4000);

        // Retrieve info about completed jobs.
        List<RequestInfo> completedJobList = executorService.getCompletedRequests(new QueryContext());
        assertEquals(1, completedJobList.size());

        // Retrieve job result data.
        byte[] responseData = completedJobList.get(0).getResponseData();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(responseData));
        ExecutionResults results = (ExecutionResults) in.readObject();
        Map<String, Object> resultData = results.getData();

        String response = (String) resultData.get("result");
        assertEquals("echo Jack", response);
    }
}
