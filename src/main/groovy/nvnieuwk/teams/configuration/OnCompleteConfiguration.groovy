package nvnieuwk.teams.configuration

import nextflow.config.spec.ConfigOption
import nextflow.config.spec.ConfigScope
import nextflow.config.spec.ScopeName
import nextflow.script.dsl.Description

@ScopeName('onComplete')
@Description('''
    The 'onComplete' scope allows you to configure what happens when a Nextflow workflow completes successfully.
''')
class OnCompleteConfiguration implements ConfigScope {

    @ConfigOption
    @Description('''
        Enable or disable sending a Teams message when the workflow completes successfully.
    ''')
    Boolean enabled

    @ConfigOption
    @Description('''
        The JSON template file used to format the Teams message upon workflow completion.
    ''')
    File template

    OnCompleteConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        if (configMap.template) {
            template = new File(configMap.template)
        } else {
            template = new File(getClass().getResource("/completeTemplate.json").toURI())
        }
    }
}