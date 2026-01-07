package nvnieuwk.teams.configuration

class OnStartConfiguration {
    Boolean enabled
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
