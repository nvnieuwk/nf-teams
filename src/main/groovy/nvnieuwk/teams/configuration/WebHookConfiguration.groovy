package nvnieuwk.teams.configuration

class WebHookConfiguration {
    String url = ''

    WebHookConfiguration(Map<String,Object> configMap = [:]) {
        url = configMap.url ?: ''
    }
}