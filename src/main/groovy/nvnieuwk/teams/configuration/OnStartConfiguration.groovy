package nvnieuwk.teams.configuration

class OnStartConfiguration {
    Boolean enabled
    String message

    OnStartConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        message = configMap.message ?: ''
    }
}