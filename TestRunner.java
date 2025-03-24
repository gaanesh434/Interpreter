import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.engine.discovery.DiscoverySelectors;

public class TestRunner {
  public static void main(String[] args) {
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
        .selectors(DiscoverySelectors.selectClass(lexersTest.class))
        .build();

    Launcher launcher = LauncherFactory.create();
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    launcher.registerTestExecutionListeners(listener);
    launcher.execute(request);

    TestExecutionSummary summary = listener.getSummary();
    System.out.println("Tests run: " + summary.getTestsFoundCount());
    System.out.println("Tests succeeded: " + summary.getTestsSucceededCount());
    System.out.println("Tests failed: " + summary.getTestsFailedCount());

    if (summary.getTestsFailedCount() > 0) {
      summary.getFailures().forEach(failure -> {
        System.out.println("Test failed: " + failure.getTestIdentifier().getDisplayName());
        System.out.println("Reason: " + failure.getException().getMessage());
      });
      System.exit(1);
    }
  }
}