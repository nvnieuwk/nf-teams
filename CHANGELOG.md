# Changelog

## 0.2.0

1. Removed the hook URL from the log messages
2. Fixed warnings with configuration options

## 0.1.1

1. The configuration option `teams.enabled` was not checked, making the plugin always active when used. This has now been fixed and the plugin will only work when this option is set to `true`.