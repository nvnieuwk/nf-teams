package nvnieuwk.teams.configuration

import nextflow.config.spec.ConfigOption
import nextflow.config.spec.ConfigScope
import nextflow.config.spec.ScopeName
import nextflow.script.dsl.Description

@ScopeName('webHook')
@Description('''
    The 'webHook' scope allows you to configure the Microsoft Teams webhook for sending notifications.
''')
class WebHookConfiguration implements ConfigScope {

    @ConfigOption
    @Description('''
        The URL of the Microsoft Teams webhook.
    ''')
    String url = ''

    WebHookConfiguration(Map<String,Object> configMap = [:]) {
        url = configMap.url ?: ''
    }
}