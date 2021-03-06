package name.mkc1370.rhelper.task;

import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.lang.Language;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;

import com.jetbrains.rider.ideaInterop.find.scopes.RiderProjectScope;
import com.jetbrains.rider.model.ProjectId;
import com.jetbrains.rider.model.Solution;
import com.jetbrains.rider.model.SolutionModel;
import name.mkc1370.rhelper.configuration.TaskConfiguration;
import name.mkc1370.rhelper.configuration.TaskConfigurationType;
import name.mkc1370.rhelper.exceptions.NotificationException;
import name.mkc1370.rhelper.generation.FileUtils;
import name.mkc1370.rhelper.generation.TemplatesUtils;

public class TaskUtils {

	private TaskUtils() {
	}

	/**
	 * Generates task file content depending on custom user template
	 */
	private static String getTaskContent(Project project, String className) {
		String template = TemplatesUtils.getTemplate(project, "Task");
		template = TemplatesUtils.replaceAll(template, TemplatesUtils.CLASS_NAME, className);
		return template;
	}

	public static PsiElement saveNewTask(TaskData taskData, Project project) {
		createConfigurationForTask(project, taskData);
		return generateSource(project, taskData);
	}

	private static PsiElement generateSource(Project project, TaskData taskData) {
		VirtualFile parent = FileUtils.findOrCreateByRelativePath(project.getBaseDir(), FileUtils.getDirectory(taskData.getSourcePath()));
		PsiDirectory psiParent = PsiManager.getInstance(project).findDirectory(parent);
		if (psiParent == null) {
			throw new NotificationException("Couldn't open parent directory as PSI");
		}

		Language cSharp = Language.findLanguageByID("C#");
		if (cSharp == null) {
			throw new NotificationException("Language not found");
		}

		PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(
				FileUtils.getFilename(taskData.getSourcePath()),
				cSharp,
				getTaskContent(project, taskData.getClassName())
		);
		if (file == null) {
			throw new NotificationException("Couldn't generate file");
		}
		return ApplicationManager.getApplication().runWriteAction(
				(Computable<PsiElement>) () -> psiParent.add(file)
		);

	}

	private static void createConfigurationForTask(Project project, TaskData taskData) {
		TaskConfigurationType configurationType = new TaskConfigurationType();
		ConfigurationFactory factory = configurationType.getConfigurationFactories()[0];

		RunManager manager = RunManager.getInstance(project);
		TaskConfiguration taskConfiguration = new TaskConfiguration(
				project,
				factory
		);
		taskConfiguration.setFromTaskData(taskData);
		RunnerAndConfigurationSettings configuration = manager.createConfiguration(
				taskConfiguration,
				factory
		);
		manager.addConfiguration(configuration, true);

		manager.setSelectedConfiguration(configuration);
	}
}
