#FROM jenkins/jenkins:2.89.4
FROM jenkins/jenkins:lts

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
#ENV JENKINS_OPTS="--prefix=/jenkins -Djenkins.install.runSetupWizard=false"

COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
RUN xargs /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

#COPY config/plugins.txt /usr/share/jenkins/ref/

