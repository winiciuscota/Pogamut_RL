Pogamut_RL
==========

Pogamut - Applying reinforcement learning techniques to game bots

This is supposed to be a school project, im trying to implement the QLearning algorithm on a pogamut bot.

the Strategy is:

At each logic iteration the bot translates the environment into a game state, the state is fed into the 
agent which adds the state into the policy's states vector
the bot also has events that capture penalty and reward situations, which the agent uses to update the policy
The agent's policy maps states into actions, at each iteration the agent will return a action to the bot
The state is built from:

EnemiesCount - amount of visible enemies
NavPoint - nearest navpoint from the bot
Previousposition - previous navpoint, this was necessary to approximate the problem to a markov decision process
HealthLow - wheter the health is below 60 points
NearestEnemyPosition - nearest navpoint from the nearest enemy

AvailableActions - Available actions in the state(usually it is 2~4 possible adjancent navpoints for the agent to move, 
if there are visible enemies the engage action will also be available)

I believe that the NavPoints are the most efficient way to store info about map positions

the bot receives rewards from:

killing an enemy
damaging an enemy
picking up items

and receives penalties from:

getting damaged
being killed

So far the bot doesnt seems to be getting any smarter, i believe that a lot of tweaking on the reward/penalty strategy
and on the update function will solve this problem.
I believe it is possible to make the bot learn tatics related to the map using this strategy, but to keep realistic expectations
my goal is to make the bot learn an efficient route to collect itens. 
the bot starts moving randomly(like a drunk ant) if his movement on the map gradually gets more efficient
i think it will nice way to prove that he is actually learning. As a consequence, his survivability 
will also increase since picking up itens makes a huge difference on ut2004.
Any criticism is very much appreciated.

The project was built using the pogamut middleware on unreal tournament 2004.
Pogamut page: http://pogamut.cuni.cz/
