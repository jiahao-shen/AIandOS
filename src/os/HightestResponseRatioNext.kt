package os

import java.util.*
import kotlin.Comparator

fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)
    val inf = 1e-7

    class PCBNode(var name: String, var arriveTime: Int, var serviceTime: Int) {
        var runTime = 0
        var finishTime = -1
        var turnOverTime = -1
        var weight = 0.0
    }

    val compareByResponse = Comparator<PCBNode> { o1, o2 ->     //根据相应比排序
        if (o1.weight - o2.weight < -inf)
            return@Comparator 1
        else if (o1.weight - o2.weight > inf)
            return@Comparator -1
        0
    }


    var readyQueue = PriorityQueue<PCBNode>(compareByResponse)

    val compareByArriveTime = Comparator { o1: PCBNode, o2: PCBNode ->      //根据到达时间排序
        o1.arriveTime - o2.arriveTime
    }

    val waitQueue = PriorityQueue<PCBNode>(compareByArriveTime)         //等待队列
    val finishQueue = ArrayList<PCBNode>()      //进程结束队列

    var totalTime = 0
    val n: Int

    println("请输入进程数量n:")
    n = scan.nextInt()

    println("请输入进程名称,到达时间和服务时间(例如:A 0 1):")
    for (i in 0 until n) {
        val name = scan.next()
        val arriveTime = scan.nextInt()
        val serviceTime = scan.nextInt()
        val node = PCBNode(name, arriveTime, serviceTime)
        waitQueue.add(node)
    }

    var currentNode: PCBNode? = null
    var tempQueue: PriorityQueue<PCBNode>

    while (true) {
        if (finishQueue.size == n)
            break
        while (!waitQueue.isEmpty() && waitQueue.peek().arriveTime == totalTime) {
            val waitTop = waitQueue.poll()
            waitTop.weight = (totalTime - waitTop.arriveTime + waitTop.serviceTime) * 1.0 / (waitTop.serviceTime)
            readyQueue.add(waitTop)
        }
        if (!readyQueue.isEmpty()) {        //更新weight
            tempQueue = PriorityQueue(compareByResponse)
            for (item in readyQueue) {
                item.weight = (totalTime - item.arriveTime + item.serviceTime) * 1.0 / (item.serviceTime)
                tempQueue.add(item)
            }
            readyQueue = tempQueue
        }
        if (!readyQueue.isEmpty() && currentNode == null) {     //当前没有正在执行的进程
            currentNode = readyQueue.poll()
        }
        if (currentNode != null) {      //当前有进程在执行
            currentNode.runTime++       //运行时间+1
            if (currentNode.runTime == currentNode.serviceTime) {        //运行完成
                currentNode.finishTime = totalTime + 1      //记录结束时间
                currentNode.turnOverTime = currentNode.finishTime - currentNode.arriveTime      //周转时间
                finishQueue.add(currentNode)     //往结果队列添加
                currentNode = null
            }
        }
        totalTime++
    }

    var aveTurnOverTime = 0.0
    var aveTurnOverTimeWithWeight = 0.0
    var validWorkTime = 0.0
    val utilizationOfCPU: Double

    print("进程的调度顺序为:")
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
}