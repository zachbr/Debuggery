Debuggery [![Build Status](https://ci.destroystokyo.com/buildStatus/icon?job=Debuggery)](https://ci.destroystokyo.com/job/Debuggery)
=========

## Building
Debuggery requires [Java 8] to build.

Once cloned, use the gradle wrapper included in this repository to build a runnable jar.

`./gradlew build`

You will find a compiled version in the `./build/libs/` directory.


[Java 8]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

## What is it?
Debuggery is a small plugin designed to expose API values at runtime.

![/dplayer setHealth(.)](https://i.imgur.com/VZV6wsk.png)
![/dworld setStorm(.) true](https://i.imgur.com/MNamG2q.png)
![/dentity setFireTicks(.) 100](https://i.imgur.com/Uv8toKI.png)
![/dplayer setFlying(.)](https://i.imgur.com/Fv8PUC1.png)
![/dworld createExplosion(..) world,-237,64,401 100](https://i.imgur.com/IlqBRhk.png)
![/ditem getEnchantments](https://i.imgur.com/A8zXdSk.png)

## Why?
It is often helpful to see exactly what and how Bukkit will provide you with a piece of data. However, writing
an entire plugin just for that to see that one output value is stupid.

Also because it can be useful right after a major game update.

## How do I use it?
To get a command listing, type /debuggery and hit tab.
The plugin is very reliant on tab auto completion, so try it on any command.

## How does it handle more complicated objects?
There is basic support for formatting object types without proper toString() methods and/or those that have poor
toString() methods. Every one of them has to be added manually so right now I'm most concerned with supporting those
that the API returns. If you find any missing, feel free to open an issue or contribute them.

## Can I set values using Debuggery?
Yes. Currently input handling is rather basic.

Arguments are delimited by spaces as per Bukkit, therefore when you specify more complex inputs it is not particularly
friendly. For example, to specify a Location, you must enter `worldName,x,y,z` as in `/dplayer teleport(.) world,1,100,1`.
I will eventually get around to investigating some sort of standardized patterns for this, perhaps even moving away
from Bukkit's space delimiter system.

Furthermore, every conversion from `string -> object` is added manually, so some are currently missing. Feel free to
open an issue or contribute them.

## What's the biggest limitation at the moment?
~~You can only access top level methods directly on the object you're interfacing with. You cannot (currently) call a
method on the result of another method. This is planned but not currently supported.~~

## Do you have a timeline for feature development?
Nope. I am pretty busy. If you'd like something done sooner, feel free to contribute.
