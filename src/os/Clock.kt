package os

import java.util.*

fun main(args: Array<String>) {

    val scan = Scanner(System.`in`)

    class MemoryPiece(var value: Int, var visited: Int = 1, var writed: Int = (Random().nextInt(10) % 2))

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

    //查找该页号是否存在物理内存中
    fun containsPageInMemoryPiece(pageNumber: Int): Int {
        for ((index, item) in memoryPieceList.withIndex()) {
            if (item.value == pageNumber)
                return index
        }
        return -1
    }

    fun output() {
        for (item in memoryPieceList) {
            if (item.writed == 0)
                print("${item.value}\t")
            else
                print("${item.value}*\t")
        }
        println()
    }

    fun searchReplacedPiece(): Int {
        for ((index, item) in memoryPieceList.withIndex()) {
            if (item.visited == 0 && item.writed == 0)
                return index
        }
        for ((index, item) in memoryPieceList.withIndex()) {
            if (item.visited == 0 && item.writed == 1)
                return index
            else
                item.visited = 0
        }
        return searchReplacedPiece()
    }

    for (item in pageList) {
        val searchIndex = containsPageInMemoryPiece(item)
        if (searchIndex == -1) {      //不在内存块中
            if (memoryPieceList.size < numOfMemoryPiece) {
                memoryPieceList.add(MemoryPiece(item))
            } else {
                cntOfExchange++
                val replacedIndex = searchReplacedPiece()
                memoryPieceList[replacedIndex].value = item
                memoryPieceList[replacedIndex].visited = 1
                memoryPieceList[replacedIndex].writed = Random().nextInt(10) % 2
            }
            output()
            cntOfLack++
        } else {        //已经在内存中
            memoryPieceList[searchIndex].visited = 1
            memoryPieceList[searchIndex].writed = Random().nextInt(10) % 2
        }
    }

    println("缺页中断的次数为:$cntOfLack")
    println("页面置换的次数为:$cntOfExchange")
}