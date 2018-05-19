package ai

import java.util.*

/**
 * IDA*(8 puzzle)
 */
class SolvePuzzle(lists: ArrayList<Int>) {

    private val size = 3        //方格阶数
    private var sx = -1         //空白块起始x
    private var sy = -1         //空白块起始y
    var length = 0      //最优解长度

    private var puzzle: Array<IntArray> = Array(size) { IntArray(size) }        //构造sizexsize的二维数组
    private val move = arrayOf(intArrayOf(-1, 0), intArrayOf(0, -1), intArrayOf(0, 1), intArrayOf(1, 0))        //上左右下
    private var tempPath = IntArray(100)    //暂时路径
    private var path = IntArray(100)        //最终路径

    private var limit = 0       //上限
    private var flag = false        //找到解标志位

    init {      //初始化
        for ((index, value) in lists.withIndex()) {     //将传进来的一维数组转成二维数组
            if (value == size * size - 1) {
                sx = index / size
                sy = index % size
            }
            puzzle[index / size][index % size] = value
        }
    }

    private fun getManhattanValue(): Int {      //求解曼哈顿距离
        var value = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                val number = puzzle[i][j]
                value += Math.abs(i - number / size) + Math.abs(j - number % size)
            }
        }
        return value
    }

    /**
     * @param sx(空白块座标x)
     * @param sy(空白块座标y)
     * @param len(当前路径长度)
     * @param preMove(上一次的移动方向)
     */
    private fun dfs(sx: Int, sy: Int, len: Int, preMove: Int) {         //dfs
        val dis = getManhattanValue()       //获取曼哈顿距离
        if (dis == 0) {     //如果是目标解
            if (!flag) {        //第一次找到目标解
                flag = true     //标志位置true
                tempPath.indices.forEach { i -> path[i] = tempPath[i] }     //保存路径
                length = len        //保存路径长度
            } else {        //找到其他解
                if (len < length) {     //如果其他解更优
                    tempPath.indices.forEach { i -> path[i] = tempPath[i] }     //保存路径
                    length = len        //更新路径长度
                }
            }
            limit = length      //更新limit
            return
        }
        for (i in 0..3) {       //遍历上下左右四个方向
            if (i + preMove == 3 && len > 0)        //剔除preMove
                continue
            val nx = sx + move[i][0]        //空白块交换后的新座标
            val ny = sy + move[i][1]
            if (nx in 0..(size - 1) && ny in 0..(size - 1)) {       //如果没有移出边界
                puzzle.swap(sx, sy, nx, ny)     //则swap
                val value = getManhattanValue()     //求解新的曼哈顿距离
                if (value + len < limit) {     //value + len + 1 <= limit
                    tempPath[len] = i
                    dfs(nx, ny, len + 1, i)     //开始dfs
                }
                puzzle.swap(sx, sy, nx, ny)     //回溯
            }
        }
    }

    fun solve(): IntArray {
        limit = getManhattanValue()     //求解初始limit
        do {
            dfs(sx, sy, 0, 0)
            limit++
        } while(!flag)      //如果没有找到则limit+1

        return path
    }

    private fun Array<IntArray>.swap(index1_i: Int, index1_j: Int, index2_i: Int, index2_j: Int) {
        val temp = this[index1_i][index1_j]
        this[index1_i][index1_j] = this[index2_i][index2_j]
        this[index2_i][index2_j] = temp
    }
}
fun main(args: Array<String>) {
    val list = ArrayList<Int>(Arrays.asList(1, 2, 3, 0, 4, 8, 6, 5, 7))
    val sp = SolvePuzzle(list)
    val path = sp.solve()
    println(sp.length)
    for (i in 0 until sp.length) {
        when (path[i]) {
            0 -> print("U")
            1 -> print("L")
            2 -> print("R")
            3 -> print("D")
        }
    }
}