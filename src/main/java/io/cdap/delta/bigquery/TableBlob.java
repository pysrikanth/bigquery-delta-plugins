/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.delta.bigquery;

import com.google.cloud.storage.Blob;
import io.cdap.cdap.api.data.schema.Schema;

/**
 * A batch of events for a table, stored as a GCS blob.
 */
public class TableBlob {
  private final String dataset;
  private final String table;
  private final Schema stagingSchema;
  private final Schema targetSchema;
  private final long batchId;
  private final Blob blob;

  public TableBlob(String dataset, String table, Schema targetSchema, Schema stagingSchema, long batchId, Blob blob) {
    this.dataset = dataset;
    this.table = table;
    this.targetSchema = targetSchema;
    this.stagingSchema = stagingSchema;
    this.batchId = batchId;
    this.blob = blob;
  }

  public String getDataset() {
    return dataset;
  }

  public String getTable() {
    return table;
  }

  public Schema getStagingSchema() {
    return stagingSchema;
  }

  public Schema getTargetSchema() {
    return targetSchema;
  }

  public long getBatchId() {
    return batchId;
  }

  public Blob getBlob() {
    return blob;
  }
}
