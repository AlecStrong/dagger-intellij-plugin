package com.squareup.idea.dagger.index

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Computable
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import org.jetbrains.kotlin.idea.refactoring.toPsiFile
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.toUElement
import java.io.File

/** A site (injection or provision) is a tuple of the file name and offset. */
typealias Site = Pair<String, Int>

fun findProvisionSites(typeName: String, project: Project): List<NavigatablePsiElement> {
  return ApplicationManager.getApplication().runReadAction(
      Computable<List<NavigatablePsiElement>> {
        val provisionSites = try {
          FileBasedIndex.getInstance().getValues(ProvisionSitesIndexExtension.NAME, typeName,
              GlobalSearchScope.allScope(project)).flatten()
        } catch (_: IndexNotReadyException) {
          return@Computable emptyList()
        }
        val elements = mutableListOf<NavigatablePsiElement>()
        for ((fileName, offset) in provisionSites) {
          val psiFile = VfsUtil.findFileByIoFile(File(fileName), true)?.toPsiFile(project)
          psiFile?.findElementAt(offset)?.let {
            elements.add(it.toUElement() as UMethod)
          }
        }
        return@Computable elements
      })
}
