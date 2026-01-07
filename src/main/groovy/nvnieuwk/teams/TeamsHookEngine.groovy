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

    public void sendStartupMessage(Session session, String message) {
        log.info("Sending message to Teams webhook (${url})")
        WorkflowMetadata workflow = session.workflowMetadata

        Map<String,Object> misc_fields = [:]
        misc_fields['start']          = workflow.start
        misc_fields['scriptfile']     = workflow.scriptFile
        misc_fields['scriptid']       = workflow.scriptId
        if (workflow.repository) {
            misc_fields['repository'] = workflow.repository
        }
        if (workflow.commitId) {
            misc_fields['commitid']   = workflow.commitId
        }
        if (workflow.revision) {
            misc_fields['revision']   = workflow.revision
        }
        misc_fields['nxf_version']    = workflow.nextflow.version
        misc_fields['nxf_build']      = workflow.nextflow.build
        misc_fields['nxf_timestamp']  = workflow.nextflow.timestamp

        Map<String,Object> msg_fields = [:]
        msg_fields['version']       = session.manifest.version
        msg_fields['pipeline_name'] = workflow.manifest.name
        msg_fields['runName']       = workflow.runName
        msg_fields['customMessage'] = message
        msg_fields['dateStarted']   = workflow.start
        msg_fields['commandLine']   = workflow.commandLine
        msg_fields['summary']       = misc_fields

        // Render the JSON template
        GStringTemplateEngine engine = new GStringTemplateEngine()
        // Uses to "Adaptive Cards" (https://adaptivecards.io)
        File template = new File(getClass().getResource("/startTemplate.json").toURI())
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