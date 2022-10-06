Web application to accept signals and execute commands accordingly.

When a signal is called and found in service-config.conf, each command associated with it will execute.
If a signal is not found in the config, then the service defaults to execute signal 0 via a caught NullPointerException.

Two GET mappings:
  base path: /home

  1) called like ../int/3
  2) called like ../json?signal=%7B"signal":"4"%7D
    %7B and %7D are html coding for '{' and '}'

service-config.conf:
  All signal calls added here, including new ones.
  Each signal maps to a list of commands
 
    
