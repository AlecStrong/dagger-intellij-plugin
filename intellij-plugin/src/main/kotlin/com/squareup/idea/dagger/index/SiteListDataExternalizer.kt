package com.squareup.idea.dagger.index

import com.intellij.util.containers.ContainerUtil
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.DataInputOutputUtil
import org.jetbrains.kotlin.idea.core.util.readString
import org.jetbrains.kotlin.idea.core.util.writeString
import java.io.DataInput
import java.io.DataOutput
import java.io.IOException

internal class SiteListDataExternalizer : DataExternalizer<List<Site>> {
  @Throws(IOException::class)
  override fun save(out: DataOutput, value: List<Site>) {
    DataInputOutputUtil.writeINT(out, value.size)
    for ((fileName, offset) in value) {
      out.writeString(fileName)
      out.writeInt(offset)
    }
  }

  @Throws(IOException::class)
  override fun read(`in`: DataInput): List<Site> {
    val value = ContainerUtil.newSmartList<Site>()
    val size = DataInputOutputUtil.readINT(`in`)
    for (i in 0 until size) {
      val site = `in`.readString() to `in`.readInt()
      value.add(site)
    }
    return value
  }
}
