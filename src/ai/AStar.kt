package ai

import java.util.*

/**
 * A*(maze)
 */
fun main(args: Array<String>) {

    class Node(newX: Int, newY: Int): Comparable<Node> {
        var x = newX
        var y = newY
        var value = 0
        var depth = 0
        var father: Node? = null

        fun calculate(goal: Node) {
            value = depth + Math.abs(x - goal.x) + Math.abs(y - goal.y)
        }

        override fun compareTo(other: Node): Int {
            return value - other.value
        }
    }

    fun isEqual(a: Node, b: Node): Boolean {
        if (a.x == b.x && a.y == b.y)
            return true
        return false
    }

//    val m = 10
//    val n = 10
//
//    val maze = arrayOf(       //S是起点,G是终点
//            charArrayOf('1', '1', '1', '0', '0', '0', '0', '0', '1', 'G'),
//            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
//            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
//            charArrayOf('0', '0', '1', '0', '0', '0', '0', '1', '1', '1'),
//            charArrayOf('0', '0', '0', '1', '0', '1', '0', '0', '0', '1'),
//            charArrayOf('0', '1', '0', '1', '0', '1', '1', '1', '0', '1'),
//            charArrayOf('0', '1', '0', '1', '0', '0', '0', '1', '0', '0'),
//            charArrayOf('0', '1', '0', '1', '1', '0', '0', '1', '0', '0'),
//            charArrayOf('0', '1', '0', '0', '1', '1', '1', '1', '0', '0'),
//            charArrayOf('S', '1', '1', '0', '0', '0', '0', '0', '0', '0')
//    )
    val m = 20
    val n = 20
    val maze = arrayOf(       //S是起点,G是终点
            charArrayOf('1', '1', '1', '0', '0', '0', '0', '0', '1', '0', '1', '1', '1', '0', '0', '0', '0', '0', '1', 'G'),
            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '1', '0', '0', '0', '0', '1', '1', '1', '0', '0', '1', '0', '0', '0', '0', '1', '1', '1'),
            charArrayOf('0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1'),
            charArrayOf('0', '1', '0', '1', '0', '1', '1', '1', '0', '1', '0', '1', '0', '1', '0', '1', '1', '1', '0', '1'),
            charArrayOf('0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0'),
            charArrayOf('0', '1', '0', '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '1', '1', '0', '0', '1', '0', '0'),
            charArrayOf('0', '1', '0', '0', '1', '1', '1', '1', '0', '0', '0', '1', '0', '0', '1', '1', '1', '1', '0', '0'),
            charArrayOf('0', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '0'),
            charArrayOf('1', '1', '1', '0', '0', '0', '0', '0', '1', '0', '1', '1', '1', '0', '0', '0', '0', '0', '1', '0'),
            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '1', '1', '1', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '1', '0', '0', '0', '0', '1', '1', '1', '0', '0', '1', '0', '0', '0', '0', '1', '1', '1'),
            charArrayOf('0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1'),
            charArrayOf('0', '1', '0', '1', '0', '1', '1', '1', '0', '1', '0', '1', '0', '1', '0', '1', '1', '1', '0', '1'),
            charArrayOf('0', '1', '0', '1', '0', '0', '0', '1', '0', '0', '0', '1', '0', '1', '0', '0', '0', '1', '0', '0'),
            charArrayOf('0', '1', '0', '1', '1', '0', '0', '1', '0', '0', '0', '1', '0', '1', '1', '0', '0', '1', '0', '0'),
            charArrayOf('0', '1', '0', '0', '1', '1', '1', '1', '0', '0', '0', '1', '0', '0', '1', '1', '1', '1', '0', '0'),
            charArrayOf('S', '1', '1', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '0', '0')
    )

    val direction = arrayOf(     //上下左右
            intArrayOf(-1, 0),
            intArrayOf(1, 0),
            intArrayOf(0, -1),
            intArrayOf(0, 1)
    )

    lateinit var start: Node
    lateinit var goal: Node

    val openSet = PriorityQueue<Node>()
    val closedSet = PriorityQueue<Node>()

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

    start.calculate(goal)       //计算起点的value
    openSet.add(start)          //添加到openSet

    val startTime = System.currentTimeMillis()
    while (!openSet.isEmpty()) {        //openSet非空
        val minNode = openSet.poll()        //取出头节点
        if (isEqual(minNode, goal)) {       //终点则break
            goal = minNode
            break
        }
        for (i in 0..3) {       //遍历上下左右方向
            val x = minNode.x + direction[i][0]
            val y = minNode.y + direction[i][1]
            if (x in 0..(m - 1) && y in 0..(n - 1) && maze[x][y] != '1') {      //在边界内且不是墙
                val node = Node(x, y)
                node.depth = minNode.depth + 1
                node.calculate(goal)

                var flag1 = false
                var flag2 = false

                for (item in openSet) {
                    if (isEqual(node, item)) {      //存在在openSet中
                        flag1 = true
                        if (node.value < item.value) {      //新的节点值更小
                            openSet.remove(item)        //从openSet中移除原有的节点
                            node.father = minNode       //设置父节点
                            openSet.add(node)           //放入openSet
                        }
                        break
                    }
                }

                if (!flag1) {           //不再openSet中
                    for (item in closedSet) {
                        if (isEqual(node, item)) {      //存在closedSet中
                            flag2 = true
                            break
                        }
                    }
                }
                if (!flag1 && !flag2) {     //两个表都不在
                    node.father = minNode       //设置父节点
                    openSet.add(node)           //加入openSet
                }
            }
        }
        closedSet.add(minNode)      //放入closedSet
    }
    val endTime = System.currentTimeMillis()

    var node = goal     //从终点开始回溯
    val path = ArrayList<Int>()     //路径
    while (true) {
        if (node.father != null) {
            for (j in 0..3) {
                if (node.father!!.x + direction[j][0] == node.x && node.father!!.y + direction[j][1] == node.y) {
                    path.add(j)
                    break
                }
            }
            node = node.father!!
        } else {
            break
        }
    }
    path.reverse()      //路径逆序
    println("length=${path.size}")
    println("time=${endTime - startTime}ms")
    for (item in path) {
        when (item) {
            0 -> print("U")
            1 -> print("D")
            2 -> print("L")
            3 -> print("R")
        }
    }

   
}