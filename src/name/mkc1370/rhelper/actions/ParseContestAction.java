package name.mkc1370.rhelper.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.rider.ideaInterop.fileTypes.csharp.psi.CSharpFile;
import name.mkc1370.rhelper.IDEUtils;
import name.mkc1370.rhelper.task.TaskData;
import name.mkc1370.rhelper.task.TaskUtils;
import name.mkc1370.rhelper.ui.ParseDialog;
import name.mkc1370.rhelper.ui.UIUtils;

public class ParseContestAction extends BaseAction {
	@Override
	protected void performAction(AnActionEvent e) {
		Project project = e.getProject();
		ParseDialog dialog = new ParseDialog(project);
		dialog.show();
		if (!dialog.isOK()) {
			return;
		}
		for (TaskData taskData : dialog.getResult()) {
			PsiElement generatedFile = TaskUtils.saveNewTask(taskData, project);
			UIUtils.openMethodInEditor(project, (CSharpFile) generatedFile, "Solve");
		}

		IDEUtils.reloadProject(project);
	}
}
