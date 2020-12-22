from java.util.function import Consumer
from de.unistuttgart.iste.rss.oo.hamstersimulator.datatypes import Direction

class HamsterMain(Consumer):
    def accept(self, territory):
        self.paule = territory.getDefaultHamster()
        self.run() 

    def run(self):
        self.moveNTimes(10)
        for i in range(0, 6):
            self.putNGrains(i * 2 + 1)
            self.downAndTurn()
        self.moveNTimes(5)
        for i in range(0, 3):
            self.putNGrains(3)
            self.downAndTurn()
            self.paule.move() 
    
    def faceTo(self, direction):
        while self.paule.getDirection() != direction:
            self.paule.turnLeft()
            
    def markCurrentTile(self):
        self.execNTimes(12, self.paule.putGrain)

    def moveNTimes(self, times):
        self.execNTimes(times, self.paule.move)

    def putNGrains(self, times):
        self.execNTimes(times, self.putAndMove)
            
    def turnNTimes(self, times):
        self.execNTimes(times, self.paule.turnLeft)
        
    def execNTimes(self, times, operation):
        for _ in range(0, times):
            operation()
            
    def putAndMove(self):
        self.markCurrentTile()
        self.paule.move()

    def downAndTurn(self):
        previousDirection = self.paule.getDirection()
        self.faceTo(Direction.SOUTH)
        self.paule.move()
        if previousDirection == Direction.EAST:
            self.faceTo(Direction.WEST)
        else:
            self.faceTo(Direction.EAST)