package ai

import java.util.*

fun main(args: Array<String>) {

    val level = 10
    class Node(val x: Int, val y: Int) {
        var father: Node? = null
        var depth = 0

        fun output() {
            println("($x,$y),depth=$depth")
        }
    }

    fun isEqual(a: Node, b: Node): Boolean {
        if (a.x == b.x && a.y == b.y)
            return true
        return false
    }

    val maze = arrayOf(       //S是起点,G是终点
            charArrayOf('1', '1', '1', '0', '0', '0', '0', '0', '1', 'G'),
            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '1', '0', '0', '0', '0', '1', '1', '1'),
            charArrayOf('0', '0', '0', '1', '0', '1', '0', '0', '0', '1'),
            charArrayOf('0', '1', '0', '1', '0', '1', '1', '1', '0', '1'),
            charArrayOf('0', '1', '0', '1', '0', '0', '0', '1', '0', '0'),
            charArrayOf('0', '1', '0', '1', '1', '0', '0', '1', '0', '0'),
            charArrayOf('0', '1', '0', '0', '1', '1', '1', '1', '0', '0'),
            charArrayOf('S', '1', '1', '0', '0', '0', '0', '0', '0', '0')
    )

    val visited = Array(level, { Array(level) { _ -> false }})

    val direction = arrayOf(     //上下左右
            intArrayOf(-1, 0),
            intArrayOf(1, 0),
            intArrayOf(0, -1),
            intArrayOf(0, 1)
    )

    lateinit var start: Node
    lateinit var goal: Node

    val path = ArrayList<Int>()
    var runTime: Long
    val queue = LinkedList<Node>()


    fun solvePath() {
        for (i in 0 until level) {
            for (j in 0 until level) {
                if (maze[i][j] == 'S') {
                    start = Node(i, j)      //起点
                    visited[i][j] = true
                }
                if (maze[i][j] == 'G') {
                    goal = Node(i, j)       //终点
                }
            }
        }


        val startTime = System.currentTimeMillis()
        queue.push(start)
        while (!queue.isEmpty()) {
            val node = queue.pollFirst()
            if (isEqual(node, goal))  {
                goal = node
                break
            }
            for (i in 0..3) {
                val x = node.x + direction[i][0]
                val y = node.y + direction[i][1]
                if (x in 0..(level - 1) && y in 0..(level - 1) && maze[x][y] != '1' && !visited[x][y]) {
                    val newNode = Node(x, y)
                    newNode.father = node
                    newNode.depth = node.depth + 1
                    queue.add(newNode)
                    visited[x][y] = true
                }
            }
        }

        val endTime = System.currentTimeMillis()
        runTime = endTime - startTime

        var node = goal
        while (true) {
            if (node.father != null) {
                for (i in 0..3) {
                    if (node.father!!.x + direction[i][0] == node.x && node.father!!.y + direction[i][1] == node.y) {
                        path.add(i)
                        break
                    }
                }
                node = node.father!!
            } else {
                break
            }
        }
        path.reverse()
        println(path.size)
        println(runTime)
    }

    solvePath()
}