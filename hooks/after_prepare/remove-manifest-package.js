#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

module.exports = function (context) {
  const manifestPath = path.join(
    context.opts.projectRoot,
    'platforms',
    'android',
    'app',
    'src',
    'main',
    'AndroidManifest.xml'
  );

  if (!fs.existsSync(manifestPath)) {
    console.warn('[Hook] AndroidManifest.xml not found:', manifestPath);
    return;
  }

  let content = fs.readFileSync(manifestPath, 'utf8');
  const updatedContent = content.replace(/\s*package="[^"]*"/, '');

  if (content !== updatedContent) {
    fs.writeFileSync(manifestPath, updatedContent, 'utf8');
    console.log('[Hook] Removed package attribute from AndroidManifest.xml');
  } else {
    console.log('[Hook] No package attribute found in AndroidManifest.xml');
  }
};
