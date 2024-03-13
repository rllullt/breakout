# [EN] Breakout

This is a 2D game developed in Java 8 along the course “Design Methodologies and Programming”.
The graphics are made with JavaFX, and the controller with the FXGL library.
It follows the Model-View-Controller methodology.

The base idea is the Breakout game.
This is an arcade game that consists in using a horizontal bar at the bottom of the screen and a ball that bounces on the bar, the walls, and the blocks (bricks) located in the upper part of the screen.
Every time the ball hits a brick, it bounces and can destroy the brick, giving the player a score according to the difficulty of destroying it (it may take more than one hit to destroy a brick).
If the ball hits the bottom of the screen, it is a lost ball and the player loses a life.

Metal bricks have a different behaviour: they give the player an extra ball, but no points.
Also, it is not necessary to eliminate all the metal bricks from a level to continue to the next one (since they do not have intrinsic score).


## Running the game

The game is old.
To date (March 2024), the java LTS version is Java 21.
This game was made for executing it with Java 8, though at its creation date is was already available Java 11.

The program is _easily_ run in IntelliJ, setting up the src/main/java folder as the sources one, and the src/test/java folder as the tests one.
Then, the only thing that has to be done is executing the BigTestT2 for testing, or any other testing file.

The game starts with an initial level
The only thing that has to be done, then, is to run the BigTestT2 or whatever other test file in the test folder.

The game starts with an initial level, where the player can play.
To pass to another level it is indispensable to generate a new level before, with the ‘N’ key.
This is because if there are no more levels, the game finishes (and the finishing part of this application is not too intuitive: it only makes the bricks disappear and nothing more happens).

### Controls

- N: New level, it is added to the level queue
- → (right arrow): Move the bottom bar to the right
- ← (left arrow): Move the bottom bar to the left
- SPACE: Throw the ball to start the game


## Design Patterns Used

When I decided the patterns before the start of coding, I really didn’t know _how_ or _where_ to implement them.
The only available information was that at some moment of the game, bricks would needed to send notifications to the controller, and that the logic reuse was important, based on previous experience.
The patterns used were:


### Observer Pattern

The Observer Pattern was implemented to make a _refresh_ of the application, because it was ideal to generate updates in the game process.
Also, it was used as a tool where handling events from the game to the gui was necessary.
It was used in scenarios like the folloing:
- Every time a brick was destroyed, i.e., a change was made in the game progress, the brick sent a notification to its observers (in this case only the game controller).
- When the game needed to say “change of level”, it notified the GUI, in order to change the level in the graphics.

It was necessary to import the Observer interface in the Game class, so that the game could implement it.
Also, it was necessary to import the Observable class in the AbstractBrick class, so that the bricks could extend it.

It is important to notice that not every time a brick reaches life 0 it becomes destroyed, due to how the brick logic was implemented.
Only the first time a brick reaches life 0 it is ‘destroyed’.

The Game class implemented the `update` method from the Observer, in order to apply a visitor like double dispatch.
Here is where the Visitor Pattern appears.


### Visitor Pattern

The Visitor Pattern was implemented in the part of receiving the notifications from bricks. It was a single line of code in the Game class:
```java
@Override
public void update(Observable o, Object arg) {
    ((Brick) o).accept(this);
    checkLevelChange();
}
```
It was made in this way because somebody doesn’t know at first what was the type of the observable object that sent the notification.
The checkLevelChange() part was to check if, given the new changes in the execution of the game, it was necessary to change to the next level.

So, the default response of the bricks (implemented in the AbstractBrick class) resulted in a single line too:
```java
@Override
public void accept(Game game) {
    game.raiseScore(score);
}
```
This increases the player’s score in the amount of points the brick had.

But, in MetalBrick class, this response had to be different, because this brick _was different_.
```java
@Override
public void accept(Game game) {
    game.raiseBall(); // increase the amount of balls in 1
}
```
This increases the amount of avilable balls the player had.


### Factory Pattern

To make the resources to the GUI, there was implemented a class called BreakoutFactory.
This class had the main purpose of generating the objects that were going to be drawn in the scene.
The factory was present in the part of parameters specification: it was mainly used for telling what parameters would be good to have in an object.
For example, the code to generate a new Player was the following:
```java
static Entity newPlayer(double x, double y) {
	PhysicsComponent physicsComponent = new PhysicsComponent();
	physicsComponent.setBodyType(BodyType.KINEMATIC);
	physicsComponent.setFixtureDef(new FixtureDef().density(0f).restitution(0f).friction(1f));
	return Entities.builder()
			.at(x - playerWidth / 2.0, y - playerHeight)
			.type(Types.PLAYER)
			.viewFromNodeWithBBox(FXGL.getAssetLoader()
							.loadTexture("player.png", playerWidth, playerHeight))
			.with(physicsComponent, new CollidableComponent(true))
			.with(new PlayerControl())
			.build();
}
```
It can be noted the _.at()_, _.type_, _.with_, etcetera, so we could choose the parameters we wanted.


### Use of the PhysicsComponent

The most important part of the application was the physics component we had to append to every element that was going to collide with others.
This component allowed to detect collisions and to generate _Collision Handlers_ to do something with them.
In the example of the player, the _KINEMATIC_ type let the bar of the player be moved with the arrow keys.

### Use of the SomethingController

One really important implemented thing in the game was the use of Controllers for the objects in the graphics.
The Controllers were FXGL components (i.e., classes that inherited from the Component class), that saved some logic to manage the figures.
This logic was directly associated with the _Logic_ package. For example, the BrickController had a Brick inside it.

There where implemented PlayerControl and BrickControl classes.
The Player control was mainly taken from the AlmasB example of bar to make it not to go out of the screen.
The other feature of this control, not implemented by AlmasB, was to make the player not to move when the ball was _not shot_.
This was implemented by a boolean parameter.
The code for the construction of the player could be found at the _Factory Pattern_ section.

The Brick control was made to manipulate the scores and the amount of balls, also to indicate the GUI when to remove a brick.
This is the reason why a Drawn Brick had a Brick Control, and the Brick Control, the Brick associated with the Controller.
So, when a collision between the ball and the brick was detected, the game told the Brick Controller to _hit_, and then, internally, the Brick Controller told the Brick of the Logic package to hit.

Sometimes, when a brick was destroyed, the game asked the Brick Controller if the brick was destroyed.
If that was the case, the Breakout removed the brick from the GameWorld.
This feature also helped to the Observer pattern implemented before: when a brick was destroyed, the brick notified the Game, so when there where no more bricks to break, the game passed to the next level.
But here entered the Observer pattern implmented in this new part: when the level was finished, the game notified to change the level, too.

### Extras

An extra functionality implemented was that the bricks distribution of the levels _increase_ in difficulty:
In the beginning, the probability of glass (the weakest brick, it requires 1 hit to be destroyed) is 1 and, while the number of levels increase, this probability decreases by the formula
`probability = level / level^1.5`,
where level is the number of the current level.

Also, the probability of generating a metal brick increases while the levels increase, by the formula
`probabilty = level / (level + 6)`,
so the number of metal bricks is higher every time.

Another extra functionality was that all the componentes but the ball where drawn with sprites, taken from images.
The ball was drawn with a JavaFX circle of silver color.



# [ES] Breakout

Este es un juego en 2D realizado en Java 8 durante el curso «Metodologías de diseño y programación».
Los gráficos están realizados con JavaFX, y el controlador con la librería FXGL.
Sigue la metodología Modelo Vista Controlador.

La idea base es el juego Breakout.
Este es un juego de arcade que consiste en utilizar una barra horizontal en la parte inferior de la pantalla y una bola que rebota en la barra, las paredes, y los bloques (ladrillos) en la parte superior de la pantalla.
Cada vez que la bola choca con un ladrillo, esta rebota y puede destruirlo, atribuyendo un puntaje al jugador acorde a la dificultad de destruirlo (puede tomar más de un golpe destruir un ladrillo).
Si la bola toca la parte inferior de la pantalla, se considera una bola perdida y el jugador pierde una vida.

Los ladrillos de metal tienen un comportamiento diferente: le dan al jugador una bola adicional, pero no puntos.
Además, no es necesario eliminar todos los ladrillos de metal para pasar al siguiente nivel (dado que no tienen un puntaje intrínseco).


## Ejecutar el juego

El juego es antiguo.
A la fecha (marzo de 2024), la versión LTS de java es Java 21.
Este juego fue hecho para ejecutarse con Java 8, aunque a la fecha de su creación ya estaba disponible Java 11.

El programa puede ejecutarse _fácilmente_ en IntelliJ, definiendo la carpeta `src/main/java` como la carpeta de «sources», y la carpeta `src/test/java` como la carpeta de tests.
Después, lo único que hay que hacer es ejecutar el BigTestT2 para testear, o algún otro archivo de test.

El juego comienza con un nivel inicial, donde el jugador puede jugar.
Para pasar a otro nivel, es indispensable generar un nuevo nivel de antemano, con la tecla ‘N’.
Esto es porque si no hay más niveles, el juego termina (y la forma de terminar de esta aplicación no es muy intuitiva: solo hace que los ladrillos desaparezcan y no pasa nada más).

### Controles

- N: Nuevo nivel, se agrega a la cola de niveles
- → (flecha a la derecha): Mover la barra inferior a la derecha
- ← (flecha a la izquierda): Mover la barra inferior a la izquierda
- ESPACIO: Lanzar la bola para comenzar el juego