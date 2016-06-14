/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.test.util;

import java.util.Properties;

import bitronix.tm.resource.jdbc.PoolingDataSource;
import org.jbpm.persistence.util.PersistenceUtil;
import org.junit.Assert;

public class ExecutorTestUtil {
    
    protected static final String MAX_POOL_SIZE = "maxPoolSize";
    protected static final String ALLOW_LOCAL_TXS = "allowLocalTransactions";
    
    protected static final String DATASOURCE_CLASS_NAME = "className";
    protected static final String DRIVER_CLASS_NAME = "driverClassName";
    protected static final String USER = "user";
    protected static final String PASSWORD = "password";
    protected static final String JDBC_URL = "url";
    
    public static PoolingDataSource setupPoolingDataSource() {
        Properties dsProps = new Properties();
        setDefaultProperties(dsProps);
        PoolingDataSource pds = PersistenceUtil.setupPoolingDataSource(dsProps, "jdbc/jbpm-ds", false);
        pds.init();
        
        return pds;
    }

    /**
     * Return the default database/datasource properties - These properties use
     * an in-memory H2 database
     * 
     * This is used when the developer is somehow running the tests but
     * bypassing the maven filtering that's been turned on in the pom.
     * 
     * @return Properties containing the default properties
     */
    private static void setDefaultProperties(Properties props) {
        String[] keyArr = { 
                "serverName", "portNumber", "databaseName", JDBC_URL,
                USER, PASSWORD,
                DRIVER_CLASS_NAME, DATASOURCE_CLASS_NAME,
                MAX_POOL_SIZE, ALLOW_LOCAL_TXS };
        String[] defaultPropArr = { 
                "", "", "", "jdbc:h2:mem:jbpm-db;MVCC=true",
                "sa", "", 
                "org.h2.Driver", "bitronix.tm.resource.jdbc.lrc.LrcXADataSource", 
                "5", "true" };
        Assert.assertTrue("Unequal number of keys for default properties", keyArr.length == defaultPropArr.length);
        for (int i = 0; i < keyArr.length; ++i) {
            if( ! props.containsKey(keyArr[i]) ) {
                props.put(keyArr[i], defaultPropArr[i]);
            }
        }
    }
}
