/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti5.standalone.escapeclause;

import org.activiti.engine.repository.DeploymentProperties;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.TimerJobQuery;

public class JobQueryEscapeClauseTest extends AbstractEscapeClauseTestCase {

  private String deploymentId;
  private String deploymentTwoId;
  private String deploymentThreeId;

  protected void setUp() throws Exception {
    super.setUp();
    
    deploymentId = repositoryService.createDeployment()
        .addClasspathResource("org/activiti5/engine/test/api/mgmt/timerOnTask.bpmn20.xml")
        .tenantId("tenant%")
        .deploymentProperty(DeploymentProperties.DEPLOY_AS_ACTIVITI5_PROCESS_DEFINITION, Boolean.TRUE)
        .deploy()
        .getId();

    deploymentTwoId = repositoryService.createDeployment()
            .addClasspathResource("org/activiti5/engine/test/api/mgmt/timerOnTask.bpmn20.xml")
            .tenantId("tenant_")
            .deploy()
            .getId();

    deploymentThreeId = repositoryService.createDeployment()
        .addClasspathResource("org/activiti5/engine/test/api/mgmt/timerOnTask.bpmn20.xml")
        .tenantId("test")
        .deploymentProperty(DeploymentProperties.DEPLOY_AS_ACTIVITI5_PROCESS_DEFINITION, Boolean.TRUE)
        .deploy()
        .getId();

    runtimeService.startProcessInstanceByKeyAndTenantId("timerOnTask", "tenant%").getId();
    
    runtimeService.startProcessInstanceByKeyAndTenantId("timerOnTask", "tenant_").getId();
    
    runtimeService.startProcessInstanceByKeyAndTenantId("timerOnTask", "test").getId();
  }

  @Override
  protected void tearDown() throws Exception {
    repositoryService.deleteDeployment(deploymentId, true);
    repositoryService.deleteDeployment(deploymentTwoId, true);
    repositoryService.deleteDeployment(deploymentThreeId, true);
    super.tearDown();
  }

  public void testQueryByTenantIdLike() {
    TimerJobQuery query = managementService.createTimerJobQuery().jobTenantIdLike("%\\%%");
    assertEquals("tenant%", query.singleResult().getTenantId());
    assertEquals(1, query.list().size());
    assertEquals(1, query.count());

    query = managementService.createTimerJobQuery().jobTenantIdLike("%\\_%");
    assertEquals("tenant_", query.singleResult().getTenantId());
    assertEquals(1, query.list().size());
    assertEquals(1, query.count());

    query = managementService.createTimerJobQuery().jobTenantIdLike("%test%");
    assertEquals("test", query.singleResult().getTenantId());
    assertEquals(1, query.list().size());
    assertEquals(1, query.count());
  }
}
