package nvnieuwk.teams.configuration

import nextflow.config.spec.ConfigOption
import nextflow.config.spec.ConfigScope
import nextflow.config.spec.ScopeName
import nextflow.script.dsl.Description

class OnStartConfiguration implements ConfigScope {

    @ConfigOption
    @Description('''
        Enable or disable sending a Teams message when the workflow starts.
    ''')
    Boolean enabled

    @ConfigOption
    @Description('''
        The JSON template file used to format the Teams message upon workflow start.
    ''')
    File template

    OnStartConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        if (configMap.template) {
            template = new File(configMap.template)
        } else {
            template = new File(getClass().getResource("/startTemplate.json").toURI())
        }
    }
}
