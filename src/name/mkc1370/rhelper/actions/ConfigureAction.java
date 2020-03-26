package name.mkc1370.rhelper.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import name.mkc1370.rhelper.components.Configurator;
import name.mkc1370.rhelper.exceptions.NotificationException;
import name.mkc1370.rhelper.ui.ConfigurationDialog;

public class ConfigureAction extends BaseAction {
	@Override
	public void performAction(AnActionEvent e) {
		Project project = e.getProject();
		if (project == null) {
			throw new NotificationException("No project found", "Are you in any project?");
		}

		Configurator configurator = project.getComponent(Configurator.class);
		Configurator.State configuration = configurator.getState();

		ConfigurationDialog x = new ConfigurationDialog(project, configuration);
		x.show();
		if (x.isOK()) {
			configurator.loadState(x.getConfiguration());
		}
	}
}
