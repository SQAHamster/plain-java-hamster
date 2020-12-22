from de.unistuttgart.iste.rss.oo.hamstersimulator.external.model import HamsterGame
from de.unistuttgart.iste.rss.oo.hamstersimulator.ui.javafx import JavaFXUI
from de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes import Size
from de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes import Location
from de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes import Direction
from game.HamsterMain import HamsterMain

def initializeGame(game):
    builder = game.getNewTerritoryBuilder()
    builder.initializeTerritory(Size(10, 21))
    builder.defaultHamsterAt(Location(0, 0), Direction.EAST, 1000)
    game.initialize(builder)
    game.setSpeed(10)
    game.startGame()
    JavaFXUI.displayInNewGameWindow(game.getModelViewAdapter())

if __name__ == "__main__":
    game = HamsterGame()
    initializeGame(game)
    game.runGame(HamsterMain())
