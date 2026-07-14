package nvnieuwk.plugin

import nextflow.Session
import spock.lang.Specification
import test.MockScriptRunner

/**
 * Implements a basic factory test
 *
 */
class TeamsObserverTest extends Specification {

    def 'should create the observer instance' () {
        when:
        def SCRIPT = """
        workflow {

        }
        """
        def config = ["teams": [
            "enabled": true,
            "webHook": [
                "url": System.getenv('TEST_TEAMS_HOOK_URL')
            ]
        ]]
        def result = new MockScriptRunner(config).setScript(SCRIPT).execute()

        then:
        result == []
    }

}
