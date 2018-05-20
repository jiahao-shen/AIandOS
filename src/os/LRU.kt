package os

import java.util.*

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    class MemoryPiece(var value: Int, var time: Int = 0)

    println("请输入物理块的个数:")

    val numOfMemoryPiece = scan.nextInt()

    println("请输入逻辑页的个数:")

    val numOfPage = scan.nextInt()

    val memoryPieceList = ArrayList<MemoryPiece>()
    val pageList = ArrayList<Int>()

    println("逻辑页号初始化成功:")

    var cntOfExchange = 0
    var cntOfLack = 0

    for (i in 0 until Random().nextInt(20) + numOfMemoryPiece) {
        var value: Int
        do {
            value = Random().nextInt(numOfPage)
        } while (pageList.size != 0 && pageList[pageList.size - 1] == value)
        pageList.add(value)
        print("$value\t")
    }
    println()

    fun containsPageInMemoryPiece(pageNumber: Int): Int {
        for ((index, item) in memoryPieceList.withIndex()) {
            if (item.value == pageNumber)
                return index
        }
        return -1
    }

    fun searchLeastRecentlyUsedPiece(): Int {
        var max = Integer.MIN_VALUE
        var result = -1
        for ((index, item) in memoryPieceList.withIndex()) {
            if (item.time > max) {
                result = index
                max = item.time
            }
        }
        return result
    }

    fun output() {
        for (item in memoryPieceList) {
            print("${item.value}\t")
        }
        println()
    }

    fun countTime() {
        for (item in memoryPieceList)
            item.time++
    }
    for (item in pageList) {
        countTime()
        val searchIndex = containsPageInMemoryPiece(item)
        if (searchIndex == -1) {      //不在内存块中
            if (memoryPieceList.size < numOfMemoryPiece) {
                memoryPieceList.add(MemoryPiece(item))
                output()
            } else {
                cntOfExchange++
                val replacedIndex = searchLeastRecentlyUsedPiece()
                memoryPieceList[replacedIndex].value = item
                memoryPieceList[replacedIndex].time = 0
                output()
            }
            cntOfLack++
        } else {
            memoryPieceList[searchIndex].time = 0
        }
    }

    println("缺页中断的次数为:$cntOfLack")
    println("页面置换的次数为:$cntOfExchange")
}

