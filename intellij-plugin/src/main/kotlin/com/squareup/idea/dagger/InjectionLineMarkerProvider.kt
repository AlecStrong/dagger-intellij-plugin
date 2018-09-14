package com.squareup.idea.dagger

import com.intellij.codeHighlighting.Pass.UPDATE_ALL
import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.LEFT
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.refactoring.toPsiFile
import org.jetbrains.uast.UField
import org.jetbrains.uast.UFile
import org.jetbrains.uast.toUElement
import org.jetbrains.uast.toUElementOfType

class InjectionLineMarkerProvider : LineMarkerProvider {
  override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
    val uElement = element.toUElement()
    if (uElement is UField) {
      val injectAnnotation = uElement.findAnnotation("javax.inject.Inject")
      if (injectAnnotation != null) {
        if (uElement.findAnnotation("Inject") != null) {
          ProjectFileIndex.getInstance(element.project).iterateContent { vFile ->
            val uFile = vFile.toPsiFile(element.project)?.toUElementOfType<UFile>() ?: return@iterateContent true
            uFile.classes.forEach {
              println(it)
            }
            return@iterateContent true
          }
        }
        return LineMarkerInfo(element, element.textRange, ICON, UPDATE_ALL, null,
            DefaultGutterIconNavigationHandler(listOf(uElement), "title"), LEFT)
      }
    }
    return null
  }

  override fun collectSlowLineMarkers(
    elements: MutableList<PsiElement>,
    result: MutableCollection<LineMarkerInfo<PsiElement>>
  ) {
    // do nothing
  }

  companion object {
    private val ICON = IconLoader.getIcon("/icons/inject.png")
  }
}
