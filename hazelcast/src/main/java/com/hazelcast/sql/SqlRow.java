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

package com.hazelcast.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * SQL row.
 */
public interface SqlRow {
    /**
     * Gets the value of the column by index.
     * <p>
     * The class of the returned value depends on the SQL type of the column. No implicit conversions are performed on the value.
     *
     * @param columnIndex Column index, 0-based.
     * @return Value of the column.
     *
     * @throws IndexOutOfBoundsException If the column index is out of bounds
     * @throws ClassCastException If the type of the column type isn't assignable to the type {@code T}
     *
     * @see #getMetadata()
     * @see SqlColumnMetadata#getType()
     */
    @Nullable
    <T> T getObject(int columnIndex);

    /**
     * Gets the value of the column by column name.
     * <p>
     * Column name should be one of those defined in {@link SqlRowMetadata}, case-sensitive. You may also use
     * {@link SqlRowMetadata#findColumn(String)} to test for column existence.
     * <p>
     * The class of the returned value depends on the SQL type of the column. No implicit conversions are performed on the value.
     *
     * @param columnName Column name.
     * @return Value of the column
     *
     * @throws NullPointerException If column name is null
     * @throws IllegalArgumentException If a column with the given name is not found
     * @throws ClassCastException If the type of the column type isn't assignable to the type {@code T}
     *
     * @see #getMetadata()
     * @see SqlRowMetadata#findColumn(String)
     * @see SqlColumnMetadata#getName()
     * @see SqlColumnMetadata#getType()
     */
    @Nullable
    <T> T getObject(@Nonnull String columnName);

    /**
     * @see SqlRowMetadata
     * @return Row metadata.
     */
    @Nonnull
    SqlRowMetadata getMetadata();
}
