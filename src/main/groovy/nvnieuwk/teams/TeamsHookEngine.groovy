package nvnieuwk.teams

import groovy.util.logging.Slf4j
import groovy.text.GStringTemplateEngine
import nextflow.Session
import nextflow.script.WorkflowMetadata
import nvnieuwk.teams.configuration.TeamsConfiguration


import groovy.json.JsonOutput.*

@Slf4j
class TeamsHookEngine {
    String url

    TeamsHookEngine(TeamsConfiguration config) {
        this.url = config.webHook.url
    }

    public void sendMessage(Session session, File template) {
        log.info("Sending message to Teams webhook (${url})")

        Map<String,Object> msg_fields = [
            'session': session
        ]

        // Render the JSON template
        GStringTemplateEngine engine = new GStringTemplateEngine()
        String json_message = engine.createTemplate(template).make(msg_fields).toString()
        postToHook(json_message)
    }

    private void postToHook(String message) {
        def post = new URL(url).openConnection()
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.getOutputStream().write(message.getBytes("UTF-8"))
        def postRC = post.getResponseCode()
        if (!postRC.equals(200)) {
            log.warn(post.getErrorStream().getText())
        }
    }
}