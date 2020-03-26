package name.mkc1370.rhelper.task;

import com.intellij.openapi.project.Project;
import name.mkc1370.rhelper.components.Configurator;
import net.egork.chelper.task.StreamConfiguration;
import net.egork.chelper.task.Test;
import net.egork.chelper.task.TestType;

import java.util.Arrays;

/**
 * Represent configuration of a task
 */
public class TaskData {
	private final String name;
	private final String className;
	private final String sourcePath;
	private final StreamConfiguration input;
	private final StreamConfiguration output;
	private final TestType testType;
	private final Test[] tests;

	public TaskData(
			String name,
			String className,
			String sourcePath,
			StreamConfiguration input,
			StreamConfiguration output,
			TestType testType,
			Test[] tests
	) {
		this.input = input;
		this.output = output;
		this.name = name;
		this.className = className;
		this.sourcePath = sourcePath;
		this.testType = testType;
		this.tests = Arrays.copyOf(tests, tests.length);
	}

	public TaskData(TaskData task) {
		this(task.name, task.className, task.sourcePath, task.input, task.output, task.testType, task.tests);
	}

	public String getName() {
		return name;
	}

	public String getClassName() {
		return className;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public StreamConfiguration getInput() {
		return input;
	}

	public StreamConfiguration getOutput() {
		return output;
	}

	public Test[] getTests() {
		return Arrays.copyOf(tests, tests.length);
	}

	public static TaskData emptyTaskData(Project project) {
		return new TaskData(
				"",
				"",
				String.format(defaultSourcePathFormat(project), ""),
				StreamConfiguration.STANDARD,
				StreamConfiguration.STANDARD,
				TestType.SINGLE,
				new Test[0]
		);
	}

	public static String defaultSourcePathFormat(Project project) {
		Configurator configurator = project.getComponent(Configurator.class);
		Configurator.State configuration = configurator.getState();
		String path = configuration.getTasksDirectory();
		return path + "/%s.cs";
	}

	public TestType getTestType() {
		return testType;
	}

}
