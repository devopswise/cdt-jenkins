import static jenkins.model.Jenkins.instance as jenkins
import jenkins.install.InstallState

if (!jenkins.installState.isSetupComplete()) {
  InstallState.INITIAL_SETUP_COMPLETED.initializeState()
}
