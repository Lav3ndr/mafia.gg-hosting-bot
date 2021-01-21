# mafia.gg hosting bot
- written for fun by Lavender with contributions/original code from a random Zy
- App.java contains the main loop and upper level functionality
- MafiaSession.java contains the lower-level code which interacts with the browser
- in my experience the biggest difficulty for this project was just configuring eclipse to work with selenium/chromedriver. this tutorial got me started: [https://www.guru99.com/installing-selenium-webdriver.html](https://www.guru99.com/installing-selenium-webdriver.html)
- there needs to be a file called "credentials.txt" in the same directory as chromedriver.exe which contains the username and password of the mafia.gg account to use for the bot (username on first line, password on the second line)
- if the program terminates abnormally, you need to reset to default conplan settings manually (majority vote off, role reveal off). setup changes are done relative to this.
- apologies for the shitty/almost entirely absent documentation and comments
- yes, I know this code could be optimized greatly
- to do:
	- commands to add: .expand, .despand (figure out how to deal with these intelligently, will probably involve low-level functions to read the size of a setup and current number of players in the room), .ban [user] (would open a new tab and add user to the ban list, then return to main tab)
	- functionality to add: autokick users who spam commands (have a set number of commands allowable for each user which decrements to zero and slowly recharges; warn players when low)
	- functionality to add: allow a list of setup codes to be given for a closed setup
	- fix and robustify the functions which set day and night length
	- add functions to change deadlock prevention
- anyone is welcome to use this code, but I ask that you please let me know if you are going to use it! I'd love to know what it's being used for.