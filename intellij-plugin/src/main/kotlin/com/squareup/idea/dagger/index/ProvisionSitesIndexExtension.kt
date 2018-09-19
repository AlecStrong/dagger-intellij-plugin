package com.squareup.idea.dagger.index

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.DefaultFileTypeSpecificInputFilter
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.DataExternalizer
import com.intellij.util.io.EnumeratorStringDescriptor
import org.jetbrains.kotlin.idea.KotlinFileType

class ProvisionSitesIndexExtension : FileBasedIndexExtension<String, List<Site>>() {
  private val inputFilter = DefaultFileTypeSpecificInputFilter(
      JavaFileType.INSTANCE, KotlinFileType.INSTANCE)
  private val keyDescriptor = EnumeratorStringDescriptor()
  private val valueExternalizer: DataExternalizer<List<Site>> = SiteListDataExternalizer()
  private val indexer = ProvisionSitesDataIndexer()

  override fun getName() = NAME
  override fun getVersion() = VERSION
  override fun dependsOnFileContent() = true
  override fun getInputFilter() = inputFilter
  override fun getKeyDescriptor() = keyDescriptor
  override fun getValueExternalizer() = valueExternalizer
  override fun getIndexer() = indexer

  class ProvisionSitesDataIndexer : DataIndexer<String, List<Site>, FileContent> {
    override fun map(inputData: FileContent): Map<String, List<Site>> {
      return TextBasedDaggerSitesLocator.findProvisionSites(inputData.fileName,
          inputData.contentAsText.toString())
    }
  }

  companion object {
    val NAME = ID.create<String, List<Site>>(ProvisionSitesIndexExtension::class.qualifiedName!!)
    private const val VERSION = 1 // increment to rebuild cache
  }
}
