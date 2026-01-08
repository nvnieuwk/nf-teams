# nf-teams plugin

This plugin can be used to send notifications to Microsoft Teams channels about the status of Nextflow workflows. It supports notifications for workflow start, success, failure, and completion events.

## Get started

Add the following configuration to a Nextflow configuration file:

```groovy
plugins {
    id 'nf-teams@0.1.0'
}

teams {
    enabled = true
    webHook {
        url = '<YOUR_WEBOOK_URL>'
    }
}
```

You can generate a Teams webhook url by following the instructions [here](https://learn.microsoft.com/en-us/microsoftteams/platform/webhooks-and-connectors/how-to/add-incoming-webhook?tabs=newteams%2Cdotnet#create-an-incoming-webhook).

To configure when notifications are sent, you can use the following configuration options:

- `onStart.enabled = true`: Send a notification when the workflow starts.
- `onSuccess.enabled = true`: Send a notification when the workflow completes successfully.
- `onError.enabled = true`: Send a notification when the workflow fails.
- `onComplete.enabled = true`: Send a notification when the workflow completes (regardless of success or failure).

:warning: The default messages use the `name` and `version` fields from the workflow manifest. Make sure to set these fields in your pipeline's `nextflow.config` file:

```groovy
manifest {
    name = 'my-pipeline'
    version = '1.0.0'
}
```

## Event configuration

Each event configuration (`onStart`, `onSuccess`, `onError`, `onComplete`) supports the following options:

- `enabled`: Enable or disable notifications for the event.
- `template`: Path to a custom JSON template file for the notification message. If not provided, a default template will be used.

## Make your own template

You can customize the notification messages by providing your own JSON template files. The templates should follow the [Adaptive Cards](https://adaptivecards.io/) format with GString interpolation for dynamic content.

See the default templates in the [`src/main/resources`](src/main/resources) directory for examples.

The following variables are available for use in the templates:

### `session` - The Nextflow session object, which contains metadata about the workflow execution.

This is basically a copy of the `Session` object available in Nextflow pipelines. This object can be used to fetch information about the workflow execution, such as the workflow name, ID, start time, end time, and status.

Take a look at the [source code](https://github.com/nextflow-io/nextflow/blob/master/modules/nextflow/src/main/groovy/nextflow/Session.groovy) to see all possible properties and methods.

Here's a list of commonly used properties:
- `session.manifest`: The workflow manifest object, which contains information about the workflow such as its name and version as configured by the pipeline. [Documentation](https://www.nextflow.io/docs/latest/reference/config.html#manifest)
- `session.workflowMetadata`: An object containing metadata about the workflow execution, such as its ID, start time, end time, success status, and error message (if any). Similar to the `workflow` object available in Nextflow pipelines. Take a look at the [source code](https://github.com/nextflow-io/nextflow/blob/master/modules/nextflow/src/main/groovy/nextflow/script/WorkflowMetadata.groovy) for all possible properties and methods.

### `event` - The TaskEvent object (only available in the `onError` event).

This object contains information about the task event that caused the workflow to fail. It can be used to fetch details about the failed task, such as its name, command, exit status, and error message.

It has two properties that can be used to access the failed task's details:

#### `handler` - The TaskHandler object (only available in the `onError` event).

This object contains information about the task that caused the workflow to fail. It can be used to fetch details about the failed task, such as its name, command, exit status, and error message.

Take a look at the [source code](https://github.com/nextflow-io/nextflow/blob/master/modules/nextflow/src/main/groovy/nextflow/processor/TaskHandler.groovy) to see all possible properties and methods.

#### `trace` - The TraceRecord object (only available in the `onError` event).

This object contains performance metrics and resource usage statistics for the failed task.

Take a look at the [source code](https://github.com/nextflow-io/nextflow/blob/master/modules/nextflow/src/main/groovy/nextflow/trace/TraceRecord.groovy) to see all possible properties and methods.

## Building

To build the plugin:
```bash
make assemble
```

## Testing with Nextflow

The plugin can be tested without a local Nextflow installation:

1. Build and install the plugin to your local Nextflow installation: `make install`
2. Run a pipeline with the plugin: `nextflow run hello -plugins nf-teams@0.1.0`

## Publishing

Plugins can be published to a central plugin registry to make them accessible to the Nextflow community. 


Follow these steps to publish the plugin to the Nextflow Plugin Registry:

1. Create a file named `$HOME/.gradle/gradle.properties`, where $HOME is your home directory. Add the following properties:

    * `npr.apiKey`: Your Nextflow Plugin Registry access token.

2. Use the following command to package and create a release for your plugin on GitHub: `make release`.


> [!NOTE]
> The Nextflow Plugin registry is currently available as preview technology. Contact info@nextflow.io to learn how to get access to it.
> 
