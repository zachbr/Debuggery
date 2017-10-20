Debuggery
=========

## Building
Debuggery requires [Java 8] to build.

Once cloned, use the gradle wrapper included in this repository to build a runnable jar.

`./gradlew build`

You will find a compiled version in the `./build/libs/` directory.


[Java 8]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

## What is it?
Debuggery is a small plugin designed to expose API values at runtime.

![/dserver](https://i.imgur.com/jhLcrc2.png)
![/dchunk](https://i.imgur.com/wbTZvbA.png)
![/dplayer](https://i.imgur.com/8d6cxSE.png)

## Why?
It is often helpful to see exactly what and how Bukkit will provide you with a piece of data. However, writing
an entire plugin just for that to see that one output value is stupid.

Also because it can be useful right after a major game update.

## How do I use it?
To get a command listing, type /debuggery and hit tab.
The plugin is very reliant on tab auto completion, so try it on any command.

## How does it handle more complicated objects?
Right now? It doesn't. Objects that do not implement their own toString() method will simply show a memory location.
Most primitive types are well handled however. This is something that I'll be adding eventually.

## Can I set values using Debuggery?
Not right now. There's a lot of work to be done to properly deduce types from input, as well as keep the inputs
standardized across all commands. This is planned, but not currently supported.

## Do you have a timeline for feature development?
Nope. I am pretty busy. If you'd like something done sooner, feel free to contribute.
