package name.mkc1370.rhelper;

import com.intellij.execution.ExecutionTarget;
import com.intellij.execution.ExecutionTargetManager;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.project.Project;

public class IDEUtils {
	private IDEUtils() {
	}

	public static void reloadProject(Project project) {
	}

	public static void chooseConfigurationAndTarget(
			Project project,
			RunnerAndConfigurationSettings runConfiguration,
			ExecutionTarget target
	) {
		RunManager.getInstance(project).setSelectedConfiguration(runConfiguration);
		ExecutionTargetManager.getInstance(project).setActiveTarget(target);
	}
}
