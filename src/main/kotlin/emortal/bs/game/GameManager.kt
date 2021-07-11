package emortal.bs.game

import net.minestom.server.entity.Player
import java.util.concurrent.ConcurrentHashMap

object GameManager {
    private val gameMap: ConcurrentHashMap<Player, Game> = ConcurrentHashMap<Player, Game>()
    private val games: MutableSet<Game> = HashSet()
    private var gameIndex = 0

    /**
     * Adds a player to the game queue
     * @param player The player to add to the game queue
     */
    fun addPlayer(player: Player, game: Game = nextGame()): Game {
        game.addPlayer(player)
        gameMap[player] = game

        return game
    }

    fun removePlayer(player: Player) {
        this[player]?.removePlayer(player)

        gameMap.remove(player)
    }

    fun createGame(options: GameOptions = GameOptions()): Game {
        val newGame = Game(gameIndex, options)
        games.add(newGame)
        gameIndex++
        return newGame
    }

    fun deleteGame(game: Game) {
        game.players.forEach(gameMap::remove)
        games.remove(game)

        gameIndex--
    }


    fun nextGame(): Game {
        return games.firstOrNull { it.gameState == GameState.WAITING_FOR_PLAYERS }
            ?: createGame()
    }

    operator fun get(player: Player) = gameMap[player]

}