# Changelog

## 0.2.0

1. Removed the hook URL from the log messages
2. Fixed warnings with configuration options
3. Removed error messages being shown when message has been posted succesfully.
4. Added a warning if a wrong webhook URL is given instead of the cryptic error message

## 0.1.1

1. The configuration option `teams.enabled` was not checked, making the plugin always active when used. This has now been fixed and the plugin will only work when this option is set to `true`.