/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.client.impl.protocol.codec;

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.protocol.Generated;
import com.hazelcast.client.impl.protocol.codec.builtin.*;
import com.hazelcast.client.impl.protocol.codec.custom.*;
import com.hazelcast.logging.Logger;

import javax.annotation.Nullable;

import static com.hazelcast.client.impl.protocol.ClientMessage.*;
import static com.hazelcast.client.impl.protocol.codec.builtin.FixedSizeTypesCodec.*;

/*
 * This file is auto-generated by the Hazelcast Client Protocol Code Generator.
 * To change this file, edit the templates or the protocol
 * definitions on the https://github.com/hazelcast/hazelcast-client-protocol
 * and regenerate it.
 */

/**
 * TODO DOC
 */
@Generated("3ff09fcc69fa367298d48577da2fa68f")
public final class CacheAddEntryListenerCodec {
    //hex: 0x150100
    public static final int REQUEST_MESSAGE_TYPE = 1376512;
    //hex: 0x150101
    public static final int RESPONSE_MESSAGE_TYPE = 1376513;
    private static final int REQUEST_LOCAL_ONLY_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_LOCAL_ONLY_FIELD_OFFSET + BOOLEAN_SIZE_IN_BYTES;
    private static final int RESPONSE_RESPONSE_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_RESPONSE_FIELD_OFFSET + UUID_SIZE_IN_BYTES;
    private static final int EVENT_CACHE_TYPE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET = EVENT_CACHE_TYPE_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int EVENT_CACHE_INITIAL_FRAME_SIZE = EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    //hex: 0x150102
    private static final int EVENT_CACHE_MESSAGE_TYPE = 1376514;

    private CacheAddEntryListenerCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the cache.
         */
        public java.lang.String name;

        /**
         * if true fires events that originated from this node only, otherwise fires all events
         */
        public boolean localOnly;
    }

    public static ClientMessage encodeRequest(java.lang.String name, boolean localOnly) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(false);
        clientMessage.setAcquiresResource(false);
        clientMessage.setOperationName("Cache.AddEntryListener");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET, localOnly);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        return clientMessage;
    }

    public static CacheAddEntryListenerCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.localOnly = decodeBoolean(initialFrame.content, REQUEST_LOCAL_ONLY_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        return request;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * Registration id for the registered listener.
         */
        public java.util.UUID response;
    }

    public static ClientMessage encodeResponse(java.util.UUID response) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET, response);
        clientMessage.add(initialFrame);

        return clientMessage;
    }

    public static CacheAddEntryListenerCodec.ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        ResponseParameters response = new ResponseParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        response.response = decodeUUID(initialFrame.content, RESPONSE_RESPONSE_FIELD_OFFSET);
        return response;
    }

    public static ClientMessage encodeCacheEvent(int type, java.util.Collection<com.hazelcast.cache.impl.CacheEventData> keys, int completionId) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[EVENT_CACHE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        initialFrame.flags |= ClientMessage.IS_EVENT_FLAG;
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, EVENT_CACHE_MESSAGE_TYPE);
        encodeInt(initialFrame.content, EVENT_CACHE_TYPE_FIELD_OFFSET, type);
        encodeInt(initialFrame.content, EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET, completionId);
        clientMessage.add(initialFrame);

        ListMultiFrameCodec.encode(clientMessage, keys, CacheEventDataCodec::encode);
        return clientMessage;
    }

    public abstract static class AbstractEventHandler {

        public void handle(ClientMessage clientMessage) {
            int messageType = clientMessage.getMessageType();
            ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
            if (messageType == EVENT_CACHE_MESSAGE_TYPE) {
                ClientMessage.Frame initialFrame = iterator.next();
                int type = decodeInt(initialFrame.content, EVENT_CACHE_TYPE_FIELD_OFFSET);
                int completionId = decodeInt(initialFrame.content, EVENT_CACHE_COMPLETION_ID_FIELD_OFFSET);
                java.util.Collection<com.hazelcast.cache.impl.CacheEventData> keys = ListMultiFrameCodec.decode(iterator, CacheEventDataCodec::decode);
                handleCacheEvent(type, keys, completionId);
                return;
            }
            Logger.getLogger(super.getClass()).finest("Unknown message type received on event handler :" + messageType);
        }
        public abstract void handleCacheEvent(int type, java.util.Collection<com.hazelcast.cache.impl.CacheEventData> keys, int completionId);
    }
}
