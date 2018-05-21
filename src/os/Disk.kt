package os

import java.lang.Math.abs
import java.lang.Math.min
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.ceil

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    val diskRequestList = ArrayList<Int>()

//    println("请输入磁道个数:")
//    val n = scan.nextInt()
//
//    for (i in 0 until n) {
//        diskRequestList.add(scan.nextInt())
//    }

    diskRequestList.add(55)
    diskRequestList.add(58)
    diskRequestList.add(39)
    diskRequestList.add(18)
    diskRequestList.add(90)
    diskRequestList.add(160)
    diskRequestList.add(150)
    diskRequestList.add(38)
    diskRequestList.add(184)

//    println("请输入磁头当前位置:")
    val currentDisk = 100

//    currentDisk = scan.nextInt()

    println("磁道序列初始化成功:")
    for (item in diskRequestList) {
        print("$item\t")
    }
    println()
    println("磁头当前位置:$currentDisk")

    fun outputResult(moveQueue: ArrayList<Int>, moveDistance: Int) {
        println("磁道访问顺序为:")
        for (item in moveQueue) {
            print("$item\t")
        }
        println()
        println("平均寻道长度为:${moveDistance * 1.0 / moveQueue.size}")
    }

    fun firstComeFirstService() {
        val moveQueue = ArrayList<Int>()
        var moveDistance = 0
        var current = currentDisk
        for (item in diskRequestList) {
            moveDistance += abs(current - item)
            current = item
            moveQueue.add(current)
        }
        outputResult(moveQueue, moveDistance)
    }

    fun shortestSeekTimeFirst() {
        val moveQueue = ArrayList<Int>()
        var moveDistance = 0
        var current = currentDisk
        val visited = Array(diskRequestList.size, { _ -> false })
        while (moveQueue.size < diskRequestList.size) {
            var min = Integer.MAX_VALUE
            var result: Int = -1
            for ((index, item) in diskRequestList.withIndex()) {
                if (!visited[index] && abs(current - item) < min) {
                    min = abs(current - item)
                    result = index
                }
            }
            moveDistance += abs(current - diskRequestList[result])
            visited[result] = true
            current = diskRequestList[result]
            moveQueue.add(current)
        }
        outputResult(moveQueue, moveDistance)
    }

    fun scan() {
        val moveQueue = ArrayList<Int>()
        var moveDistance = 0
        var current = currentDisk
        val visited = Array(diskRequestList.size, { _ -> false })

        fun searchDisk(): Int {
            var min1 = Integer.MAX_VALUE
            var min2 = Integer.MAX_VALUE
            var result1 = -1
            var result2 = -1
            for ((index, value) in diskRequestList.withIndex()) {
                if (!visited[index] && value - current in 0 until min1) {
                    min1 = value - current
                    result1 = index
                } else if (!visited[index] && current - value in 0 until min2) {
                    min2 = current - value
                    result2 = index
                }
            }
            return if (result1 != -1)
                result1
            else
                result2
        }

        while (moveQueue.size < diskRequestList.size) {
            val index = searchDisk()
            moveDistance += abs(diskRequestList[index] - current)
            visited[index] = true
            current = diskRequestList[index]
            moveQueue.add(current)
        }

        outputResult(moveQueue, moveDistance)
    }

    fun circleScan() {
        val moveQueue = ArrayList<Int>()
        var moveDistance = 0
        var current = currentDisk

        val orderList = ArrayList<Int>()
        for (item in diskRequestList) {
            orderList.add(item)
        }
        orderList.sort()
        var index = 0
        while (orderList[index] < current) {
            index++
        }
        for (i in index until orderList.size) {
            moveDistance += abs(orderList[i] - current)
            current = orderList[i]
            moveQueue.add(current)
        }
        for (i in 0 until index) {
            moveDistance += abs(orderList[i] - current)
            current = orderList[i]
            moveQueue.add(current)
        }
        outputResult(moveQueue, moveDistance)
    }

    fun nStepScan() {
        println("请输入子队列个数:")
        val num = scan.nextInt()
        val pieceSize = diskRequestList.size / num

        var moveDistance = 0
        val moveQueue = ArrayList<Int>()

        val visited = Array(diskRequestList.size, { _ -> false })
        var current = currentDisk

        var start = 0
        val boundList = HashMap<Int, Int>()
        for (i in 0 until num) {
            if (i == num - 1) {
                boundList[start] = diskRequestList.size - 1
            } else {
                boundList[start] = start + pieceSize - 1
                start += pieceSize
            }
        }

        fun searchDisk(start: Int, end: Int): Int {
            var min1 = Integer.MAX_VALUE
            var min2 = Integer.MAX_VALUE
            var result1 = -1
            var result2 = -1
            for (i in start..end) {
                if (!visited[i] && diskRequestList[i] - current in 0 until min1) {
                    min1 = diskRequestList[i] - current
                    result1 = i
                } else if (!visited[i] && current - diskRequestList[i] in 0 until min2) {
                    min2 = current - diskRequestList[i]
                    result2 = i
                }
            }
            return if (result1 != -1)
                result1
            else
                result2
        }

        for (item in boundList) {
            println("${item.key} => ${item.value}")
            for (i in 0..(item.value - item.key)) {
                val index = searchDisk(item.key, item.value)
                moveDistance += abs(diskRequestList[index] - current)
                visited[index] = true
                current = diskRequestList[index]
                moveQueue.add(current)
            }
        }

        outputResult(moveQueue, moveDistance)
    }

    while (true) {
        println("请选择算法:")
        println("1.FCFS\t\t\t2.SSTF")
        println("3.SCAN\t\t\t4.CSCAN")
        println("5.NStepSCAN\t\t6.退出")
        when (scan.nextInt()) {
            1 -> {
                firstComeFirstService()
            }
            2 -> {
                shortestSeekTimeFirst()
            }
            3 -> {
                scan()
            }
            4 -> {
                circleScan()
            }
            5 -> {
                nStepScan()
            }
            6 -> {
                System.exit(0)
            }
        }
        println("------------------------------------------------------------------------------------------------------------------------------")
    }

}