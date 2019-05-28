#!groovy

/*
 * Automatically configure the docker cloud in Jenkins.
 * Tested with:
 *   - {name: 'docker-plugin' ver: '1.1.2'}
 *
 * Based on: https://gist.github.com/stuart-warren/e458c8439bcddb975c96b96bec3971b6
*/
import jenkins.model.*;
import hudson.model.*;
import hudson.slaves.*
import com.nirima.jenkins.plugins.docker.DockerCloud
import com.nirima.jenkins.plugins.docker.DockerTemplate
import com.nirima.jenkins.plugins.docker.DockerTemplateBase
import io.jenkins.docker.connector.DockerComputerJNLPConnector
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerEndpoint


def env = System.getenv()

def network = env['DOCKER_SLAVE_NETWORK']
def volumesString = env['DOCKER_SLAVE_VOLUMES']
def environmentsString = env['DOCKER_SLAVE_ENVIRONMENT']
def image = env['DOCKER_SLAVE_IMAGE']
def dockerSlaveLabel = env['DOCKER_SLAVE_LABEL']
def dockerHostUri = env['DOCKER_SLAVE_DOCKER_HOST_URI']
def jenkinsBaseUrl = env['JENKINS_BASE_URL']

// https://github.com/jenkinsci/docker-plugin/blob/docker-plugin-1.1.2/src/main/java/com/nirima/jenkins/plugins/docker/DockerTemplateBase.java#L122
DockerTemplateBase templateBase = new DockerTemplateBase(
                                image, // image
                              null, // pullCredentialsId
                              null, // dnsString
                              network, // network
                              null, // dockerCommand
                              volumesString, // volumesString
                              null, // volumesFromString
                              environmentsString, // environmentsString
                              null, // hostname
                              null, // memoryLimit
                              null, // memorySwap
                              null, // cpuShares
                              null, // bindPorts
                              false, // bindAllPorts
                              false, // privileged
                              false, // tty
                              null, // macAddress
                              "" // extraHostsString
);

//jlc = JenkinsLocationConfiguration.get()

DockerComputerJNLPConnector connector = new DockerComputerJNLPConnector(new JNLPLauncher(null, null)).withUser("jenkins")
                    .withJenkinsUrl(jenkinsBaseUrl);

DockerTemplate dkTemplate = new DockerTemplate(templateBase,connector,dockerSlaveLabel,"","");

ArrayList<DockerTemplate> dkTemplates = new ArrayList<DockerTemplate>();
dkTemplates.add(dkTemplate);

DockerServerEndpoint endpoint = new DockerServerEndpoint(dockerHostUri, "")

ArrayList<DockerCloud> dkCloud = new ArrayList<DockerCloud>();
dkCloud.add(
        // https://github.com/jenkinsci/docker-plugin/blob/docker-plugin-1.1.2/src/main/java/com/nirima/jenkins/plugins/docker/DockerCloud.java#L106
        new DockerCloud(
                "docker",
                dkTemplates,
                endpoint,
                2,
                60,
                60,
                "",
                ""
            )
);

Jenkins.getInstance().clouds.replaceBy(dkCloud)
println "Docker Cloud Config completed"
