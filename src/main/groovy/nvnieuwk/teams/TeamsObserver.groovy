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
import nextflow.trace.TraceObserverV2
import nextflow.trace.event.TaskEvent
import nvnieuwk.teams.configuration.TeamsConfiguration

/**
 * Implements an observer that allows implementing custom
 * logic on nextflow execution events.
 */
@Slf4j
@CompileStatic
class TeamsObserver implements TraceObserverV2 {

    private TeamsConfiguration config
    private TeamsHookEngine hookEngine
    private Session session

    @Override
    void onFlowCreate(Session session) {
        this.config = new TeamsConfiguration(session.config.navigate("teams") as Map<String, Object>)
        this.hookEngine = new TeamsHookEngine(config)
        this.session = session
        log.debug("TeamsObserver created")
        if (config.onStart.enabled) {
            log.info("Sending Teams notification on workflow start")
            hookEngine.sendStandardMessage(session, config.onStart.template)
        }
    }

    @Override
    void onFlowComplete() {
        if (config.onComplete.enabled) {
            log.info("Sending Teams notification on workflow completion")
            hookEngine.sendStandardMessage(session, config.onComplete.template)
        }
        if (config.onSuccess.enabled && session?.workflowMetadata.success) {
            log.info("Sending Teams notification on workflow success")
            hookEngine.sendStandardMessage(session, config.onSuccess.template)
        }
    }

    @Override
    void onFlowError(TaskEvent event) {
        if (config.onError.enabled) {
            log.info("Sending Teams notification on workflow error")
            hookEngine.sendErrorMessage(session, config.onError.template, event)
        }
    }
}
