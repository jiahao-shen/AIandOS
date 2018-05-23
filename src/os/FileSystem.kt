package os

import java.util.*


val scan = Scanner(System.`in`)

/**
 * @param fileName(文件名称)
 * @param type(文件类型,0文件,1文件夹)
 * @param permission(权限,0不可读不可写,1可读不可写,2可读可写
 * @param content(文件内容)
 * @param fatherNode(父目录)
 */
class FileNode(var fileName: String, var type: Int, var permission: Int, var size: Int, var content: String?, var fatherNode: FileNode?)

val fileList = ArrayList<FileNode>()        //文件列表

lateinit var currentPath: FileNode      //当前路径

//初始化文件系统
fun initFileSystem() {
    val rootNode = FileNode("/", 1, 0, 0, null, null)
    currentPath = rootNode
    fileList.add(rootNode)
    fileList.add(FileNode("bin", 1, 1, 0, null, rootNode))
    fileList.add(FileNode("usr", 1, 2, 0, null, rootNode))
    fileList.add(FileNode("etc", 1, 2, 0, null, rootNode))
}

//登录
fun login() {
    val USERNAME = "sam"
    val PASSWORD = "258667"
    while (true) {
        print("Please enter your username:")
        val username = scan.next()
        print("Please enter your password:")
        val password = scan.next()
        if (username == USERNAME && password == PASSWORD) {
            println("Login Success")
            return
        } else {
            println("Password Error")
        }
    }
}

//列出当前路径的所有文件和文件夹
fun listAllFile() {
    System.out.printf("%-15s%-15s%-15s\n", "Name", "Size", "Permission")
    println("-----------------------------------------------------------")
    for (item in fileList) {
        if (item.fatherNode == currentPath) {
            val permission = when (item.permission) {
                0 -> "--"
                1 -> "R-"
                2 -> "RW"
                else -> "--"
            }
            System.out.printf("%-15s%-15s%-15s\n", item.fileName, item.size, permission)
        }
    }

}

//改变当前路径
fun changeDir(name: String) {
    when (name) {
        ".." -> {       //退到父目录
            currentPath.fatherNode?.let {
                currentPath = currentPath.fatherNode!!
            }
        }
        else -> {
            for (item in fileList) {
                if (item.fatherNode == currentPath && item.type == 1 && item.fileName == name) {        //找到对应名称的文件夹
                    if (item.permission == 0) {     //没有进入权限
                        println("cd: permission denied: $name")
                    } else {    //否则更改当前路径
                        currentPath = item
                    }
                    return
                }
            }
            println("cd: no such directory: $name")     //没有该目录
        }
    }
}

//创建文件夹
fun createDir(name: String) {
    for (item in fileList) {
        if (item.fatherNode == currentPath && item.type == 1 && item.fileName == name) {    //文件夹已经存在
            println("mkdir: $name: File exists")
            return      //直接return
        }
    }
    if (currentPath.permission == 2)        //当前文件夹有权限修改
        fileList.add(FileNode(name, 1, 2, 0, null, currentPath))
    else {
        println("mkdir: $name: Permission denied")      //当前文件夹没有权限创建
    }
}

//删除文件
fun removeFile(name: String) {
    for (item in fileList) {
        if (item.fatherNode == currentPath && item.fileName == name) {      //找到对应文件
            if (currentPath.permission == 2) {     //当前路径有写权限
                print("Are you sure to delete $name? ")
                when (scan.next()) {
                    "Yes", "yes" -> {
                        fileList.remove(item)
                        var node = currentPath      //递归更新大小
                        while (true) {
                            node.size -= item.size
                            if (node.fatherNode != null)
                                break
                            else
                                node = node.fatherNode!!
                        }
                    }
                }
            } else {        //当前路径没有删除权限
                println("rm: $name: Permission denied")
            }
            return
        }
    }
}

//展示当前路径
fun showCurrentPath() {
    val path = ArrayList<String>()
    var node = currentPath
    while (true) {
        if (node.fatherNode != null) {
            path.add(node.fileName)
            node = node.fatherNode!!
        } else {
            break
        }
    }
    path.reverse()
    if (path.size == 0)
        println("/")
    else {
        for (item in path) {
            print("/$item")
        }
        println()
    }
}

//编辑文件
fun vimFile(name: String) {
    for (item in fileList) {
        if (item.fatherNode == currentPath && item.fileName == name) {      //根据名字查找
            when (item.type) {
                0 -> {          //如果是文件夹则不能编辑
                    println("vim: $name: cannot edit a directory")
                }
                1 -> {      //否则直接编辑
                    writeFile(item)
                }
            }
            return
        }
    }
    createFile(name)        //创建新文件
}

fun createFile(name: String) {
    if (currentPath.permission == 2) {      //当前路径有写权限
        val file = FileNode(name, 0, 2, 0, null, currentPath)       //创建新文件
        writeFile(file)     //写文件
        fileList.add(file)      //添加
        println("create: $name: success")
    } else {
        println("vim: $name: Permission denied")       //当前路径没有写权限
    }
}

//写文件
fun writeFile(file: FileNode) {
    when (file.permission) {        //文件权限判断
        0, 1 -> {       //没有写权限
            println("vim: ${file.fileName}: Permission denied" )
            return
        }
        2 -> {      //有写权限
            println("Input content(end with enter):")
            val content = scan.nextLine()
            file.content = content
            file.size = content.length
            var node = currentPath      //递归增加文件大小
            while (true) {
                node.size += file.size
                if (node.fatherNode == null)
                    break
                else
                    node = node.fatherNode!!
            }
        }
    }
}

//读文件
fun readFile(name: String) {
    for (item in fileList) {
        if (item.fatherNode == currentPath && item.fileName == name) {
            if (item.type == 1) {       //不能输出一个文件夹
                println("cat: $name: cannot cat a directory")
                return
            }
            when (item.permission) {
                0 -> {      //没有读权限
                    println("cat: $name: Permission denied")
                    return
                }
                else -> {       //有读权限则输出内容
                    println("The content of file:")
                    println(item.content)
                }
            }
        }
    }
}

//更改权限
fun changePermission(name: String, permission: Int) {
    for (item in fileList) {
        if (item.fatherNode == currentPath && item.fileName == name) {
            item.permission = permission
            return
        }
    }
}

fun main(args: Array<String>) {
    initFileSystem()

    login()

    while (true) {
        val input = scan.nextLine().split("\\s+".toRegex())
        when (input[0]) {
            "ls" -> {
                listAllFile()
            }
            "mkdir" -> {
                createDir(input[1])
            }
            "cd" -> {
                changeDir(input[1])
            }
            "rm" -> {
                removeFile(input[1])
            }
            "vim" -> {
                vimFile(input[1])
            }
            "cat" -> {
                readFile(input[1])
            }
            "exit" -> {
                System.exit(0)
            }
            "pwd" -> {
                showCurrentPath()
            }
            "chmod" -> {
                changePermission(input[2], input[1].toInt())
            }
        }
    }
}