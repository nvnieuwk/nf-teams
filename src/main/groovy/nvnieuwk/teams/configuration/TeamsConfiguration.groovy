package nvnieuwk.teams.configuration

class TeamsConfiguration {
    Boolean enabled
    WebHookConfiguration webHook
    OnCompleteConfiguration onComplete
    OnErrorConfiguration onError
    OnStartConfiguration onStart

    TeamsConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        webHook = new WebHookConfiguration(configMap.webHook ?: [:])
        onComplete = new OnCompleteConfiguration(configMap.onComplete ?: [:])
        onError = new OnErrorConfiguration(configMap.onError ?: [:])
        onStart = new OnStartConfiguration(configMap.onStart ?: [:])
    }
}