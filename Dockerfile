FROM jenkins/jenkins:lts
JENKINS_OPTS="--prefix=/jenkins -Djenkins.install.runSetupWizard=false"
