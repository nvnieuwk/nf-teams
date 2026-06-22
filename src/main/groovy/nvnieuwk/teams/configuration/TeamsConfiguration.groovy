package nvnieuwk.teams.configuration

import nextflow.config.spec.ConfigOption
import nextflow.config.spec.ConfigScope
import nextflow.config.spec.ScopeName
import nextflow.script.dsl.Description

@ScopeName('teams')
@Description('''
    The 'teams' scope allows you to configure the Microsoft Teams notifications for Nextflow workflows.
''')
class TeamsConfiguration implements ConfigScope {
    @ConfigOption
    @Description('''
        Enable or disable Microsoft Teams notifications.
    ''')
    Boolean enabled

    @Description('''
        Configuration for the Microsoft Teams webhook.
    ''')
    WebHookConfiguration webHook

    @Description('''
        Configuration for notifications when the workflow completes successfully.
    ''')
    OnSuccessConfiguration onSuccess

    @Description('''
        Configuration for notifications when the workflow fails.
    ''')
    OnErrorConfiguration onError

    @Description('''
        Configuration for notifications when the workflow starts.
    ''')
    OnStartConfiguration onStart

    @Description('''
        Configuration for notifications when the workflow completes (regardless of success or failure).
    ''')
    OnCompleteConfiguration onComplete

    TeamsConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        webHook = new WebHookConfiguration(configMap.webHook ?: [:])
        onSuccess = new OnSuccessConfiguration(configMap.onSuccess ?: [:])
        onError = new OnErrorConfiguration(configMap.onError ?: [:])
        onStart = new OnStartConfiguration(configMap.onStart ?: [:])
        onComplete = new OnCompleteConfiguration(configMap.onComplete ?: [:])
    }
}