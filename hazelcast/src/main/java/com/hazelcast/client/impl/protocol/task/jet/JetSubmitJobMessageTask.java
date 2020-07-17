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

package com.hazelcast.client.impl.protocol.task.jet;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.instance.impl.Node;
import com.hazelcast.internal.nio.Connection;
import com.hazelcast.client.impl.protocol.codec.JetSubmitJobCodec;
import com.hazelcast.client.impl.protocol.codec.JetSubmitJobCodec.RequestParameters;
import com.hazelcast.jet.impl.operation.SubmitJobOperation;
import com.hazelcast.spi.impl.operationservice.Operation;

public class JetSubmitJobMessageTask extends AbstractJetMessageTask<RequestParameters, Void> {

    public JetSubmitJobMessageTask(ClientMessage clientMessage, Node node, Connection connection) {
        super(clientMessage, node, connection, JetSubmitJobCodec::decodeRequest,
                o -> JetSubmitJobCodec.encodeResponse());
    }

    @Override
    protected Operation prepareOperation() {
        return new SubmitJobOperation(parameters.jobId, parameters.dag, parameters.jobConfig);
    }

    @Override
    public String getMethodName() {
        return "submitJob";
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{};
    }

}
