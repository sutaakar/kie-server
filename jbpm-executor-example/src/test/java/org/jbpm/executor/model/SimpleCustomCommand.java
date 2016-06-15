package org.jbpm.executor.model;

import java.util.HashMap;
import java.util.Map;

import org.kie.api.executor.Command;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;

/**
 * Simple custom command.
 */
public class SimpleCustomCommand implements Command{

    public ExecutionResults execute(CommandContext ctx) {
        String request = (String) ctx.getData("request");

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", "echo " + request);

        ExecutionResults executionResults = new ExecutionResults();
        executionResults.setData(result);
        return executionResults;
    }
}
