package name.mkc1370.rhelper.components;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.text.StringTokenizer;
import com.jetbrains.rider.ideaInterop.fileTypes.csharp.psi.CSharpFile;
import name.mkc1370.rhelper.IDEUtils;
import name.mkc1370.rhelper.network.SimpleHttpServer;
import name.mkc1370.rhelper.task.TaskData;
import name.mkc1370.rhelper.task.TaskUtils;
import name.mkc1370.rhelper.ui.Notificator;
import name.mkc1370.rhelper.ui.UIUtils;
import net.egork.chelper.parser.*;
import net.egork.chelper.task.Task;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A Component to monitor request from CHelper Chrome Extension and parse them to Tasks
 */
public class ChromeParser implements ProjectComponent {
	private static final int PORT = 4243;
	private static final Map<String, Parser> PARSERS;

	static {
		Map<String, Parser> taskParsers = new HashMap<>();
		taskParsers.put("yandex", new YandexParser());
		taskParsers.put("codeforces", new CodeforcesParser());
		taskParsers.put("hackerrank", new HackerRankParser());
		taskParsers.put("facebook", new FacebookParser());
		taskParsers.put("usaco", new UsacoParser());
		taskParsers.put("gcj", new GCJParser());
		taskParsers.put("bayan", new BayanParser());
		taskParsers.put("kattis", new KattisParser());
		taskParsers.put("codechef", new CodeChefParser());
		taskParsers.put("hackerearth", new HackerEarthParser());
		taskParsers.put("atcoder", new AtCoderParser());
		taskParsers.put("csacademy", new CSAcademyParser());
		taskParsers.put("new-gcj", new NewGCJParser());
		taskParsers.put("json", new JSONParser());
		PARSERS = Collections.unmodifiableMap(taskParsers);
	}

	private SimpleHttpServer server = null;
	private Project project;

	public ChromeParser(Project project) {
		this.project = project;
	}

	@Override
	public void projectOpened() {
		try {
			server = new SimpleHttpServer(
					PORT,
					request -> {
						StringTokenizer st = new StringTokenizer(request);
						String type = st.nextToken();
						Parser parser = PARSERS.get(type);
						if (parser == null) {
							Notificator.showNotification(
									"Unknown parser",
									"Parser " + type + " unknown, request ignored",
									NotificationType.INFORMATION
							);
							return;
						}
						String page = request.substring(st.getCurrentPosition());
						Collection<Task> tasks = parser.parseTaskFromHTML(page);
						if (tasks.isEmpty()) {
							Notificator.showNotification(
									"Couldn't parse any task",
									"Maybe format changed?",
									NotificationType.WARNING
							);
						}

						Configurator configurator = project.getComponent(Configurator.class);
						Configurator.State configuration = configurator.getState();
						String path = configuration.getTasksDirectory();
						for (Task rawTask : tasks) {
							TaskData task = new TaskData(
									rawTask.name,
									rawTask.taskClass,
									String.format("%s/%s.cs", path, rawTask.taskClass),
									rawTask.input,
									rawTask.output,
									rawTask.testType,
									rawTask.tests
							);
							PsiElement generatedFile = TaskUtils.saveNewTask(task, project);
							UIUtils.openMethodInEditor(project, (CSharpFile) generatedFile, "Solve");
						}

						IDEUtils.reloadProject(project);
					}
			);

			new Thread(server, "ChromeParserThread").start();
		}
		catch (IOException ignored) {
			Notificator.showNotification(
					"Could not create serverSocket for Chrome parser",
					"Probably another CHelper or RHelper project is running?",
					NotificationType.ERROR
			);
		}
	}

	@Override
	public void projectClosed() {
		if (server != null) {
			server.stop();
		}
	}
}
