# TheWalkingDev Hack Zurich 2016
This project is an IntelliJ plugin that requires you to walk a certain amount of steps every hour. If you don't reach your goal you get locked out of IntelliJ until you've reached your goal.

This Project consist of the following Parts:
* intellj-plugin - The actual IntelliJ Plugin
* server - A django backend for syncing the walking progress
* webclient - An Angular 2 Dashboard which summarizes some key figures