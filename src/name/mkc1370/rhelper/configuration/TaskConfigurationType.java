package name.mkc1370.rhelper.configuration;

import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.SimpleConfigurationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NotNullLazyValue;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TaskConfigurationType extends SimpleConfigurationType {
	public TaskConfigurationType() {
		super(
				"name.mkc1370.rhelper.configuration.TaskConfigurationType",
				"Task",
				"Task for RHelper",
				new NotNullLazyValue<Icon>() {
					@NotNull
					@Override
					protected Icon compute() {
						return IconLoader.getIcon("/name/mkc1370/rhelper/icons/task.png");
					}
				}
		);
	}

	@NotNull
	@Override
	public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
		return new TaskConfiguration(project, this);
	}

}
