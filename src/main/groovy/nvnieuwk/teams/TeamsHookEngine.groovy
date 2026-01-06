package nvnieuwk.teams

import groovy.util.logging.Slf4j

import nextflow.Session

import nvnieuwk.teams.configuration.TeamsConfiguration


import groovy.json.JsonOutput.*

@Slf4j
class TeamsHookEngine {
    String url

    TeamsHookEngine(TeamsConfiguration config) {
        this.url = config.webHook.url
    }

    public void sendMessage(Session session) {
        log.info("Sending message to Teams webhook (${url})")
        def workflow = session.workflowMetadata
        def summary = [:]
        // def summary_params = session.params
        // summary_params
        //     .keySet()
        //     .sort()
        //     .each { group ->
        //         summary << summary_params[group]
        //     }

        def misc_fields = [:]
        misc_fields['start']          = workflow.start
        misc_fields['complete']       = workflow.complete
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

        def msg_fields = [:]
        msg_fields['version']      = session.manifest.version
        msg_fields['pipeline_name'] = workflow.manifest.name
        msg_fields['runName']      = workflow.runName
        msg_fields['success']      = workflow.success
        msg_fields['dateComplete'] = workflow.complete
        msg_fields['duration']     = workflow.duration
        msg_fields['exitStatus']   = workflow.exitStatus
        msg_fields['errorMessage'] = (workflow.errorMessage ?: 'None')
        msg_fields['errorReport']  = (workflow.errorReport ?: 'None')
        msg_fields['commandLine']  = workflow.commandLine
        msg_fields['projectDir']   = workflow.projectDir
        msg_fields['summary']      = summary << misc_fields

        // Render the JSON template
        def engine       = new groovy.text.GStringTemplateEngine()
        // Defaults to "Adaptive Cards" (https://adaptivecards.io), except Slack which has its own format
        def hf            = new File(getClass().getResource("/template.json").toURI())
        def json_template = engine.createTemplate(hf).make(msg_fields)
        def json_message  = json_template.toString()


        def post = new URL(url).openConnection()
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.getOutputStream().write(json_message.getBytes("UTF-8"))
        def postRC = post.getResponseCode()
        if (!postRC.equals(200)) {
            log.warn(post.getErrorStream().getText())
        }
    }
}