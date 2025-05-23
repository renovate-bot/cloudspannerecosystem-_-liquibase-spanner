/**
 * Copyright 2020 Google LLC
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>https://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package liquibase.ext.spanner.sqlgenerator;

import liquibase.database.Database;
import liquibase.datatype.DataTypeFactory;
import liquibase.exception.ValidationErrors;
import liquibase.ext.spanner.ICloudSpanner;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AddDefaultValueGenerator;
import liquibase.statement.core.AddDefaultValueStatement;

public class AddDefaultValueGeneratorSpanner extends AddDefaultValueGenerator {

  @Override
  public ValidationErrors validate(
      AddDefaultValueStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
    return super.validate(statement, database, sqlGeneratorChain);
  }

  @Override
  public Sql[] generateSql(
      final AddDefaultValueStatement statement,
      final Database database,
      SqlGeneratorChain sqlGeneratorChain) {
    Object defaultValue = statement.getDefaultValue();
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("ALTER TABLE ");
    queryStringBuilder.append(
        database.escapeTableName(
            statement.getCatalogName(), statement.getSchemaName(), statement.getTableName()));
    queryStringBuilder.append(" ALTER COLUMN ");
    queryStringBuilder.append(
        database.escapeColumnName(
            statement.getCatalogName(),
            statement.getSchemaName(),
            statement.getTableName(),
            statement.getColumnName()));
    queryStringBuilder.append(" SET DEFAULT (");
    queryStringBuilder.append(
        DataTypeFactory.getInstance()
            .fromObject(defaultValue, database)
            .objectToSql(defaultValue, database));
    queryStringBuilder.append(")");

    return new Sql[] {new UnparsedSql(queryStringBuilder.toString(), getAffectedColumn(statement))};
  }

  @Override
  public int getPriority() {
    return SqlGenerator.PRIORITY_DATABASE;
  }

  @Override
  public boolean supports(AddDefaultValueStatement statement, Database database) {
    return (database instanceof ICloudSpanner);
  }
}
