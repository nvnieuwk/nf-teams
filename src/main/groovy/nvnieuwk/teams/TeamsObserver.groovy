/*
 * Copyright 2025, Nicolas Vannieuwkerke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nvnieuwk.teams

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import nextflow.Session
import nextflow.trace.TraceObserver
import nextflow.processor.TaskHandler
import nextflow.trace.TraceRecord
import nvnieuwk.teams.configuration.TeamsConfiguration

/**
 * Implements an observer that allows implementing custom
 * logic on nextflow execution events.
 */
@Slf4j
@CompileStatic
class TeamsObserver implements TraceObserver {

    private TeamsConfiguration config
    private TeamsHookEngine hookEngine
    private Session startSession

    @Override
    void onFlowCreate(Session session) {
        this.config = new TeamsConfiguration(session.config.navigate("teams") as Map<String, Object>)
        this.hookEngine = new TeamsHookEngine(config)
        this.startSession = session
        log.debug("TeamsObserver created with webhook URL: ${config.webHook.url}")
    }

    @Override
    void onFlowBegin() {
        if (config.onStart.enabled) {
            log.info("Sending Teams notification on workflow start")
            hookEngine.sendStartMessage(startSession, config.onStart.template)
        }
    }

    @Override
    void onFlowComplete() {
        if (config.onComplete.enabled) {
            log.info("Sending Teams notification on workflow completion")
            hookEngine.sendCompleteMessage(startSession, config.onComplete.template)
        }
    }

    @Override
    void onFlowError(TaskHandler handler, TraceRecord trace) {
        if (config.onError.enabled) {
            log.info("Sending Teams notification on workflow error")
            hookEngine.sendErrorMessage(startSession, config.onError.template, handler, trace)
        }
    }
}
