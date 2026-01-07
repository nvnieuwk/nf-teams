package nvnieuwk.teams

import java.time.OffsetDateTime
import groovy.util.logging.Slf4j
import groovy.text.GStringTemplateEngine
import nextflow.Session
import nextflow.script.WorkflowMetadata
import nextflow.processor.TaskHandler
import nextflow.trace.TraceRecord
import nvnieuwk.teams.configuration.TeamsConfiguration


import groovy.json.JsonOutput.*

@Slf4j
class TeamsHookEngine {
    String url

    TeamsHookEngine(TeamsConfiguration config) {
        this.url = config.webHook.url
    }

    public void sendStartMessage(Session session, File template) {
        log.info("Sending message to Teams webhook (${url})")

        Map<String,Object> msg_fields = [
            'session': session
        ]

        postToHook(renderTemplate(template, msg_fields))
    }

    public void sendSuccessMessage(Session session, File template) {
        log.info("Sending message to Teams webhook (${url})")

        Map<String,Object> msg_fields = [
            'session': session
        ]

        postToHook(renderTemplate(template, msg_fields))
    }

    public void sendErrorMessage(Session session, File template, TaskHandler handler, TraceRecord trace) {
        log.info("Sending message to Teams webhook (${url})")

        Map<String,Object> msg_fields = [
            'session': session
        ]

        postToHook(renderTemplate(template, msg_fields))
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

    private String renderTemplate(File template, Map<String,Object> fields) {
        GStringTemplateEngine engine = new GStringTemplateEngine()
        return engine.createTemplate(template).make(fields).toString()
    }
}