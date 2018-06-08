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
import com.nirima.jenkins.plugins.docker.DockerCloud
import com.nirima.jenkins.plugins.docker.DockerTemplate
import com.nirima.jenkins.plugins.docker.DockerTemplateBase
import io.jenkins.docker.connector.DockerComputerAttachConnector
import org.jenkinsci.plugins.docker.commons.credentials.DockerServerEndpoint



//def pullCredentialsId = env['LDAP_SERVER']
//def dnsString = env['LDAP_ROOTDN']

def network = env['DOCKER_SLAVE_NETWORK']
//def network = 'internal'

//def dockerCommand = env['LDAP_ROOTDN']

//def volumesString = '/var/run/docker.sock:/var/run/docker.sock'
def volumesString = env['DOCKER_SLAVE_VOLUMES']

//def volumesFromString = env['LDAP_ROOTDN']
//def environmentsString = 'JENKINS_SLAVE_SSH_PUBKEY=ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCq2fTf6psRS53paW1hSWGANcmSG9miwci08L9AChjIDZG5OV8RBWjXKimfFzbP18fnmuzJ6cmghvx1e4IkpBdAdgz9qyqPC10J/c3gYtdqdZkTHI712DyME7FoIYqEP3dy7H407mJOnj4nJB3E6KhMsPXHGWu1IYp2fIOqJPY3p/4n3KGqYZnWamGuFoefqE3zd/blB7MMNRrT7vcnrHnCl1XNC6P9T29orTrHJL7Vg1wNklyM24w0vXxVm7whBfiZc/C4rotjTHwbuQeL5jqP4OEp1N8mHbrAcnuEzVRQzK9chvwDR81c4PQkGAWx6jC54XOV6UnQYyrlPnToofKx root@ip-172-31-36-108'
def environmentsString = env['DOCKER_SLAVE_ENVIRONMENT']
//def hostname = env['LDAP_ROOTDN']
//def memoryLimit = env['LDAP_ROOTDN']
//def memorySwap = env['LDAP_ROOTDN']

//def image = 'jenkinsci/ssh-slave'
def image = env['DOCKER_SLAVE_IMAGE']

//def dockerSlaveLabel = 'jenkins-ssh-slave'
def dockerSlaveLabel = env['DOCKER_SLAVE_LABEL']

//def dockerHostUri = 'tcp://172.17.0.1:2375'
def dockerHostUri = env['DOCKER_SLAVE_DOCKER_HOST_URI']


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

DockerComputerAttachConnector connector = new DockerComputerAttachConnector("root")

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
