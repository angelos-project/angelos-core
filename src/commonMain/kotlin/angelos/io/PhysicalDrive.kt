

package angelos.io

import angelos.nio.file.FileVault
import angelos.interop.FileSystem
import kotlin.jvm.JvmStatic

@Suppress("OVERRIDE_BY_INLINE")
class PhysicalDrive(drive: String) : FileVault(drive) {
    fun getRoot(): RealPath {
        return getPath(VirtualPath(drive))
    }

    fun getPath(path: VirtualPath): RealPath {
        return path.toRealPath(this)
    }

    override inline fun readFile(number: Int, array: ByteArray, index: Int, count: Long): Long =
        FileSystem.readFile(number, array, index, count)

    override inline fun writeFile(number: Int, array: ByteArray, index: Int, count: Long): Long =
        FileSystem.writeFile(number, array, index, count)

    override inline fun tellFile(number: Int): Long = FileSystem.tellFile(number)
    override inline fun seekFile(number: Int, position: Long, whence: FileDescriptor.Seek): Long =
        FileSystem.seekFile(number, position, whence)

    override inline fun closeFile(number: Int): Boolean = FileSystem.closeFile(number)

    override inline fun checkReadable(path: String): Boolean = FileSystem.checkReadable(path)
    override inline fun checkWritable(path: String): Boolean = FileSystem.checkWritable(path)
    override inline fun checkExecutable(path: String): Boolean = FileSystem.checkExecutable(path)
    override inline fun checkExists(path: String): Boolean = FileSystem.checkExists(path)
    override inline fun getFileType(path: String): Int = FileSystem.getFileType(path)
    override inline fun getFileInfo(path: String): FileObject.Info = FileSystem.getFileInfo(path)
    override inline fun getLinkTarget(path: String): String = FileSystem.getLinkTarget(path)
    override inline fun openDir(path: String): Long = FileSystem.openDir(path)
    override inline fun readDir(dir: Long): Dir.FileEntry = FileSystem.readDir(dir)
    override inline fun closeDir(dir: Long): Boolean = FileSystem.closeDir(dir)
    override inline fun openFile(path: String, option: Int): Int = FileSystem.openFile(path, option)

    enum class Drive(val root: String){
        UNIX("/"),
        DOS_A("A:\\"),
        DOS_B("B:\\"),
        DOS_C("C:\\"),
        DOS_D("D:\\"),
        DOS_E("E:\\"),
        DOS_F("F:\\"),
        DOS_G("G:\\"),
        DOS_H("H:\\"),
        DOS_I("I:\\"),
        DOS_J("J:\\"),
        DOS_K("K:\\"),
        DOS_L("L:\\"),
        DOS_M("M:\\"),
        DOS_N("N:\\"),
        DOS_O("O:\\"),
        DOS_P("P:\\"),
        DOS_Q("Q:\\"),
        DOS_R("R:\\"),
        DOS_S("S:\\"),
        DOS_T("T:\\"),
        DOS_V("V:\\"),
        DOS_W("W:\\"),
        DOS_X("X:\\"),
        DOS_Y("Y:\\"),
        DOS_Z("Z:\\")
    }

    companion object {
        private val fileStores: FileStores = FileStores()

        @JvmStatic
        fun createFileSystem(drive: Drive): PhysicalDrive {
            if (!fileStores.contains(drive.root))
                fileStores[drive.root] = PhysicalDrive(drive.root)
            return fileStores[drive.root]!!
        }
    }
}

typealias FileStores = HashMap<String, PhysicalDrive>