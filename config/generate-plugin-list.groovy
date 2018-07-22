/* 
 * This script helps to generate plugins.txt, run it on Manage Jenkins -> Script Console
 * https://stackoverflow.com/questions/9815273/how-to-get-a-list-of-installed-jenkins-plugins-with-name-and-version-pair
 */
List<String> jenkinsPlugins = new ArrayList<String>(Jenkins.instance.pluginManager.plugins);
jenkinsPlugins.sort { it.displayName }
              .each { plugin ->
                   println ("${plugin.shortName}:${plugin.version}")
              }
