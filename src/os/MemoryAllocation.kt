package os

import java.util.*

fun main(args: Array<String>) {

    val scan = Scanner(System.`in`)

    class MemoryPiece(var startAddress: Int, var size: Int, var status: Boolean = false) : Comparable<MemoryPiece> {        //起始地址,大小,状态
        override fun compareTo(other: MemoryPiece): Int {       //按起始地址排序
            return startAddress - other.startAddress
        }
    }    //空闲区表结构

    val startAddressList = ArrayList<Int>()     //起始地址序列

    println("请输入空闲分区的个数:")

    val n: Int = scan.nextInt()     //空闲区个数
    val memoryTable = ArrayList<MemoryPiece>()      //空闲区表
    val minSize = 2         //不可再分割大小
    var lastIndex = 0      //NF算法中指向最后一次分配的

    fun output() {      //输出
        println("分区号\t\t分区始址\t\t分区大小\t\t状态")
        var index = 0
        for (item in memoryTable) {
            if (startAddressList.contains(item.startAddress)) {
                println("----------------------------------------------------------------------")
                if (item.status) {
                    println("$index\t\t\t${item.startAddress}\t\t\t${item.size}\t\t\tRunning")
                } else {
                    println("$index\t\t\t${item.startAddress}\t\t\t${item.size}\t\t\t未分配")
                }
                index++
            } else {
                if (item.status) {
                    println("\t\t\t${item.startAddress}\t\t\t${item.size}\t\t\tRunning")
                } else {
                    println("\t\t\t${item.startAddress}\t\t\t${item.size}\t\t\t未分配")
                }
            }
        }
    }       //输出空闲区表

    fun initFreeSpace() {
        var address = 0
        val addressFactor = 30
        val sizeFactor = 100
        for (i in 0 until n) {
            address += Random().nextInt(addressFactor)
            val size = Random().nextInt(sizeFactor) + minSize
            memoryTable.add(MemoryPiece(address, size, false))
            startAddressList.add(address)
            address += size + 1
        }
        println("空闲分区初始化成功")
        output()
    }       //初始化空闲区表

    initFreeSpace()

    println("请选择分配算法:")
    println("1.首次适应算法(FF)\t\t2.循环首次适应算法(NF)")
    println("3.最佳适应算法(BF)\t\t4.最坏适应算法(WF)")

    val type = scan.nextInt()       //分配算法

    fun firstFit(requestSize: Int): Int {
        for ((index, value) in memoryTable.withIndex()) {
            if (value.size > requestSize && !value.status)
                return index
        }
        return -1
    }       //首次适应

    //循环首次适应
    fun nextFit(requestSize: Int): Int {
        if (lastIndex >= memoryTable.size)
            lastIndex = 0
        for (index in lastIndex until memoryTable.size) {
            if (memoryTable[index].size >= requestSize && !memoryTable[index].status) {
                lastIndex = index
                return index
            }
        }
        for (index in 0 until lastIndex) {
            if (memoryTable[index].size >= requestSize && !memoryTable[index].status) {
                lastIndex = index
                return index
            }
        }
        return -1
    }

    //最佳适应
    fun bestFit(requestSize: Int): Int {
        var id = -1
        var min = Integer.MAX_VALUE
        for ((index, value) in memoryTable.withIndex()) {
            if (value.size - requestSize in 0..(min - 1) && !value.status) {
                id = index
                min = value.size - requestSize
            }
        }
        return id
    }

    //最坏适应
    fun worstFit(requestSize: Int): Int {
        var id = -1
        var max = Integer.MIN_VALUE
        for ((index, value) in memoryTable.withIndex()) {
            if (value.size - requestSize > max && !value.status) {
                id = index
                max = value.size - requestSize
            }
        }
        return id
    }

    //分配内存
    fun allocation(id: Int, requestSize: Int) {
        val freePiece = memoryTable[id]
        if (freePiece.size - requestSize < minSize) {
            freePiece.status = true
        } else {
            memoryTable.add(MemoryPiece(freePiece.startAddress, requestSize, true))
            freePiece.size -= requestSize
            freePiece.startAddress += requestSize
        }
        memoryTable.sort()
    }

    //请求内存
    fun requestMemory() {
        println("输入请求内存的大小:")
        val requestSize = scan.nextInt()        //请求内存大小
        val id = when (type) {
            1 -> {
                firstFit(requestSize)
            }
            2 -> {
                nextFit(requestSize)
            }
            3 -> {
                bestFit(requestSize)
            }
            4 -> {
                worstFit(requestSize)
            }
            else -> {
                -1
            }
        }

        if (id == -1) {
            println("分配失败")
        } else {
            println("分配成功")
            allocation(id, requestSize)
            output()
        }
    }

    //按照开始地址查找index
    fun searchIndexByStartAddress(startAddress: Int): Int {
        for ((index, value) in memoryTable.withIndex()) {
            if (value.startAddress == startAddress)
                return index
        }
        return -1
    }

    //释放内存
    fun releaseMemory() {
        println("请输入需要释放的进程在内存中的起始地址:")
        val startAddress = scan.nextInt()
        val index = searchIndexByStartAddress(startAddress)
        if (index in 0 until memoryTable.size) {
            if (!memoryTable[index].status) {
                println("该内存块没有正在运行的进程")
            } else {
                var flag1 = false
                var flag2 = false
                if (index - 1 >= 0 && !memoryTable[index - 1].status && memoryTable[index - 1].startAddress + memoryTable[index - 1].size == memoryTable[index].startAddress ) {
                    flag1 = true
                }
                if (index + 1 < memoryTable.size && !memoryTable[index + 1].status && memoryTable[index].startAddress + memoryTable[index].size == memoryTable[index + 1].startAddress) {
                    flag2 = true
                }
                if (flag1 && flag2) {
                    memoryTable[index - 1].size += memoryTable[index].size + memoryTable[index + 1].size
                    memoryTable[index - 1].status = false
                    memoryTable.removeAt(index)
                    memoryTable.removeAt(index)
                } else if (flag1 && !flag2) {
                    memoryTable[index - 1].size += memoryTable[index].size
                    memoryTable[index - 1].status = false
                    memoryTable.removeAt(index)
                } else if (!flag1 && flag2) {
                    memoryTable[index].size += memoryTable[index + 1].size
                    memoryTable[index].status = false
                    memoryTable.removeAt(index + 1)
                } else {
                    memoryTable[index].status = false
                }
                println("释放成功")
                output()
            }
        } else {
            println("不存在该进程")
        }
    }

    while (true) {
        println("请输入选择操作类型:")
        println("1.请求内存分配\t\t2.释放内存\t\t3.退出")
        when (scan.nextInt()) {
            1 -> {
                requestMemory()
            }
            2 -> {
                releaseMemory()
            }
            3 -> {
                System.exit(0)
            }
        }
    }

}