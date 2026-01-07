package nvnieuwk.teams.configuration

class OnCompleteConfiguration {
    Boolean enabled
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