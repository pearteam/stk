# Sebo Toolkit
This software is a skeleton for java code snipets.
It is design to work in backbround and be accesible from system tray.
There is an interface Pluginable to add new snipets.
Interface Tooltipable provides way to display various data when mouse pointer will be on tray icon.

Timebalance plugin

   Main puprose of this plugin is to compute working time of user. In basic words working time is when this application is on and computer is working.
   Plugin is making entries in file located in user folder: .stk\.timebalance\timeent2015.txt that look like this:

2015-01-25 18:36 19:12

2015-01-25 19:18 19:34

2015-01-25 19:53 19:53
   
It can be modiffied by user to add or remove time gaps.
When computer is hibernated or suspended (stand by) working time is not counted.

When user move his mouse pointer on Tray Icon it will be displayed summary about working time. Items are configurable in Settings.

TW:  This week

TM:  This month

T:   Today

LR:  Last run

LW:  Last week

LM:  Last month

LWD: Last working day



About build

Maven build allows pack all in one jar:
>mvn clean compile assembly:single
