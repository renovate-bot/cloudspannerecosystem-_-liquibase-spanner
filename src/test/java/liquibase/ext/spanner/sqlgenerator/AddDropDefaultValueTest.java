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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.OutputStreamWriter;
import java.sql.Connection;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.CommandExecutionException;
import liquibase.ext.spanner.AbstractMockServerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.SAME_THREAD)
public class AddDropDefaultValueTest extends AbstractMockServerTest {

  @BeforeEach
  void resetServer() {
    mockSpanner.reset();
    mockAdmin.reset();
  }

  @Test
  void testAddDefaultValueSingersFromYaml() throws Exception {
    for (String file : new String[]{"add-default-value-singers.spanner.yaml"}) {
      try (Connection con = createConnection();
          Liquibase liquibase = getLiquibase(con, file)) {
        CommandExecutionException exception = assertThrows(CommandExecutionException.class,
            () -> liquibase.update(new Contexts("test"), new OutputStreamWriter(System.out)));
        assertThat(exception.getMessage())
            .contains(AddDefaultValueGeneratorSpanner.ADD_DEFAULT_VALUE_VALIDATION_ERROR);
      }
    }
    assertThat(mockAdmin.getRequests()).isEmpty();
  }

  @Test
  void testDropDefaultValueSingersFromYaml() throws Exception {
    for (String file : new String[]{"drop-default-value-singers.spanner.yaml"}) {
      try (Connection con = createConnection();
          Liquibase liquibase = getLiquibase(con, file)) {
        CommandExecutionException exception = assertThrows(CommandExecutionException.class,
            () -> liquibase.update(new Contexts("test"), new OutputStreamWriter(System.out)));
        assertThat(exception.getMessage())
            .contains(DropDefaultValueGeneratorSpanner.DROP_DEFAULT_VALUE_VALIDATION_ERROR);
      }
    }
    assertThat(mockAdmin.getRequests()).isEmpty();
  }
}
