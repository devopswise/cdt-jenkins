FROM jenkins/jenkins:lts
ENV JENKINS_OPTS="--prefix=/jenkins -Djenkins.install.runSetupWizard=false"
