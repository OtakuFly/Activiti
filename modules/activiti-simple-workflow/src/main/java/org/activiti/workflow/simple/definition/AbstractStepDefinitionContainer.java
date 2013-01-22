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
package org.activiti.workflow.simple.definition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joram Barrez
 */
@SuppressWarnings("unchecked")
public abstract class AbstractStepDefinitionContainer<T> implements StepDefinitionContainer<T> {
  
  protected List<StepDefinition> steps;
  
  public AbstractStepDefinitionContainer() {
    this.steps = new ArrayList<StepDefinition>();
  }
  
  public void addStep(StepDefinition stepDefinition) {
    steps.add(stepDefinition);
  }

  public List<StepDefinition> getSteps() {
    return steps;
  }

  public T addHumanStep(String name, String assignee) {
    return (T) addHumanStep(name, assignee, false);
  }

  public T addHumanStepForWorkflowInitiator(String name) {
    return (T) addHumanStep(name, null, true);
  }

  protected T addHumanStep(String name, String assignee, boolean initiator) {
    HumanStepDefinition humanStepDefinition = new HumanStepDefinition();

    if (name != null) {
      humanStepDefinition.setName(name);
    }

    if (assignee != null) {
      humanStepDefinition.setAssignee(assignee);
    }

    humanStepDefinition.setAssigneeInitiator(initiator);

    addStep(humanStepDefinition);
    return (T) this;
  }

}