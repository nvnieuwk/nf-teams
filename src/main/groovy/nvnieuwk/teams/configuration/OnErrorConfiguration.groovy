package nvnieuwk.teams.configuration

class OnErrorConfiguration {
    Boolean enabled
    File template

    OnErrorConfiguration(Map<String,Object> configMap = [:]) {
        enabled = configMap.enabled ?: false
        if (configMap.template) {
            template = new File(configMap.template)
        } else {
            template = new File(getClass().getResource("/errorTemplate.json").toURI())
        }
    }
}