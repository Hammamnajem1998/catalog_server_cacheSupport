# Catalog-Server

we made this catalog server for storing the books on some kind of data base .
this server can receive search and lookup request (these requests come from front end server), and can receive pay request (this request come from order server).


what you want to change ? 

there is one line to change to make the server work on your machine. 
line 177 make a connection with front_end_server to make invalidate request, so you have to change the previous front_end_server URL with 
your new front_end_server URL .
