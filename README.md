Web application to accept signals and execute commands accordingly.

Two GET mappings:
  base path: /home

  1) called like ../int/3
  2) called like ../json?signal=%7B"signal":"4"%7D
    %7B and %7D are html coding for '{' and '}'

service-config.conf:
  All signal calls added here, including new ones.
  Each signal maps to a list of commands
    
    
