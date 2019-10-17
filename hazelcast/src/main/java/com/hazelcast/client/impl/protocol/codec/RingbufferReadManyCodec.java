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
 * Reads a batch of items from the Ringbuffer. If the number of available items after the first read item is smaller
 * than the maxCount, these items are returned. So it could be the number of items read is smaller than the maxCount.
 * If there are less items available than minCount, then this call blacks. Reading a batch of items is likely to
 * perform better because less overhead is involved. A filter can be provided to only select items that need to be read.
 * If the filter is null, all items are read. If the filter is not null, only items where the filter function returns
 * true are returned. Using filters is a good way to prevent getting items that are of no value to the receiver.
 * This reduces the amount of IO and the number of operations being executed, and can result in a significant performance improvement.
 */
@Generated("7cad5253bcbb2f1751293e4b5e226b51")
public final class RingbufferReadManyCodec {
    //hex: 0x190A00
    public static final int REQUEST_MESSAGE_TYPE = 1640960;
    //hex: 0x190A01
    public static final int RESPONSE_MESSAGE_TYPE = 1640961;
    private static final int REQUEST_START_SEQUENCE_FIELD_OFFSET = PARTITION_ID_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_MIN_COUNT_FIELD_OFFSET = REQUEST_START_SEQUENCE_FIELD_OFFSET + LONG_SIZE_IN_BYTES;
    private static final int REQUEST_MAX_COUNT_FIELD_OFFSET = REQUEST_MIN_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int REQUEST_INITIAL_FRAME_SIZE = REQUEST_MAX_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_READ_COUNT_FIELD_OFFSET = RESPONSE_BACKUP_ACKS_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_NEXT_SEQ_FIELD_OFFSET = RESPONSE_READ_COUNT_FIELD_OFFSET + INT_SIZE_IN_BYTES;
    private static final int RESPONSE_INITIAL_FRAME_SIZE = RESPONSE_NEXT_SEQ_FIELD_OFFSET + LONG_SIZE_IN_BYTES;

    private RingbufferReadManyCodec() {
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class RequestParameters {

        /**
         * Name of the Ringbuffer
         */
        public java.lang.String name;

        /**
         * the startSequence of the first item to read
         */
        public long startSequence;

        /**
         * the minimum number of items to read.
         */
        public int minCount;

        /**
         * the maximum number of items to read.
         */
        public int maxCount;

        /**
         * Filter is allowed to be null, indicating there is no filter.
         */
        public @Nullable com.hazelcast.nio.serialization.Data filter;
    }

    public static ClientMessage encodeRequest(java.lang.String name, long startSequence, int minCount, int maxCount, @Nullable com.hazelcast.nio.serialization.Data filter) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        clientMessage.setRetryable(true);
        clientMessage.setAcquiresResource(false);
        clientMessage.setOperationName("Ringbuffer.ReadMany");
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[REQUEST_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, REQUEST_MESSAGE_TYPE);
        encodeLong(initialFrame.content, REQUEST_START_SEQUENCE_FIELD_OFFSET, startSequence);
        encodeInt(initialFrame.content, REQUEST_MIN_COUNT_FIELD_OFFSET, minCount);
        encodeInt(initialFrame.content, REQUEST_MAX_COUNT_FIELD_OFFSET, maxCount);
        clientMessage.add(initialFrame);
        StringCodec.encode(clientMessage, name);
        CodecUtil.encodeNullable(clientMessage, filter, DataCodec::encode);
        return clientMessage;
    }

    public static RingbufferReadManyCodec.RequestParameters decodeRequest(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        RequestParameters request = new RequestParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        request.startSequence = decodeLong(initialFrame.content, REQUEST_START_SEQUENCE_FIELD_OFFSET);
        request.minCount = decodeInt(initialFrame.content, REQUEST_MIN_COUNT_FIELD_OFFSET);
        request.maxCount = decodeInt(initialFrame.content, REQUEST_MAX_COUNT_FIELD_OFFSET);
        request.name = StringCodec.decode(iterator);
        request.filter = CodecUtil.decodeNullable(iterator, DataCodec::decode);
        return request;
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings({"URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD"})
    public static class ResponseParameters {

        /**
         * TODO DOC
         */
        public int readCount;

        /**
         * TODO DOC
         */
        public java.util.List<com.hazelcast.nio.serialization.Data> items;

        /**
         * TODO DOC
         */
        public @Nullable long[] itemSeqs;

        /**
         * TODO DOC
         */
        public long nextSeq;
    }

    public static ClientMessage encodeResponse(int readCount, java.util.Collection<com.hazelcast.nio.serialization.Data> items, @Nullable long[] itemSeqs, long nextSeq) {
        ClientMessage clientMessage = ClientMessage.createForEncode();
        ClientMessage.Frame initialFrame = new ClientMessage.Frame(new byte[RESPONSE_INITIAL_FRAME_SIZE], UNFRAGMENTED_MESSAGE);
        encodeInt(initialFrame.content, TYPE_FIELD_OFFSET, RESPONSE_MESSAGE_TYPE);
        encodeInt(initialFrame.content, RESPONSE_READ_COUNT_FIELD_OFFSET, readCount);
        encodeLong(initialFrame.content, RESPONSE_NEXT_SEQ_FIELD_OFFSET, nextSeq);
        clientMessage.add(initialFrame);

        ListMultiFrameCodec.encode(clientMessage, items, DataCodec::encode);
        CodecUtil.encodeNullable(clientMessage, itemSeqs, LongArrayCodec::encode);
        return clientMessage;
    }

    public static RingbufferReadManyCodec.ResponseParameters decodeResponse(ClientMessage clientMessage) {
        ClientMessage.ForwardFrameIterator iterator = clientMessage.frameIterator();
        ResponseParameters response = new ResponseParameters();
        ClientMessage.Frame initialFrame = iterator.next();
        response.readCount = decodeInt(initialFrame.content, RESPONSE_READ_COUNT_FIELD_OFFSET);
        response.nextSeq = decodeLong(initialFrame.content, RESPONSE_NEXT_SEQ_FIELD_OFFSET);
        response.items = ListMultiFrameCodec.decode(iterator, DataCodec::decode);
        response.itemSeqs = CodecUtil.decodeNullable(iterator, LongArrayCodec::decode);
        return response;
    }

}
