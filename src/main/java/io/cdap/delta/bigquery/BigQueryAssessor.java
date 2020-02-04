/*
 * Copyright © 2020 Cask Data, Inc.
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

import com.google.cloud.bigquery.StandardSQLTypeName;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.delta.api.assessment.ColumnAssessment;
import io.cdap.delta.api.assessment.StandardizedTableDetail;
import io.cdap.delta.api.assessment.TableAssessment;
import io.cdap.delta.api.assessment.TableAssessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Assesses table information.
 */
public class BigQueryAssessor implements TableAssessor<StandardizedTableDetail> {

  @Override
  public TableAssessment assess(StandardizedTableDetail tableDetail) {
    List<ColumnAssessment> columnAssessments = new ArrayList<>();
    for (Schema.Field field : tableDetail.getSchema().getFields()) {
      columnAssessments.add(new ColumnAssessment(field.getName(), toBigQueryType(field).toLowerCase()));
    }
    return new TableAssessment(columnAssessments);
  }

  private String toBigQueryType(Schema.Field field) {
    Schema schema = field.getSchema();
    schema = schema.isNullable() ? schema.getNonNullable() : schema;
    Schema.LogicalType logicalType = schema.getLogicalType();
    if (logicalType != null) {
      switch (logicalType) {
        case DECIMAL:
          return StandardSQLTypeName.NUMERIC.name();
        case DATE:
          return StandardSQLTypeName.DATE.name();
        case TIME_MICROS:
        case TIME_MILLIS:
          return StandardSQLTypeName.TIME.name();
        case TIMESTAMP_MICROS:
        case TIMESTAMP_MILLIS:
          return StandardSQLTypeName.TIMESTAMP.name();
      }
      throw new IllegalArgumentException(String.format("Column '%s' is of unsupported type '%s'",
                                                       field.getName(), schema.getLogicalType().getToken()));
    }

    switch (schema.getType()) {
      case BOOLEAN:
        return StandardSQLTypeName.BOOL.name();
      case FLOAT:
      case DOUBLE:
        return StandardSQLTypeName.FLOAT64.name();
      case STRING:
      case ENUM:
        return StandardSQLTypeName.STRING.name();
      case INT:
      case LONG:
        return StandardSQLTypeName.INT64.name();
      case ARRAY:
        return StandardSQLTypeName.ARRAY.name();
      case BYTES:
        return StandardSQLTypeName.BYTES.name();
      case RECORD:
        return StandardSQLTypeName.STRUCT.name();
    }

    throw new IllegalArgumentException(String.format("Column '%s' is of unsupported type '%s'",
                                                     field.getName(), schema.getType().name().toLowerCase()));
  }
}
