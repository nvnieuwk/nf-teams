package nvnieuwk.teams.configuration

class OnCompleteConfiguration {
    Boolean enabled
    String message

    OnCompleteConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        message = configMap.message ?: ''
    }
}