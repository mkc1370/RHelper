package name.mkc1370.rhelper.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.rider.ideaInterop.fileTypes.csharp.psi.CSharpFile;
import name.mkc1370.rhelper.IDEUtils;
import name.mkc1370.rhelper.exceptions.NotificationException;
import name.mkc1370.rhelper.task.TaskData;
import name.mkc1370.rhelper.task.TaskUtils;
import name.mkc1370.rhelper.ui.AddTaskDialog;
import name.mkc1370.rhelper.ui.UIUtils;

public class AddTaskAction extends BaseAction {

	@Override
	public void performAction(AnActionEvent e) {
		Project project = e.getProject();
		if (project == null) {
			throw new NotificationException("No project found", "Are you in any project?");
		}

		AddTaskDialog dialog = new AddTaskDialog(project);
		dialog.show();
		if (!dialog.isOK()) {
			return;
		}
		TaskData task = dialog.getTask();

		PsiElement generatedFile = TaskUtils.saveNewTask(task, project);

		UIUtils.openMethodInEditor(project, (CSharpFile) generatedFile, "Solve");

		IDEUtils.reloadProject(project);
	}

}
