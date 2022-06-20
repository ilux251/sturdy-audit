module.exports = function(grunt) {
  'use strict';
  
  var os = (function(){
      var platform = process.platform;
      if (/^darwin/.test(platform)) { return "mac"; }
      if (/^linux/.test(platform)) { return "linux"; }
      if (/^win32/.test(platform)) { return "win32"; }
      return null;
  })();


  var exe = {
      mac: "./node_modules/electron/dist/Electron.app/Contents/MacOS/Electron .",
      win32: "node_modules\\electron\\dist\\electron.exe",
      linux: "./node_modules/electron/dist/electron"
  };

  
  //------------------------------------------------------------------------------
  // ShellJS
  //------------------------------------------------------------------------------
  var shell = require('shelljs');

  
  //------------------------------------------------------------------------------
  // setup Tasks
  //------------------------------------------------------------------------------
  grunt.registerTask('setup', []);

  
  //------------------------------------------------------------------------------
  // launch Tasks
  //------------------------------------------------------------------------------
  
  grunt.registerTask('launch', function(async) {
      var IsAsync = (async == "true");
      grunt.log.writeln("\nLaunching development version...");
      var local_exe = exe[os];
      shell.exec(local_exe + " .", {async:IsAsync});
  });

  
  //------------------------------------------------------------------------------
  // Default Task
  //------------------------------------------------------------------------------
  
  grunt.registerTask('default', ['setup','launch']);


  // end module.exports
};