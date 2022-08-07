# 1.1.0.0
* Added logging for players using items on air
* Added logging for players interacting with entities
* Added logging for players killing entities
* Block interact event now logs the player's interaction hand and held item
* Reworked format of config file. Old configs will be partially reset when the game is loaded, if you have custom regex filters then you may want to make a backup copy of your config before updating.
* Logging rules are now specified per event type instead of globally.
* Logging rules are now checked in the order specified (top to bottom).
* Now uses SLF4J instead of Log4J (consistent with vanilla/forge).
* Block positions are now logged as `pos={x=X,y=Y,z=Z}` instead of `pos=BlockPos{x=X, y=Y, z=Z}`
* Blocks are now logged as `block={namespace:id}` instead of `block=Block{namespace:id}`
