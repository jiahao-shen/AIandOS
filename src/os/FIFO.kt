package os

import java.util.*

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    class MemoryPiece(var value: Int, var time: Int = 0)

    println("请输入物理块的个数:")

    val numOfMemoryPiece = scan.nextInt()       //物理内存块数目

    println("请输入逻辑页的个数:")

    val numOfPage = scan.nextInt()      //逻辑页块数

    val memoryPieceList = ArrayList<MemoryPiece>()      //物理内存块list
    val pageList = ArrayList<Int>()      //页号队列

    println("逻辑页号初始化成功:")

    var cntOfExchange = 0       //页面置换次数
    var cntOfLack = 0       //缺页中断次数

    //随机生成页号队列
    for (i in 0 until Random().nextInt(20) + numOfMemoryPiece) {
        var value: Int
        do {
            value = Random().nextInt(numOfPage)
        } while (pageList.size != 0 && pageList.last() == value)
        pageList.add(value)
        print("$value\t")
    }
    println()

    //检查物理内存块中是否包含当前页号
    fun containsPageInMemoryPiece(pageNumber: Int): Int {
        for ((index, item) in memoryPieceList.withIndex()) {
            if (item.value == pageNumber)
                return index
        }
        return -1
    }

    //查找最老的区块
    fun searchEarliestPiece(): Int {
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

    //输出
    fun output() {
        for (item in memoryPieceList) {
            print("${item.value}\t")
        }
        println()
    }

    //时间+1
    fun countTime() {
        for (item in memoryPieceList)
            item.time++
    }
    //遍历
    for (item in pageList) {
        countTime()
        if (containsPageInMemoryPiece(item) == -1) {      //不在内存块中
            if (memoryPieceList.size < numOfMemoryPiece) {
                memoryPieceList.add(MemoryPiece(item))
            } else {
                cntOfExchange++
                val replacedIndex = searchEarliestPiece()
                memoryPieceList[replacedIndex].value = item
                memoryPieceList[replacedIndex].time = 0
            }
            output()
            cntOfLack++
        }
    }

    println("缺页中断的次数为:$cntOfLack")
    println("页面置换的次数为:$cntOfExchange")
}
