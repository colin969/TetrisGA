# TetrisGA
DSP Tetris project

Tetris Guidelines -
http://tetris.wikia.com/wiki/Tetris_Guideline

Tetris Friends Docs -
http://www.tetrisfriends.com/help/tips_appendix.php

Using the program:

When first launched it will immediately start training games with a step rate of 1 per second.

Hotkeys:  
	[ and ] - Increase or decrease step rates  
	R - Disable rendering, put step rate to infinity. Good for quicker training data.  
	L - Load an AI. Takes a list of weights seperated by tabs, can be copy and pasted directly from Microsoft Excel rows.  
			NOTE : Java doesn't always put this dialog window on the top, it may be hidden behind the game window. Invalid inputs will end the program.  
	A - Enable debug output of the left hand player. Displays the scoring of a taken action, resets to off after each game.  
	Space - Pause the game.  
	Enter - Play against the solution on the right board. Pressing it again will resume training where it left off.  
	
Game Controls:  
    Up - Hard Drop. Piece falls all the way to the bottom and locks in.  
    Down - Soft Drop. Drops 1 row, can be held to fall more. Locks when soft dropping at the bottom.  
    Left and Right - Move a piece 1 column left or right, can be held down.  
    Z and X - Rotate a piece anti-clockwise or clockwise  
    C - Swap current piece with the hold piece, displayed as a small piece below the Lines counter.  
		
