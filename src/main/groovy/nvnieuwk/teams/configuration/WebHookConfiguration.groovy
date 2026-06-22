package nvnieuwk.teams.configuration

import nextflow.config.spec.ConfigOption
import nextflow.config.spec.ConfigScope
import nextflow.config.spec.ScopeName
import nextflow.script.dsl.Description

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