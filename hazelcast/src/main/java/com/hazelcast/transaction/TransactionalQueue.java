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

package com.hazelcast.transaction;

import com.hazelcast.collection.BaseQueue;
import com.hazelcast.collection.IQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * Transactional implementation of {@link BaseQueue}.
 *
 * @param <E> the type of elements held in this collection
 * @see BaseQueue
 * @see IQueue
 */
public interface TransactionalQueue<E> extends TransactionalObject, BaseQueue<E> {

    @Nullable
    @Override
    E poll(long timeout, @Nonnull TimeUnit unit) throws InterruptedException;

    /**
     * Retrieves the head of this queue without removing it,
     * or returns <code>null</code> if this queue is empty.
     *
     * @return the head of this queue, or <code>null</code> if this queue is empty
     */
    @Nullable
    E peek();

    /**
     * Retrieves and the head of this queue without removing it, waiting up to the
     * specified wait time if necessary for an element to become available.
     *
     * @param timeout how long to wait before giving up, in units of
     *        <code>unit</code>
     * @param unit a <code>TimeUnit</code> determining how to interpret the
     *        <code>timeout</code> parameter
     * @return the head of this queue, or <code>null</code> if the
     *         specified waiting time elapses before an element is available
     * @throws InterruptedException if interrupted while waiting
     */
    @Nullable
    E peek(long timeout, TimeUnit unit) throws InterruptedException;
}
