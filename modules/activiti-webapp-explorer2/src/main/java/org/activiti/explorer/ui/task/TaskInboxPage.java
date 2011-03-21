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
package org.activiti.explorer.ui.task;

import org.activiti.explorer.Constants;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;


/**
 * @author Joram Barrez
 */
public class TaskInboxPage extends TaskPage {
  
  private static final long serialVersionUID = 652000311912640606L;

  public TaskInboxPage() {
    addTaskList();
  }
  
  protected void addTaskList() {
    this.taskTable = new Table();
    taskTable.addStyleName(Constants.STYLE_TASK_LIST);
    
    // Set non-editable, selectable and full-size
    taskTable.setEditable(false);
    taskTable.setImmediate(true);
    taskTable.setSelectable(true);
    taskTable.setNullSelectionAllowed(false);
    taskTable.setSortDisabled(true);
    taskTable.setSizeFull();
            
    // Listener to change right panel when clicked on a task
    taskTable.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 8811553575319455854L;
      public void valueChange(ValueChangeEvent event) {
        Item item = taskTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
        TaskListEntry taskListEntry = (TaskListEntry) item.getItemProperty("component").getValue();
        mainSplitPanel.setSecondComponent(new TaskDetailPanel(taskListEntry.getTask().getId()));
      }
    });
    
    LazyLoadingQuery lazyLoadingQuery = new TaskQuery(taskService, taskTable);
    LazyLoadingContainer lazyLoadingContainer = new LazyLoadingContainer(lazyLoadingQuery, 10);
    taskTable.setContainerDataSource(lazyLoadingContainer);
    
    // Create column header
    taskTable.addContainerProperty("component", TaskListEntry.class, null);
    taskTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    
    mainSplitPanel.setFirstComponent(taskTable);
    
    // Select first task
    if (taskTable.getContainerDataSource().size() > 0) {
      taskTable.select(0);
    }
  }

}
