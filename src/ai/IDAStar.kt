package ai

import java.lang.Integer.min

fun main(args: Array<String>) {

    class Node(newX: Int, newY: Int) {
        var x = newX
        var y = newY
        var value = 0
        var depth = 0

        fun calculate(goal: Node) {
            value = depth + Math.abs(x - goal.x) + Math.abs(y - goal.y)
        }
    }

    fun isEqual(a: Node, b: Node) = a.x == b.x && a.y == b.y

    val m = 10
    val n = 10
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

//    val m = 20
//    val n = 20
//    val maze = arrayOf(       //S是起点,G是终点
//            charArrayOf('1', '1', '1', '0', '0', '0', '0', '0', '1', '0', '1', '1', '1', '0', '0', '0', '0', '0', '1', 'G'),
//            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
//            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
//            charArrayOf('0', '0', '1', '0', '0', '0', '0', '1', '1', '1', '0', '0', '1', '0', '0', '0', '0', '1', '1', '1'),
//            charArrayOf('0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1'),
//            charArrayOf('0', '1', '0', '1', '0', '1', '1', '1', '0', '1', '0', '1', '0', '1', '0', '1', '1', '1', '0', '1'),
//            charArrayOf('0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0'),
//            charArrayOf('0', '1', '0', '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '1', '1', '0', '0', '1', '0', '0'),
//            charArrayOf('0', '1', '0', '0', '1', '1', '1', '1', '0', '0', '0', '1', '0', '0', '1', '1', '1', '1', '0', '0'),
//            charArrayOf('0', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '0'),
//            charArrayOf('1', '1', '1', '0', '0', '0', '0', '0', '1', '0', '1', '1', '1', '0', '0', '0', '0', '0', '1', '0'),
//            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
//            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
//            charArrayOf('0', '0', '1', '0', '0', '0', '0', '1', '1', '1', '0', '0', '1', '0', '0', '0', '0', '1', '1', '1'),
//            charArrayOf('0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1'),
//            charArrayOf('0', '1', '0', '1', '0', '1', '1', '1', '0', '1', '0', '1', '0', '1', '0', '1', '1', '1', '0', '1'),
//            charArrayOf('0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0'),
//            charArrayOf('0', '1', '0', '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '1', '1', '0', '0', '1', '0', '0'),
//            charArrayOf('0', '1', '0', '0', '1', '1', '1', '1', '0', '0', '0', '1', '0', '0', '1', '1', '1', '1', '0', '0'),
//            charArrayOf('S', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '0')
//    )

    val direction = arrayOf(     //上左右下
            intArrayOf(-1, 0),
            intArrayOf(0, -1),
            intArrayOf(0, 1),
            intArrayOf(1, 0)
    )

    var limit: Int
    var minValue: Int = Int.MAX_VALUE
    var flag = false

    val tempPath = Array(100, { _ -> -1})
    val path = ArrayList<Int>()

    lateinit var start: Node
    lateinit var goal: Node

    for (i in 0 until m) {
        for (j in 0 until n) {
            if (maze[i][j] == 'S') {
                start = Node(i, j)      //起点
            }
            if (maze[i][j] == 'G') {
                goal = Node(i, j)       //终点
            }
        }
    }
    start.calculate(goal)
    limit = start.value

    fun dfs(node: Node, preMove: Int) {
        if (isEqual(node, goal)) {
            flag = true
            for (item in tempPath) {
                if (item != -1)
                    path.add(item)
                else
                    break
            }
            return
        }
        for (i in 0..3) {
            if (i + preMove == 3 && node.depth > 0)
                continue
            val x = node.x + direction[i][0]
            val y = node.y + direction[i][1]
            if (x in 0..(m - 1) && y in 0..(n - 1) && maze[x][y] != '1') {
                val newNode = Node(x, y)
                newNode.depth = node.depth + 1
                newNode.calculate(goal)

                if (newNode.value <= limit) {
                    tempPath[node.depth] = i
                    dfs(newNode, i)
                    if (flag)
                        return
                } else {
                    minValue = min(minValue, newNode.value)
                }
            }
        }
    }

    val startTime = System.currentTimeMillis()

    do {
        minValue = Int.MAX_VALUE
        dfs(start,  0)
        limit = minValue
    } while(!flag)
    val endTime = System.currentTimeMillis()

    println("time=${endTime - startTime}ms")
    println("length=${path.size}")
    for (item in path) {
        when(item) {
            0 -> print("U")
            1 -> print("L")
            2 -> print("R")
            3 -> print("D")
        }
    }

}