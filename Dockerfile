FROM jenkins/jenkins:lts

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

USER root
RUN usermod -aG docker jenkins
USER jenkins
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
COPY config/plugins.txt /usr/share/jenkins/ref/
RUN xargs /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

