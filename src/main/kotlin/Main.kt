import kotlin.math.absoluteValue
import kotlin.random.Random

fun main() {
    val maxMoves = 25
    val maxExp = 10000
    val sequence = listOf(25)
    val centerProbability = 0.1

    var play1 = 0
    var play2 = 0

    var play1half = 0
    var play2half = 0

    for (i in 1..maxExp) {
        var currentLevel = 1
        val playerOne = PlayerOne("Player 1", centerProbability = centerProbability)
        val playerTwo = PlayerTwo("Player 2", sequence)
        var playerOneWin = false

        for (move in 1..maxMoves) {
            val offer = playerOne.makeOffer(currentLevel)
            val response = playerTwo.respond(offer, currentLevel)

            println("$move-$currentLevel)${playerOne.name} предлагает пойти $offer")

            if (response == "Stay") {
                println("$move-$currentLevel)${playerTwo.name} решил остаться на текущем уровне.")
            } else {
                println("$move-$currentLevel)${playerTwo.name} решил пойти $response")

                if (currentLevel == 2 && response == "Up") currentLevel += 1;
                else if (currentLevel != 4 && response == "Up") currentLevel += 1;
                else if (currentLevel == 4 && response == "Up") {
                    playerOneWin = true

                    if (i <= maxExp / 2)
                        play2 += 1
                    else
                        play2half += 1

                    break;
                } else if (currentLevel != 1 && currentLevel != 2 && response == "Down") currentLevel -= 1;
            }
        }

        if (!playerOneWin) {
            if (i <= maxExp / 2)
                play1 += 1
            else
                play1half += 1
        }
    }

    println("$play1 and $play2")
    println("$play1half and $play2half")
    var a = play1 + play1half
    var b = play2 + play2half

    var percentPlay1 = (play1.toDouble() / (maxExp / 2)) * 100
    var percentPlay1half = (play1half.toDouble() / (maxExp / 2)) * 100
    var percentPlay1all = (a.toDouble() / maxExp) * 100

    var percentPlay2 = (play2.toDouble() / (maxExp / 2)) * 100
    var percentPlay2half = (play2half.toDouble() / (maxExp / 2)) * 100
    var percentPlay2all = (b.toDouble() / maxExp) * 100

    println("Статистика игроков:")
    println("Игрок 1: $percentPlay1 - $percentPlay1half - $percentPlay1all")
    println("Игрок 2: $percentPlay2 - $percentPlay2half - $percentPlay2all")
}

class PlayerOne(val name: String, val centerProbability: Double = 0.5) {
    fun makeOffer(currentLevel: Int): String {
        if (currentLevel == 2) {
            return if (Random.nextDouble() < centerProbability) "Stay" else "Up"
        }
        else if (currentLevel == 1) {
            return "Up"
        }
        else {
            return if (Random.nextDouble() < centerProbability) "Down" else "Up"
        }
    }
}

class PlayerTwo(val name: String, private val sequence: List<Int>) {
    private val responses = sequence
    private var currentMove = 0
    private var currentIndex = 0

    fun respond(offer: String, currentLevel: Int): String {
        if (currentMove <= responses[currentIndex].absoluteValue) {
            currentMove += 1
        }
        else {
            currentMove = 0
            currentIndex += 1
        }

        if (offer == "Up" && currentLevel == 1) return "Up"
        else if (offer == "Up") return if (responses[currentIndex] > 0) "Up" else "Down"
        else if (offer == "Down") return if (responses[currentIndex] > 0) "Down" else "Up"
        else if (offer == "Stay") return if (responses[currentIndex] > 0) "Stay" else "Up"
        else return "Up"
    }
}