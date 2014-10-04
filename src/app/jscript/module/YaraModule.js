ScriptRunner.require("classpath:util/io.js");

function YaraModule(config) {
  this.YARA_DEFAULT_CONFIG = {
    rpcPort: 8463, 
    rpcHost: "127.0.0.1", 
    server: "generic"
  };

  this.config =  config != undefined ? config : this.YARA_DEFAULT_CONFIG ;

  this.install = function() {
    console.h1("Install the module YaraServer on the server " + this.config.server) ;
    SHELL.exec(
        "module install " + 
        " -Pmodule.data.drop=true" +
        " -Pyara:rpc.port=" + this.config.rpcPort +
        " --autostart --module YaraServer --member-name " + this.config.server
    ) ;

    console.h1("Install the module YaraClient on all the servers ") ;
    SHELL.exec(
        "module install " + 
        " -Pmodule.data.drop=true" +
        " -Pyara:rpc.host=" + this.config.rpcHost +
        " -Pyara:rpc.port=" + this.config.rpcPort +
        " --autostart --module YaraClient --timeout 10000"
    ) ;
  };

  this.uninstall = function() {
    SHELL.exec(
      "module uninstall " +
      "  --member-name " + this.config.server +
      "  --module YaraServer --timeout 20000"
    ) ;

    SHELL.exec(
      "module uninstall  --module YaraClient --timeout 20000"
    ) ;
  };
}
