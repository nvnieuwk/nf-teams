package nvnieuwk.teams.configuration

class OnErrorConfiguration {
    Boolean enabled
    String message

    OnErrorConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        message = configMap.message ?: ''
    }
}