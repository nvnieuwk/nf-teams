package nvnieuwk.teams

import groovy.util.logging.Slf4j

import nvnieuwk.teams.configuration.TeamsConfiguration

@Slf4j
class TeamsHookEngine {
    String url

    TeamsHookEngine(TeamsConfiguration config) {
        this.url = config.webHook.url
    }

    public void sendMessage(String message) {
        log.info("Sending message to Teams webhook (${url}): ${message}")
        def post = new URL(url).openConnection()
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.getOutputStream().write("{\"text\": \"${message}\"}".getBytes("UTF-8"))
        def postRC = post.getResponseCode()
        if (!postRC.equals(200)) {
            log.warn(post.getErrorStream().getText())
        }
    }
}