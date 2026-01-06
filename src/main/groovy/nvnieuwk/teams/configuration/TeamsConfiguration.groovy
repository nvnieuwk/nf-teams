package nvnieuwk.teams.configuration

class TeamsConfiguration {
    Boolean enabled
    WebHookConfiguration webHook

    TeamsConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        webHook = new WebHookConfiguration(configMap.webHook ?: [:])
    }
}