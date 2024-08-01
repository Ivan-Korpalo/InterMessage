### This program is an amateur project meant to test my ability to write network programs. No effort has been made towards encryption and security. USE AT YOUR OWN RISK!

This software needs to be run from a command line. If you double-click the JAR, nothing will happen.

This early prototype uses a primitive protocol where Senders and Receivers take turns sending messages to one another. A user in Receiver mode cannot send messages and waits for a user in Sender mode to send them one.
Once the Receiver gets the message, the Sender and Receiver swap states and the recipient can now write a response.

## Receiver mode

To launch the program in the Receiver mode, navigate to the JAR's directory and type: 
>java -jar InterMessage.jar False [port#]

where [port#] is the port you want to listen on to make a connection.
If you want to someone to connect to you from the internet rather than just the LAN, make sure the [port#] you chose is one that is port-forwarded.
Additionaly you may have to give this program permissions in the firewall in order to receive messages.

Once all of that is done though, you now have a program that will await a connection from a Sender. Once a sender connects, they can send you a message and then you can send one back. And then you can go back and forth over and over again.

## Sender mode

To launch the program in Sender mode, navigate to the JAR's directory and type:
>java -jar InterMessage.jar True [port#]  [Host address] 

where [port#] is the port of the sender you want to connect to and [Host address] is the host address of the sender you want to connect to.
This can be either IP address, web address, or even LocalHost if you're simultaneously running a Receiver mode instance on this machine

There must be a properly configured Receiver to connect to or else the program will throw an error. If there is a valid device, you should be able to establish a connection

## What to do with a connection?

So you've succesfully connected to a Receiver, now what? Now you type a command and hit Enter. We have 2 of them:

>QUIT

QUIT simply closes the program and terminates the connection. It's unlikely that this will be your first command, but you'll want to use it when you're done with the conversation.

>SAY [stuff]

This is how you send a message in this messenger application. [stuff] can be any String, so basically any kind of text. Example:

>SAY I have a lot of respect for your programming skills

Once you send your message, you will switch to receiver mode and then you will have to wait for a response.

***

And that's everything. Pretty basic program to be honest, but the intention is build on it and make somethng more feature complete.
