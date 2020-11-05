/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.sql.impl;

import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.core.DAG;
import com.hazelcast.jet.sql.impl.parse.SqlAlterJob.AlterJobOperation;
import com.hazelcast.jet.sql.impl.schema.Mapping;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRowMetadata;
import com.hazelcast.sql.impl.QueryId;
import com.hazelcast.sql.impl.optimizer.SqlPlan;
import com.hazelcast.sql.impl.security.SqlSecurityContext;

interface JetPlan extends SqlPlan {

    SqlResult execute();

    default void checkPermissions(SqlSecurityContext context) {
        // TODO: implement
    }

    class CreateMappingPlan implements JetPlan {
        private final Mapping mapping;
        private final boolean replace;
        private final boolean ifNotExists;
        private final JetPlanExecutor planExecutor;

        CreateMappingPlan(
                Mapping mapping,
                boolean replace,
                boolean ifNotExists,
                JetPlanExecutor planExecutor
        ) {
            this.mapping = mapping;
            this.replace = replace;
            this.ifNotExists = ifNotExists;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        Mapping mapping() {
            return mapping;
        }

        boolean replace() {
            return replace;
        }

        boolean ifNotExists() {
            return ifNotExists;
        }
    }

    class DropMappingPlan implements JetPlan {
        private final String name;
        private final boolean ifExists;
        private final JetPlanExecutor planExecutor;

        DropMappingPlan(
                String name,
                boolean ifExists,
                JetPlanExecutor planExecutor
        ) {
            this.name = name;
            this.ifExists = ifExists;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        String name() {
            return name;
        }

        boolean ifExists() {
            return ifExists;
        }
    }

    class CreateJobPlan implements JetPlan {
        private final String jobName;
        private final JobConfig jobConfig;
        private final boolean ifNotExists;
        private final ExecutionPlan executionPlan;
        private final JetPlanExecutor planExecutor;

        CreateJobPlan(
                String jobName,
                JobConfig jobConfig,
                boolean ifNotExists,
                ExecutionPlan executionPlan,
                JetPlanExecutor planExecutor
        ) {
            this.jobName = jobName;
            this.jobConfig = jobConfig;
            this.ifNotExists = ifNotExists;
            this.executionPlan = executionPlan;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        public String getJobName() {
            return jobName;
        }

        public JobConfig getJobConfig() {
            return jobConfig;
        }

        public boolean isIfNotExists() {
            return ifNotExists;
        }

        public ExecutionPlan getExecutionPlan() {
            return executionPlan;
        }
    }

    class AlterJobPlan implements JetPlan {
        private final String jobName;
        private final AlterJobOperation operation;
        private final JetPlanExecutor planExecutor;

        AlterJobPlan(String jobName, AlterJobOperation operation, JetPlanExecutor planExecutor) {
            this.jobName = jobName;
            this.operation = operation;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        public String getJobName() {
            return jobName;
        }

        public AlterJobOperation getOperation() {
            return operation;
        }
    }

    class DropJobPlan implements JetPlan {
        private final String jobName;
        private final boolean ifExists;
        private final String withSnapshotName;
        private final JetPlanExecutor planExecutor;

        DropJobPlan(String jobName, boolean ifExists, String withSnapshotName, JetPlanExecutor planExecutor) {
            this.jobName = jobName;
            this.ifExists = ifExists;
            this.withSnapshotName = withSnapshotName;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        public String getJobName() {
            return jobName;
        }

        public boolean isIfExists() {
            return ifExists;
        }

        public String getWithSnapshotName() {
            return withSnapshotName;
        }
    }

    class CreateSnapshotPlan implements JetPlan {
        private final String snapshotName;
        private final String jobName;
        private final JetPlanExecutor planExecutor;

        CreateSnapshotPlan(String snapshotName, String jobName, JetPlanExecutor planExecutor) {
            this.snapshotName = snapshotName;
            this.jobName = jobName;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        public String getSnapshotName() {
            return snapshotName;
        }

        public String getJobName() {
            return jobName;
        }
    }

    class DropSnapshotPlan implements JetPlan {
        private final String snapshotName;
        private final boolean ifExists;
        private final JetPlanExecutor planExecutor;

        DropSnapshotPlan(String snapshotName, boolean ifExists, JetPlanExecutor planExecutor) {
            this.snapshotName = snapshotName;
            this.ifExists = ifExists;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        public String getSnapshotName() {
            return snapshotName;
        }

        public boolean isIfExists() {
            return ifExists;
        }
    }

    class ExecutionPlan implements JetPlan {
        private final DAG dag;
        private final boolean isStreaming;
        private final boolean isInsert;
        private final QueryId queryId;
        private final SqlRowMetadata rowMetadata;
        private final JetPlanExecutor planExecutor;

        ExecutionPlan(
                DAG dag,
                boolean isStreaming,
                boolean isInsert,
                QueryId queryId,
                SqlRowMetadata rowMetadata,
                JetPlanExecutor planExecutor
        ) {
            this.dag = dag;
            this.isStreaming = isStreaming;
            this.isInsert = isInsert;
            this.queryId = queryId;
            this.rowMetadata = rowMetadata;
            this.planExecutor = planExecutor;
        }

        @Override
        public SqlResult execute() {
            return planExecutor.execute(this);
        }

        DAG getDag() {
            return dag;
        }

        boolean isStreaming() {
            return isStreaming;
        }

        boolean isInsert() {
            return isInsert;
        }

        QueryId getQueryId() {
            return queryId;
        }

        SqlRowMetadata getRowMetadata() {
            return rowMetadata;
        }
    }
}
