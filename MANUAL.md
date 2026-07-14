# Microsoft Teams Notifications with Power Automate (Workflows)

Since Microsoft has retired legacy Office 365 Connector webhooks in MS Teams, sending notifications must be migrated to **Power Automate-based Workflows**. 

The `nf-teams` plugin natively supports these new endpoints because it sends standard JSON payloads (like Adaptive Cards) via HTTP POST.

This manual outlines two methods to restore notification functionality:
1. **Method 1: Teams Workflows App (Standard & Free)** — Quickest, uses Microsoft Teams' built-in webhook templates, and is free (no premium license needed).
2. **Method 2: Custom Power Automate Flow (Premium & Extensible)** — Best for custom processing (e.g. sending emails, writing to SharePoint, custom routing) before posting to Teams.

---

## Method 1: Using Teams Workflows App (Recommended)

This method uses the built-in **Workflows** app in Teams. It creates a webhook receiver that expects an **Adaptive Card** in a standard envelope. The default templates provided by the `nf-teams` plugin match this format out of the box.

### Step-by-Step Setup:

1. Open **Microsoft Teams**.
2. Navigate to the **Channel** where you want to receive Nextflow notifications.
3. Click the **More options (...)** button in the top right corner of the channel or next to the channel name, then choose **Workflows**.
4. In the Workflows app, search for the template:
   * **"Post to a channel when a webhook request is received"** (or **"Send webhook alerts to a channel"**).
5. Select the template.
6. Verify your account connection, select the desired **Team** and **Channel** where messages should be posted, and click **Next**.
7. Click **Add workflow** (or **Submit**).
8. Once created, the workflow will display a unique **Webhook URL**. 
9. **Copy this URL** for your Nextflow configuration.

### Configure Nextflow:
Paste the copied Webhook URL into your `nextflow.config` file:

```groovy
plugins {
    id 'nf-teams@0.3.0'
}

teams {
    enabled = true
    webHook {
        url = 'https://prod-XX.westus.logic.azure.com:443/workflows/.../invoke?api-version=...'
    }
}
```

---

## Method 2: Custom Power Automate Flow (Extensible)

If you need advanced routing, custom logic, or want to send emails or log runs in SharePoint instead of just posting to a channel, you can set up a custom flow.

### Step-by-Step Setup:

1. Go to the [Power Automate Portal](https://make.powerautomate.com/).
2. Click **Create** -> **Automated cloud flow** or **Instant cloud flow**.
3. Set the trigger to **"When an HTTP request is received"** (Note: this is a Premium connector).
4. Save the flow to generate the HTTP POST URL.
5. Under the trigger options, you will need to provide a **Request Body JSON Schema** so Power Automate can parse the incoming payload. Choose one of the options below depending on your templates:

#### Option A: If using the default Adaptive Card templates
The default templates wrap the Adaptive Card inside an envelope. Use this JSON Schema:

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "type": { "type": "string" },
    "attachments": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "contentType": { "type": "string" },
          "content": { "type": "object" }
        },
        "required": ["contentType", "content"]
      }
    }
  },
  "required": ["type", "attachments"]
}
```

#### Option B: If using a custom flat JSON template (Recommended for custom flows)
You can define a custom JSON template in Nextflow to send clean variables to Power Automate.
For example, create a file named `nextflow-payload.json`:

```json
{
  "runName": "${session.workflowMetadata.runName}",
  "success": ${session.workflowMetadata.success},
  "duration": "${session.workflowMetadata.duration}",
  "nextflowVersion": "${session.workflowMetadata.nextflow.version}"
}
```

Configure `nf-teams` to use this custom template:
```groovy
teams {
    enabled = true
    webHook {
        url = '<YOUR_HTTP_REQUEST_URL>'
    }
    onSuccess {
        template = 'nextflow-payload.json'
    }
}
```

Then, use this corresponding **Request Body JSON Schema** in Power Automate:
```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "runName": { "type": "string" },
    "success": { "type": "boolean" },
    "duration": { "type": "string" },
    "nextflowVersion": { "type": "string" }
  },
  "required": ["runName", "success", "duration"]
}
```

6. Add the next steps in your flow (e.g. **Post card in a chat or channel**, **Send an email (V2)**, etc.) utilizing the parsed dynamic fields.
7. Copy the generated **HTTP POST URL** and configure your `nextflow.config` file.

---

## Configuration Reference

The following configuration options are available for `nf-teams`:

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
| `teams.enabled` | Boolean | `false` | Enable or disable MS Teams notifications |
| `teams.webHook.url` | String | `''` | The Webhook URL (from Teams Workflows or Power Automate) |
| `teams.onStart.enabled` | Boolean | `false` | Send notification when workflow starts |
| `teams.onStart.template` | String | (Default template) | Path to custom JSON template for the start event |
| `teams.onSuccess.enabled` | Boolean | `false` | Send notification on success |
| `teams.onSuccess.template` | String | (Default template) | Path to custom JSON template for the success event |
| `teams.onError.enabled` | Boolean | `false` | Send notification on error |
| `teams.onError.template` | String | (Default template) | Path to custom JSON template for the error event |
| `teams.onComplete.enabled` | Boolean | `false` | Send notification on completion |
| `teams.onComplete.template` | String | (Default template) | Path to custom JSON template for the completion event |

### Metadata requirement
The default notification templates expect pipeline metadata from your pipeline's config file:

```groovy
manifest {
    name = 'my-pipeline'
    version = '1.0.0'
}
```
