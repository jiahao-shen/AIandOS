package os

import java.util.*

fun main(args: Array<String>) {

    val scan = Scanner(System.`in`)

    class MemoryPiece(var startAddress: Int, var size: Int, var status: Boolean = false)

    val startAddressList = ArrayList<Int>()     //起始地址序列

    println("请输入空闲分区的个数:")

    val numOfMemoryPiece = scan.nextInt()
    val memoryList = LinkedList<MemoryPiece>()
    val minSize = 2
    var lastIndex = 0

    fun output() {
        System.out.printf("%-10s%-15s%-15s%-20s\n", "No", "Start", "Size", "Status")
        var index = 0
        for (item in memoryList) {
            if (startAddressList.contains(item.startAddress)) {
                println("----------------------------------------------------------------------")
                if (item.status) {
                    System.out.printf("%-10s%-15s%-15s%-20s\n", index, item.startAddress, item.size, "Running")
                } else {
                    System.out.printf("%-10s%-15s%-15s%-20s\n", index, item.startAddress, item.size, "未分配")
                }
                index++
            } else {
                if (item.status) {
                    System.out.printf("%-10s%-15s%-15s%-20s\n", "", item.startAddress, item.size, "Running")
                } else {
                    System.out.printf("%-10s%-15s%-15s%-20s\n", "", item.startAddress, item.size, "未分配")
                }
            }
        }
    }

    fun initFreeSpace() {
        var address = 0
        val addressFactor = 30
        val sizeFactor = 100

        for (i in 0 until numOfMemoryPiece) {
            address += Random().nextInt(addressFactor)
            val size = Random().nextInt(sizeFactor) + minSize
            memoryList.addLast(MemoryPiece(address, size))
            startAddressList.add(address)
            address += size + 1
        }
        println("空闲分区初始化成功")
        output()
    }

    initFreeSpace()

    println("请选择分配算法:")
    println("1.首次适应算法(FF)\t\t2.循环首次适应算法(NF)")
    println("3.最佳适应算法(BF)\t\t4.最坏适应算法(WF)")

    val type = scan.nextInt()       //分配算法

    fun firstFit(requestSize: Int): Int {
        for ((index, value) in memoryList.withIndex()) {
            if (value.size > requestSize && !value.status)
                return index
        }
        return -1
    }

    fun nextFit(requestSize: Int): Int {
        if (lastIndex >= memoryList.size)
            lastIndex = 0
        for (index in lastIndex until memoryList.size) {
            if (memoryList[index].size >= requestSize && !memoryList[index].status) {
                lastIndex = index
                return index
            }
        }
        for (index in 0 until lastIndex) {
            if (memoryList[index].size >= requestSize && !memoryList[index].status) {
                lastIndex = index
                return index
            }
        }
        return -1
    }

    fun bestFit(requestSize: Int): Int {
        var id = -1
        var min = Integer.MAX_VALUE
        for ((index, value) in memoryList.withIndex()) {
            if (value.size - requestSize in 0..(min - 1) && !value.status) {
                id = index
                min = value.size - requestSize
            }
        }
        return id
    }

    fun worstFit(requestSize: Int): Int {
        var id = -1
        var max = Integer.MIN_VALUE
        for ((index, value) in memoryList.withIndex()) {
            if (value.size - requestSize > max && !value.status) {
                id = index
                max = value.size - requestSize
            }
        }
        return id
    }

    fun allocation(id: Int, requestSize: Int) {
        val freePiece = memoryList[id]
        if (freePiece.size - requestSize < minSize) {
            freePiece.status = true
        } else {
            memoryList.add(id, MemoryPiece(freePiece.startAddress, requestSize, true))
            freePiece.size -= requestSize
            freePiece.startAddress += requestSize
        }
    }

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

    fun searchIndexByStartAddress(startAddress: Int): Int {
        for ((index, value) in memoryList.withIndex()) {
            if (value.startAddress == startAddress)
                return index
        }
        return -1
    }

    fun releaseMemory() {
        println("请输入要释放的进程在内存中的起始地址:")
        val startAddress = scan.nextInt()
        val index = searchIndexByStartAddress(startAddress)
        if (index in 0 until memoryList.size) {
            if (!memoryList[index].status) {
                println("该内存块没有正在运行的程序")
            } else {
                var flag1 = false
                var flag2 = false
                if (index - 1 >= 0 && !memoryList[index - 1].status && memoryList[index - 1].startAddress + memoryList[index - 1].size == memoryList[index].startAddress) {
                    flag1 = true
                }
                if (index + 1 < memoryList.size && !memoryList[index + 1].status && memoryList[index].startAddress + memoryList[index].size == memoryList[index + 1].startAddress) {
                    flag2 = true
                }
                if (flag1 && flag2) {
                    memoryList[index - 1].size += memoryList[index].size + memoryList[index + 1].size
                    memoryList[index - 1].status = false
                    memoryList.removeAt(index)
                    memoryList.removeAt(index)
                } else if (flag1 && !flag2) {
                    memoryList[index - 1].size += memoryList[index].size
                    memoryList[index - 1].status = false
                    memoryList.removeAt(index)
                } else if (!flag1 && flag2) {
                    memoryList[index].size += memoryList[index + 1].size
                    memoryList[index].status = false
                    memoryList.removeAt(index + 1)
                } else {
                    memoryList[index].status = false
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
