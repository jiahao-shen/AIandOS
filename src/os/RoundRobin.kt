package os

import java.util.*

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    class PCBNode(var name: String, var arriveTime: Int, var serviceTime: Int) : Comparable<PCBNode> {
        var runTime = 0        //进程已经运行的时间
        var finishTime = -1     //进程结束的时间
        var turnOverTime = -1       //进程的轮转时间

        override fun compareTo(other: PCBNode): Int {
            return arriveTime - other.arriveTime
        }
    }
    val inputQueue = ArrayList<PCBNode>()       //输入信息

    println("请输入进程数量n:")
    val n: Int = scan.nextInt()

    println("输入进程名称,到达时间和服务时间(例如:A 0 1):")
    for (i in 0 until n) {
        val name = scan.next()
        val arriveTime = scan.nextInt()
        val serviceTime = scan.nextInt()
        val node = PCBNode(name, arriveTime, serviceTime)
        inputQueue.add(node)
    }

    while (true) {
        println("输入时间片长度(输入0退出):")
        val pieceOfTime: Int = scan.nextInt()      //时间片长度
        if (pieceOfTime == 0)
            break
        var flag = false
        var piece = 0

        var totalTime = 0        //时间
        val readyQueue = LinkedList<PCBNode>()      //就绪队列
        val waitQueue = PriorityQueue<PCBNode>()    //等待队列
        val dispatchQueue = ArrayList<String>()
        val finishQueue = ArrayList<PCBNode>()        //进程完成的队列

        for (item in inputQueue) {
            item.runTime = 0
            item.finishTime = -1
            item.turnOverTime = -1
            waitQueue.add(item)
        }

        while (finishQueue.size != n) {
            while (!waitQueue.isEmpty() && waitQueue.peek().arriveTime == totalTime) {      //取出头结点判断是否到达时间
                val waitTop = waitQueue.poll()      //到达则取出头结点放入就绪队列
                readyQueue.add(waitTop)
            }
            if (piece % pieceOfTime == 0 && !flag && !readyQueue.isEmpty()) {       //如果一个时间片到达且进程没有执行完毕
                piece = 0
                val top = readyQueue.poll()     //取出就绪队列的头结点并放入队尾
                readyQueue.add(top)
            }
            flag = false
            if (!readyQueue.isEmpty()) {        //就绪队列非空
                val readyTop = readyQueue.peek()       //取头结点
                dispatchQueue.add(readyTop.name)
                readyTop.runTime++      //进行时间+1
                piece++
                if (readyTop.runTime == readyTop.serviceTime) {     //如果该进程执行完毕
                    readyTop.finishTime = totalTime + 1      //则设置结束时间
                    readyTop.turnOverTime = readyTop.finishTime - readyTop.arriveTime    //计算轮转时间
                    finishQueue.add(readyTop)        //添加到result中
                    flag = true      //一个进程结束则直接弹出
                    piece = 0
                    readyQueue.poll()
                }
            }
            totalTime++
        }
        var aveTurnOverTime = 0.0
        var aveTurnOverTimeWithWeight = 0.0
        var validWorkTime = 0.0
        val utilizationOfCPU: Double

        print("进程的调度顺序为:")
        for ((index, value) in dispatchQueue.withIndex()) {
            if (index == dispatchQueue.size - 1)
                println(value)
            else
                print("$value->")
        }

        print("进程完成的先后顺序为:")
        for ((index, value) in finishQueue.withIndex()) {
            if (index == finishQueue.size - 1)
                println(value.name)
            else
                print("${value.name}->")
            aveTurnOverTime += value.turnOverTime
            aveTurnOverTimeWithWeight += value.turnOverTime * 1.0 / value.serviceTime
            validWorkTime += value.serviceTime
        }
        aveTurnOverTime /= n
        aveTurnOverTimeWithWeight /= n
        utilizationOfCPU = validWorkTime * 100 / totalTime
        println("平均周转时间:$aveTurnOverTime")
        println("平均带权周转时间:$aveTurnOverTimeWithWeight")
        println("CPU利用率:$utilizationOfCPU%")
        println()
    }

}
